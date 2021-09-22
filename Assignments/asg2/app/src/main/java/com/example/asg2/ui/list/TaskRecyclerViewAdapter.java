package com.example.asg2.ui.list;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.example.asg2.databinding.ListItemTaskBinding;
import com.example.asg2.model.Task;
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
    return new ViewHolder(ListItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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

    public ViewHolder(ListItemTaskBinding binding) {
      super(binding.getRoot());
      this.binding = binding;
    }

    public void bind(Task task) {
      mItem = task;
      binding.itemNumber.setText(Integer.toString(task.getId()));
      binding.content.setText(task.getDescription());
    }
  }
}
