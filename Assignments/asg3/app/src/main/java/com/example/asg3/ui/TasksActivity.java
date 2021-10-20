package com.example.asg3.ui;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.asg3.R;
import com.example.asg3.databinding.ActivityTasksBinding;
import com.example.asg3.ui.editor.TaskEditFragment;
import com.example.asg3.ui.list.TaskListFragment;
import com.example.asg3.viewmodel.TaskEditViewModel;
import com.example.asg3.viewmodel.TaskListViewModel;

public class TasksActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private ActivityTasksBinding binding;

  // fragments
  private TaskEditFragment taskEditFragment;
  private TaskListFragment taskListFragment;

  // view models
  private TaskEditViewModel taskEditViewModel;
  private TaskListViewModel taskListViewModel;

  public TasksActivity() {
    taskEditViewModel = new TaskEditViewModel();
    taskListViewModel = new TaskListViewModel();
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

  public TasksActivity setTaskEditFragment(TaskEditFragment taskEditFragment) {
    this.taskEditFragment = taskEditFragment;
    return this;
  }

  public TasksActivity setTaskListFragment(TaskListFragment taskListFragment) {
    this.taskListFragment = taskListFragment;
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
}
