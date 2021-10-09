package com.example.asg2.ui.list;

import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg2.databinding.FragmentTaskListBinding;
import com.example.asg2.databinding.ListItemTaskBinding;
import com.example.asg2.model.Priority;
import com.example.asg2.model.Status;
import com.example.asg2.model.Task;
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

  // generated binding
  private FragmentTaskListBinding fragmentBinding;

  public TaskRecyclerViewAdapter(List<Task> tasks, FragmentTaskListBinding fragmentBinding) {
    this.fragmentBinding = fragmentBinding;

    // set event listeners on the fragment
    fragmentBinding.listFilterEditText.addTextChangedListener(filter());

    // create a new list based on `tasks` as the original list
    this.originalTasks = tasks;

    // set the filterable list
    this.tasks = tasks;

    // indicates that each item can be represented with a unique id
    setHasStableIds(true);
  }

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

  public void sort(Task task) {
    // if this task was deleted, remove it from `tasks`
    if (task.getTrash())
      tasks.remove(task);

    // sort `tasks` in reverse order
    Collections.sort(tasks, Collections.reverseOrder());

    // notify the ui
    notifyDataSetChanged();
  }

  public TextWatcher filter() {
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

        // notify the ui
        notifyDataSetChanged();
      }

      @Override
      public void afterTextChanged(Editable editable) {}
    };
  }

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
      binding.taskItemLayout.setOnClickListener(view -> editTask(view));
    }

    public void bind(Task task) {
      this.task = task;

      // set the on checked listener for `complete`
      // if the task is not completed
      if (task.isCompleted())
        binding.taskItemCheckBox.setChecked(true);
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
        task.getStatus() == Status.COMPLETED ?
          Color.GRAY :
          colorMap.get(task.getPriority())
      );
    }

    public void editTask(View view) {
      TaskListBottomSheet sheet = new TaskListBottomSheet(view.getContext(), task);

      // sort and refresh the UI upon dismiss
      sheet.setOnDismissListener(_view -> sort(task));

      // show the bottom sheet dialog
      sheet.show();
    }

    public void completeTask(boolean isChecked) {
      // if the checkbox is not checked, set the status to pending
      if (!isChecked) {
        task.setStatus(Status.PENDING);
      } else {
        // set the task's status to completed
        task.setStatus(Status.COMPLETED);
      }

      // resort the tasks list
      sort(task);
    }
  }
}
