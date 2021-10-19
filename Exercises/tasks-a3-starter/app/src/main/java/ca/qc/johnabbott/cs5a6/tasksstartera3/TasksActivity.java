package ca.qc.johnabbott.cs5a6.tasksstartera3;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import ca.qc.johnabbott.cs5a6.tasksstartera3.databinding.ActivityTasksBinding;
import ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel.TaskEditViewModel;
import ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel.TaskListViewModel;

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

  public ActivityTasksBinding getBinding() {
    return binding;
  }

  public TaskEditViewModel getTaskEditViewModel() {
    return taskEditViewModel;
  }

  public TaskListViewModel getTaskListViewModel() {
    return taskListViewModel;
  }

  public TasksActivity setTaskListFragment(TaskListFragment taskListFragment) {
    this.taskListFragment = taskListFragment;
    return this;
  }

  public TasksActivity setTaskEditFragment(TaskEditFragment taskEditFragment) {
    this.taskEditFragment = taskEditFragment;
    return this;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityTasksBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

    // event listeners
    binding.fab.setOnClickListener(_view -> taskListFragment.handleAdd());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId() == android.R.id.home) {
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
    taskEditFragment.navigateBack();
  }
}
