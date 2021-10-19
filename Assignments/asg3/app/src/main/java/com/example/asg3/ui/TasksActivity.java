package com.example.asg3.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.asg3.R;
import com.example.asg3.databinding.ActivityTasksBinding;
import com.example.asg3.model.Action;
import com.example.asg3.model.Task;
import com.example.asg3.ui.editor.TaskEditFragment;
import com.example.asg3.ui.list.TaskListFragment;
import com.example.asg3.viewmodel.TaskViewModel;

public class TasksActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private ActivityTasksBinding binding;
  private TaskEditFragment taskEditFragment;
  private TaskViewModel taskViewModel;
  private TaskListFragment taskListFragment;

  public TasksActivity() {
    taskViewModel = new TaskViewModel();
  }

  public TaskViewModel getTaskViewModel() {
    return taskViewModel;
  }

  public TasksActivity setTaskEditFragment(TaskEditFragment taskEditFragment) {
    this.taskEditFragment = taskEditFragment;
    return this;
  }

  public TasksActivity setTaskListFragment(TaskListFragment taskListFragment) {
    this.taskListFragment = taskListFragment;
    return this;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityTasksBinding.inflate(getLayoutInflater());

    // event listeners
    binding.fab.setOnClickListener(_view -> handleAdd());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
      navigateBack();
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
    navigateBack();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Navigation                                                               ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void navigateBack() {
    // validate the current task and perform navigation on `ok`
    if (validateCurrentTask(((dialogInterface, i) -> navigateToList())))
      handleBackPressed();
  }

  public void navigateToList() {
    // show the `add` button
    binding.fab.setVisibility(View.VISIBLE);
    // grab the nav controller
    NavController controller = Navigation.findNavController(TasksActivity.this, R.id.nav_host_fragment_content_main);
    // navigate to `list` fragment
    controller.navigate(R.id.navigateToTaskListFragment);
  }

  public void navigateToEdit() {
    // hide `add` button
    binding.fab.setVisibility(View.GONE);
    // grab the nav controller
    NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    // navigate to `edit` fragment
    controller.navigate(R.id.navigateToTaskEditFragment);
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Handlers                                                                 ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void handleBackPressed() {
    // navigate to the `list` fragment
    navigateToList();

    // handle the action in the recycler view adapter
    taskListFragment
      .getAdapter()
      .handleAction(
        taskEditFragment.getCurrentAction(),
        taskEditFragment.getCurrentTask(),
        taskEditFragment.getCurrentId()
      );
  }

  public void handleAdd() {
    // set properties on the view model
    getTaskViewModel().setAction(Action.ADD).setTask(null);
    // navigate to `edit` fragment
    navigateToEdit();
  }

  public void handleEdit(Task task) {
    // set properties on the view model
    getTaskViewModel().setAction(Action.EDIT).setTask(task);
    // navigate to `edit` fragment
    navigateToEdit();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Validation                                                               ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private boolean validateCurrentTask(DialogInterface.OnClickListener listener) {
    // get the current task
    Task currentTask = taskEditFragment.getCurrentTask();

    // validate the description in either case, if the task is null
    // or does not contain a description, display an alert dialog
    if (currentTask == null || currentTask.getDescription() == null || currentTask.getDescription() == "") {
      new AlertDialog.Builder(this)
        .setTitle("Empty description")
        .setMessage("Description is empty, no task will be added or modified.")
        .setNegativeButton(android.R.string.ok, listener)
        .show();

      // the current task isn't valid
      return false;
    }

    // the current task is valid
    return true;
  }
}
