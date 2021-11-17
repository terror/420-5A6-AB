package com.example.asg4.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.asg4.R;
import com.example.asg4.databinding.ActivityTasksBinding;
import com.example.asg4.model.Action;
import com.example.asg4.model.Task;
import com.example.asg4.model.TaskDBHandler;
import com.example.asg4.notification.Alarm;
import com.example.asg4.sqlite.DatabaseException;
import com.example.asg4.ui.editor.TaskEditFragment;
import com.example.asg4.ui.list.TaskListFragment;
import com.example.asg4.viewmodel.TaskEditViewModel;
import com.example.asg4.viewmodel.TaskListViewModel;
import com.example.asg4.viewmodel.TaskRecyclerViewAdapterViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TasksActivity extends AppCompatActivity {
  // constants
  public static final String TASKS_NOTIFICATION_CHANNEL = "tasks-notification-channel";
  public static final String TASKS_NOTIFICATION_GROUP = "tasks-notifications";

  // saved task state
  public List<Task> tasks;
  private AppBarConfiguration appBarConfiguration;
  private ActivityTasksBinding binding;

  // fragments
  private TaskEditFragment taskEditFragment;
  private TaskListFragment taskListFragment;

  // view models
  private TaskEditViewModel taskEditViewModel;
  private TaskListViewModel taskListViewModel;
  private TaskRecyclerViewAdapterViewModel taskRecyclerViewAdapterViewModel;

  // database handler
  private TaskDBHandler taskDBHandler;

  public TasksActivity() {
    // create new view model instances
    taskEditViewModel = new TaskEditViewModel();
    taskListViewModel = new TaskListViewModel();
    taskRecyclerViewAdapterViewModel = new TaskRecyclerViewAdapterViewModel();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Getters & Setters                                                        ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public ActivityTasksBinding getBinding() {
    return binding;
  }

  public TaskEditViewModel getTaskEditViewModel() {
    return taskEditViewModel;
  }

  public TaskListViewModel getTaskListViewModel() {
    return taskListViewModel;
  }

  public TaskRecyclerViewAdapterViewModel getTaskRecyclerViewAdapterViewModel() {
    return taskRecyclerViewAdapterViewModel;
  }

  public List<Task> getTasks() {
    return tasks;
  }

  public TasksActivity setTasks(List<Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  public TasksActivity setTaskEditFragment(TaskEditFragment taskEditFragment) {
    this.taskEditFragment = taskEditFragment;
    return this;
  }

  public TasksActivity setTaskListFragment(TaskListFragment taskListFragment) {
    this.taskListFragment = taskListFragment;
    return this;
  }

  public TaskDBHandler getTaskDBHandler() {
    return taskDBHandler;
  }

  public TasksActivity setTaskDBHandler(TaskDBHandler taskDBHandler) {
    this.taskDBHandler = taskDBHandler;
    return this;
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Overridden methods                                                       ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // set the binding
    binding = ActivityTasksBinding.inflate(getLayoutInflater());

    // event listeners
    binding.fab.setOnClickListener(_view -> taskListFragment.handleAdd());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    // create the notification channel
    createChannel();

    // process intent
    Intent intent = getIntent();
    Bundle bundle = intent.getExtras();
    if (bundle != null) {
      // set the view model
      this.getTaskEditViewModel()
        .setAction(Action.EDIT)
        .setTask(Task.fromBundle(bundle));

      // hide `add` button
      this.getBinding().fab.setVisibility(View.GONE);

      // navigate to `edit` fragment
      Navigation
        .findNavController(this, R.id.nav_host_fragment_content_main)
        .navigate(R.id.navigateToTaskEditFragment);
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      // validate the current task and perform navigation on `ok`
      taskEditFragment.navigateBack();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
      || super.onSupportNavigateUp();
  }

  @Override
  public void onBackPressed() {
    // validate the current task and perform navigation on `ok`
    taskEditFragment.navigateBack();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ View handlers                                                            ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void handleAction(TaskListViewModel item) {
    // the saved previous task
    Task prev = null;

    // add a new task
    if (item.getAction().equals(Action.ADD))
      handleAdd(item.getTask());
      // modify the task
    else {
      for (int i = 0; i < tasks.size(); ++i) {
        Task curr = tasks.get(i);
        if (curr.getId() == item.getId()) {
          // set the previous task for undo
          prev = curr;
          // set the proper id before update
          item.getTask().setId(item.getId());
          // update the task
          handleUpdate(i, item.getTask());
        }
      }
    }

    // notify the adapter
    taskRecyclerViewAdapterViewModel
      .setTasks(tasks)
      .notifyChange();

    // create the snack bar
    Snackbar snackbar = Snackbar.make(
      findViewById(R.id.coordinatorLayout),
      Action.message(item.getAction()),
      Snackbar.LENGTH_SHORT
    );

    Task finalPrev = prev;
    // set event for `undo` button
    snackbar.setAction("undo", _view -> {
      // remove the added task
      if (item.getAction().equals(Action.ADD))
        handleRemove(item.getTask());
        // change back modified task
      else {
        for (int i = 0; i < tasks.size(); ++i)
          if (tasks.get(i).getId() == item.getTask().getId())
            handleUpdate(i, finalPrev);
      }

      // notify the adapter
      taskRecyclerViewAdapterViewModel
        .setTasks(tasks)
        .notifyChange();
    });

    // show the snack bar
    snackbar.show();
  }

  private void handleAdd(Task task) {
    try {
      tasks.add(task);
      taskDBHandler.getTaskTable().create(task);
      Alarm.set(this, task);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  private void handleRemove(Task task) {
    try {
      tasks.remove(task);
      taskDBHandler.getTaskTable().delete(task);
      Alarm.cancel(this, task);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  private void handleUpdate(int index, Task task) {
    try {
      tasks.set(index, task);
      taskDBHandler.getTaskTable().update(task);
      Alarm.update(this, task);
    } catch (DatabaseException e) {
      e.printStackTrace();
    }
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Utilities                                                                ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void createChannel() {
    String name = "Tasks";
    String description = "Notifications concerning Tasks that are overdue";

    int importance = NotificationManager.IMPORTANCE_DEFAULT;
    NotificationChannel channel = new NotificationChannel(TASKS_NOTIFICATION_CHANNEL, name, importance);
    channel.setDescription(description);

    // Register the channel with the system; you can't change the importance
    // or other notification behaviors after this
    NotificationManager notificationManager = getSystemService(NotificationManager.class);
    notificationManager.createNotificationChannel(channel);
  }
}
