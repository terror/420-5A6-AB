package ca.qc.johnabbott.cs5a6.tasksstartera3;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import ca.qc.johnabbott.cs5a6.tasksstartera3.databinding.FragmentTaskListBinding;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Action;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Task;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.TaskData;

public class TaskListFragment extends Fragment {
  private FragmentTaskListBinding binding;
  private TaskListAdapter adapter;
  private TasksActivity tasksActivity;

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);
    tasksActivity = (TasksActivity) context;
    tasksActivity.setTaskListFragment(this);
  }

  @Override
  public View onCreateView(
    LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState
  ) {
    binding = FragmentTaskListBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    adapter = new TaskListAdapter(this, TaskData.getData());

    // setup recycler view bindings
    binding.tasksRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    binding.tasksRecyclerView.setAdapter(adapter);

    // set event listeners on the view model
    tasksActivity.getTaskListViewModel().addOnUpdateListener(this, item -> adapter.handleAction(item));
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public void handleAdd() {
    // set properties on the view model
    tasksActivity
      .getTaskEditViewModel()
      .setAction(Action.ADD)
      .setTask(null)
      .notifyChange();

    // navigate to `edit` fragment
    navigateToEdit();
  }

  public void handleEdit(Task task) {
    // set properties on the view model
    tasksActivity
      .getTaskEditViewModel()
      .setAction(Action.EDIT)
      .setTask(task)
      .notifyChange();

    // navigate to `edit` fragment
    navigateToEdit();
  }

  public void navigateToEdit() {
    // hide the `add` button
    tasksActivity.getBinding().fab.setVisibility(View.GONE);

    // navigate to fragment
    Navigation
      .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
      .navigate(R.id.navigateToTaskEditFragment);
  }
}
