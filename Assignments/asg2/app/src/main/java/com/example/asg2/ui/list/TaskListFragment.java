package com.example.asg2.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg2.R;
import com.example.asg2.databinding.FragmentTaskListBinding;
import com.example.asg2.model.Task;
import com.example.asg2.model.TaskData;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class TaskListFragment extends Fragment {
  private static final String ARG_COLUMN_COUNT = "column-count";
  private int mColumnCount = 1;
  private RecyclerView recyclerView;
  private FragmentTaskListBinding binding;

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public TaskListFragment() {
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
    List<Task> tasks = TaskData.getData();
    Collections.sort(tasks, Collections.reverseOrder());
    recyclerView.setAdapter(new TaskRecyclerViewAdapter(tasks, binding));

    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // set event listeners
    binding.listToolbarSwitch.setOnCheckedChangeListener((button, checked) -> setGrid(checked));
  }

  public void setGrid(boolean isChecked) {
    if (isChecked)
      recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
    else recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }
}
