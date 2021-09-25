package com.example.asg2.ui.list;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg2.databinding.ListItemTaskBinding;
import com.example.asg2.model.Priority;
import com.example.asg2.model.Status;
import com.example.asg2.model.Task;
import java.util.HashMap;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task}.
 * TODO: Replace the implementation with code for your data type.
 */
public class TaskRecyclerViewAdapter extends RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder> {
  private final List<Task> mValues;

  public TaskRecyclerViewAdapter(List<Task> items) {
    mValues = items;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return new ViewHolder(
      ListItemTaskBinding.inflate(
        LayoutInflater.from(parent.getContext()),
        parent,
        false
      )
    );
  }

  @Override
  public void onBindViewHolder(final ViewHolder holder, int position) {
    holder.bind(mValues.get(position));
  }

  @Override
  public int getItemCount() {
    return mValues.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private ListItemTaskBinding binding;
    private Task mItem;

    private HashMap<Priority, Integer> colorMap = new HashMap() {{
      put(Priority.HIGH,   Color.RED);
      put(Priority.MEDIUM, Color.rgb(255, 165, 0));
      put(Priority.LOW,    Color.YELLOW);
      put(Priority.NONE,   Color.WHITE);
    }};

    public ViewHolder(ListItemTaskBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Task task) {
      mItem = task;

      // set the task item description
      binding.description.setText(task.getDescription());

      // set the task item date
      if (task.getDue() != null)
        binding.date.setText(task.getDue().toString());

      // set the task item layout's color
      if (task.getStatus() == Status.COMPLETED)
        binding.taskItemLayout.setBackgroundColor(Color.GRAY);
      else
        binding.taskItemLayout.setBackgroundColor(colorMap.get(task.getPriority()));
    }
  }
}
