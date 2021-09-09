package com.example.asg1.ui.editor;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.example.asg1.R;
import com.example.asg1.databinding.ActivityTaskEditBinding;
import com.example.asg1.model.Priority;
import com.example.asg1.model.Task;

import java.util.Date;

public class TaskEditActivity extends AppCompatActivity {
  private AppBarConfiguration appBarConfiguration;
  private ActivityTaskEditBinding binding;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    binding = ActivityTaskEditBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());

    setSupportActionBar(binding.toolbar);

    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
    NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_task_edit, menu);

    // Grab the app bar's `CheckBox` by ID.
    CheckBox checkBox = (CheckBox)
      menu
        .findItem(R.id.app_bar_checkbox)
        .getActionView();

    // Set an onCheckedChange listener to the app bar's checkbox
    // displaying the debug window if it is checked.
    checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
      if (isChecked)
        showDebugWindow();
    });

    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onSupportNavigateUp() {
    NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
    return NavigationUI.navigateUp(navController, appBarConfiguration)
      || super.onSupportNavigateUp();
  }

  public void showDebugWindow() {
    // Grab our `TaskEditFragment` instance.
    TaskEditFragment taskEditFragment =
      (TaskEditFragment)getSupportFragmentManager()
      .findFragmentById(R.id.layoutTaskEdit);

    // Set fields on a new `Task` instance.
    Task task = new Task()
      .setDue(new Date())
      .setPriority(Priority.HIGH)
      .setDescription("Hello, World!");
      // .setDescription(taskEditFragment.getBinding().descriptionEditTextTextMultiLine.getText().toString());

    new AlertDialog.Builder(this)
      .setTitle("Debug")
      .setMessage(task.toString())
      .setNegativeButton(android.R.string.no, null)
      .setIcon(android.R.drawable.ic_dialog_alert)
      .show();
  }
}