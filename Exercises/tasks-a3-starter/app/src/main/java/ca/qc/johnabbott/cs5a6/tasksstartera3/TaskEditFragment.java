package ca.qc.johnabbott.cs5a6.tasksstartera3;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import ca.qc.johnabbott.cs5a6.tasksstartera3.databinding.FragmentTaskEditBinding;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Action;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Task;
import ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel.TaskEditViewModel;
import java.util.Date;

public class TaskEditFragment extends Fragment {
  private FragmentTaskEditBinding binding;
  private TasksActivity tasksActivity;

  // view model fields
  private Task task;
  private Action action;
  private int id;

  public TaskEditFragment() {
    // start with a blank task
    setTask(new Task());
  }

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    tasksActivity = (TasksActivity) context;
    tasksActivity.setTaskEditFragment(this);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTaskEditBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // set event listeners on ui elements
    binding.descriptionEditText.addTextChangedListener(editDescription());
    // handle action item
    handleAction(tasksActivity.getTaskEditViewModel());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public Task getTask() {
    return task;
  }

  public void setTask(Task task) {
    this.task = task;
  }

  public TextWatcher editDescription() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int i, int i1, int i2) {}

      @Override
      public void onTextChanged(CharSequence s, int i, int i1, int i2) {
        task.setDescription(s.toString());
        task.setModified(new Date());
      }

      @Override
      public void afterTextChanged(Editable s) {}
    };
  }

  public void handleAction(TaskEditViewModel item) {
    Log.d("Handling action in edit: ", item.toString());

    // set the current action
    action = item.getAction();

    // grab the task to modify or add
    Task task = item.getTask();

    // if we're editing an item, set it's fields on the UI
    if (item.getAction().equals(Action.EDIT)) {
      // set the current task id
      id = task.getId();
      // set the description on the ui
      binding.descriptionEditText.setText(task.getDescription());
    }
  }

  public void navigateBack() {
    // set the view model
    tasksActivity.getTaskListViewModel()
      .setTask(task)
      .setAction(action)
      .setId(id)
      .notifyChange();

    // show the `add` button
    tasksActivity.getBinding().fab.setVisibility(View.VISIBLE);

    // pop off the fragment stack
    Navigation
      .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
      .popBackStack();
  }
}
