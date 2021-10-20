package com.example.asg3.ui.list;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg3.databinding.ListItemTaskBinding;
import com.example.asg3.model.Priority;
import com.example.asg3.model.Status;
import com.example.asg3.model.Task;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
  // original task list
  private final List<Task> originalTasks;
  // list that can be filtered through `filter`
  private List<Task> tasks;
  // task list fragment
  private TaskListFragment taskListFragment;

  public TaskRecyclerViewAdapter(TaskListFragment taskListFragment, List<Task> tasks) {
    // set the fragment
    this.taskListFragment = taskListFragment;

    // create a new list based on `tasks` as the original list
    this.originalTasks = tasks;

    // set the filterable list
    this.tasks = tasks;

    // set event listeners on the fragment
    taskListFragment
      .getBinding()
      .listFilterEditText
      .addTextChangedListener(filter());

    // indicates that each item can be represented with a unique id
    setHasStableIds(true);
  }

  public TaskRecyclerViewAdapter setTasks(List<Task> tasks) {
    this.tasks = tasks;
    return this;
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Overridden methods                                                       ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    ListItemTaskBinding binding = ListItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
    return new ViewHolder(binding);
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.bind(tasks.get(position));
  }

  @Override
  public int getItemViewType(int position) {
    return tasks.get(position).getId();
  }

  @Override
  public long getItemId(int position) {
    return tasks.get(position).getId();
  }

  @Override
  public int getItemCount() {
    return tasks.size();
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ View handlers                                                            ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public void update(Task task) {
    // if this task was deleted, remove it from `tasks`
    if (task != null && task.getTrash())
      tasks.remove(task);

    // sort `tasks` in reverse order
    Collections.sort(tasks, Collections.reverseOrder());

    // notify the ui
    notifyDataSetChanged();
  }

  private TextWatcher filter() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

      @Override
      public void onTextChanged(CharSequence query, int start, int before, int after) {
        // condition predicate
        Predicate<Task> containsQuery = task ->
          task
            .getDescription()
            .toLowerCase(Locale.ROOT)
            .contains(query.toString());

        // filter the list
        tasks = originalTasks
          .stream()
          .filter(containsQuery)
          .collect(Collectors.toList());

        // update the ui
        update(null);
      }

      @Override
      public void afterTextChanged(Editable editable) {}
    };
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ View holder                                                              ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ListItemTaskBinding binding;
    private Task task;

    private HashMap<Priority, Integer> colorMap = new HashMap() {{
      put(Priority.HIGH,   Color.rgb(229, 28, 88));
      put(Priority.MEDIUM, Color.rgb(255, 165, 0));
      put(Priority.LOW,    Color.rgb(255, 255, 0));
      put(Priority.NONE,   Color.rgb(220, 220, 220));
    }};

    public ViewHolder(ListItemTaskBinding binding) {
      super(binding.getRoot());

      this.binding = binding;

      // set the on click listener for `edit`
      binding.taskItemLayout.setOnClickListener(view -> editTaskBottomSheet(view));
    }

    public void bind(Task task) {
      this.task = task;

      // if the task is completed, set the checkbox to checked
      if (task.isCompleted())
        binding.taskItemCheckBox.setChecked(true);

      // set the on checked listener for `complete`
      binding.taskItemCheckBox.setOnCheckedChangeListener((button, isChecked) -> completeTask(isChecked));

      // set the task item description
      binding.description.setText(task.getDescription());

      // set the task item date
      if (task.getDue() != null)
        binding.date.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getDue()));

      // if the task is overdue, set the date color to red
      if (task.isOverdue() && !task.isCompleted())
        binding.date.setTextColor(Color.RED);

      // set the task item layout's color
      binding.taskItemLayout.setBackgroundColor(
        task.isCompleted() ?
          Color.GRAY :
          colorMap.get(task.getPriority())
      );

      // set on long click listener
      binding.taskItemLayout.setOnLongClickListener(_view -> editTaskFragment());
    }

    private void editTaskBottomSheet(View view) {
      // create a new `TaskListBottomSheet` instance with the current task
      TaskListBottomSheet sheet = new TaskListBottomSheet(view.getContext(), task);

      // sort and refresh the UI upon dismiss
      sheet.setOnDismissListener(_view -> update(task));

      // show the bottom sheet dialog
      sheet.show();
    }

    private boolean editTaskFragment() {
      taskListFragment.handleEdit(task);
      return true;
    }

    private void completeTask(boolean isChecked) {
      // if the checkbox is not checked, set the status to pending
      if (!isChecked) {
        task.setStatus(Status.PENDING);
      } else {
        // set the task's status to completed
        task.setStatus(Status.COMPLETED);
      }

      // resort the tasks list
      update(null);
    }
  }
}
