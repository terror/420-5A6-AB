package com.example.asg4.ui;

import android.os.Bundle;
import android.view.MenuItem;
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
import com.example.asg4.sqlite.DatabaseException;
import com.example.asg4.ui.editor.TaskEditFragment;
import com.example.asg4.ui.list.TaskListFragment;
import com.example.asg4.viewmodel.TaskEditViewModel;
import com.example.asg4.viewmodel.TaskListViewModel;
import com.example.asg4.viewmodel.TaskRecyclerViewAdapterViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class TasksActivity extends AppCompatActivity {
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

  // saved task state
  public List<Task> tasks;

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

    binding = ActivityTasksBinding.inflate(getLayoutInflater());

    // event listeners
    binding.fab.setOnClickListener(_view -> taskListFragment.handleAdd());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
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

    // whether or not to update the UI and display the snack bar
    boolean updated = true;

    // add a new task
    if (item.getAction().equals(Action.ADD)) {
      try {
        tasks.add(item.getTask());
        taskDBHandler.getTaskTable().create(item.getTask());
      } catch (DatabaseException e) {
        e.printStackTrace();
      }
    // modify the task
    } else {
      for(int i = 0; i < tasks.size(); ++i) {
        Task curr = tasks.get(i);
        if (curr.getId() == item.getId()) {
          // if the current tasks state is equal to
          // to the supposedly modified tasks state,
          // do not update
          if (curr.equals(item.getTask())) {
            updated = false;
            break;
          }

          // set the previous task for undo
          prev = curr;

          // set the proper id before update
          item.getTask().setId(item.getId());
          try {
            tasks.set(i, item.getTask());
            taskDBHandler.getTaskTable().update(item.getTask());
          } catch (DatabaseException e) {
            e.printStackTrace();
          }
        }
      }
    }

    if (updated) {
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
        if (item.getAction().equals(Action.ADD)) {
          try {
            tasks.remove(item.getTask());
            taskDBHandler.getTaskTable().delete(item.getTask());
          } catch (DatabaseException e) {
            e.printStackTrace();
          }
        // change back modified task
        } else {
          for (int i = 0; i < tasks.size(); ++i) {
            if (tasks.get(i).getId() == item.getTask().getId()) {
              try {
                tasks.set(i, finalPrev);
                taskDBHandler.getTaskTable().update(finalPrev);
              } catch (DatabaseException e) {
                e.printStackTrace();
              }
            }
          }
        }

        // notify the adapter
        taskRecyclerViewAdapterViewModel
          .setTasks(tasks)
          .notifyChange();
      });

      // show the snack bar
      snackbar.show();
    }
  }
}
