package com.example.asg4.ui.list;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg4.R;
import com.example.asg4.databinding.FragmentTaskListBinding;
import com.example.asg4.model.Action;
import com.example.asg4.model.Task;
import com.example.asg4.ui.TasksActivity;

/**
 * A fragment representing a list of Items.
 */
public class TaskListFragment extends Fragment {
  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private RecyclerView recyclerView;
  private FragmentTaskListBinding binding;
  private TasksActivity tasksActivity;
  private TaskRecyclerViewAdapter taskRecyclerViewAdapter;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TaskListFragment() {}

  public FragmentTaskListBinding getBinding() {
    return binding;
  }

  // TODO: Customize parameter initialization
  @SuppressWarnings("unused")
  public static TaskListFragment newInstance(int columnCount) {
    TaskListFragment fragment = new TaskListFragment();
    Bundle args = new Bundle();
    args.putInt(ARG_COLUMN_COUNT, columnCount);
    fragment.setArguments(args);
    return fragment;
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Overridden methods                                                       ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  @Override
  public void onAttach(@NonNull Context context) {
    super.onAttach(context);

    // set the activity
    tasksActivity = (TasksActivity) context;

    // set the fragment on the activity
    tasksActivity.setTaskListFragment(this);

    // set event listeners on the view model
    tasksActivity.getTaskListViewModel().addOnUpdateListener(this, item -> tasksActivity.handleAction(item));
    tasksActivity.getTaskRecyclerViewAdapterViewModel().addOnUpdateListener(this, item -> taskRecyclerViewAdapter.setTasks(item.getTasks()).update(null));
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // check for args
    if (getArguments() != null)
      mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTaskListBinding.inflate(inflater, container, false);

    // Grab the recycler view from layout
    recyclerView = binding.getRoot().findViewById(R.id.list);

    // Account for `mColumnCount`
    if (mColumnCount <= 1)
      recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    else recyclerView.setLayoutManager(new GridLayoutManager(getContext(), mColumnCount));

    // Set the adapter
    taskRecyclerViewAdapter = new TaskRecyclerViewAdapter(this, tasksActivity.getTasks());
    recyclerView.setAdapter(taskRecyclerViewAdapter);

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // set event listeners on the ui
    binding.listToolbarSwitch.setOnCheckedChangeListener((button, checked) -> setGrid(checked));
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ View handlers                                                            ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void setGrid(boolean isChecked) {
    if (isChecked)
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    else recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  public void handleAdd() {
    // set properties on the task edit
    // view model and notify listeners
    tasksActivity.getTaskEditViewModel()
      .setAction(Action.ADD)
      .setTask(null);

    // navigate to `edit` fragment
    navigateToEdit();
  }

  public void handleEdit(Task task) {
    // set properties on the task edit
    // view model and notify listeners
    tasksActivity.getTaskEditViewModel()
      .setAction(Action.EDIT)
      .setTask(task);

    // navigate to `edit` fragment
    navigateToEdit();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Navigation                                                               ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private void navigateToEdit() {
    // hide `add` button
    tasksActivity.getBinding().fab.setVisibility(View.GONE);

    // navigate to `edit` fragment
    Navigation
      .findNavController(getActivity(), R.id.nav_host_fragment_content_main)
      .navigate(R.id.navigateToTaskEditFragment);
  }
}
