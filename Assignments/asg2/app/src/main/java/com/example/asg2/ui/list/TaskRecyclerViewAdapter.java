package com.example.asg2.ui.list;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg2.databinding.ListItemTaskBinding;
import com.example.asg2.model.Priority;
import com.example.asg2.model.Status;
import com.example.asg2.model.Task;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
  private final List<Task> tasks;

  public TaskRecyclerViewAdapter(List<Task> items) {
    tasks = items;
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
  public int getItemCount() {
    return tasks.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ListItemTaskBinding binding;
    private Task task;

    private HashMap<Priority, Integer> colorMap = new HashMap() {{
      put(Priority.HIGH, Color.RED);
      put(Priority.MEDIUM, Color.rgb(255, 165, 0));
      put(Priority.LOW, Color.YELLOW);
      put(Priority.NONE, Color.WHITE);
    }};

    public ViewHolder(ListItemTaskBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Task task) {
      this.task = task;

      // set the on click listener for `edit`
      binding.taskItemLayout.setOnClickListener(view -> editTask(view));

      // set the on checked listener for `complete`
      // if the task is not completed
      if (task.getStatus() == Status.COMPLETED)
        binding.taskItemCheckBox.setEnabled(false);
      else
        binding.taskItemCheckBox.setOnCheckedChangeListener((button, isChecked) -> completeTask(isChecked));

      // set the task item description
      binding.description.setText(task.getDescription());

      // set the task item date
      if (task.getDue() != null)
        binding.date.setText(task.getDue().toString());

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
      sheet.setOnDismissListener(_view -> sort());

      // show the bottom sheet dialog
      sheet.show();
    }

    public void completeTask(boolean isChecked) {
      if (isChecked) {
        task.setStatus(Status.COMPLETED);
        sort();
      }
    }

    public void sort() {
      // if this task was deleted, remove it
      // from `tasks`
      if (task.getTrash())
        tasks.remove(task);

      // sort `tasks` in reverse order
      Collections.sort(tasks, Collections.reverseOrder());

      // notify the ui
      notifyDataSetChanged();
    }
  }
}
