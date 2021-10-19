package ca.qc.johnabbott.cs5a6.tasksstartera3;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import ca.qc.johnabbott.cs5a6.tasksstartera3.databinding.ListItemTaskBinding;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Action;
import ca.qc.johnabbott.cs5a6.tasksstartera3.model.Task;
import ca.qc.johnabbott.cs5a6.tasksstartera3.viewmodel.TaskListViewModel;

import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder> {
  private List<Task> data;
  private TaskListFragment taskListFragment;

  public TaskListAdapter(TaskListFragment taskListFragment, List<Task> data) {
    this.taskListFragment = taskListFragment;
    this.data = data;
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new ViewHolder(ListItemTaskBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
    holder.bind(data.get(position));
  }

  @Override
  public int getItemCount() {
    return data.size();
  }

  public void handleAction(TaskListViewModel item) {
    Task prev = null;

    if (item.getAction().equals(Action.ADD)) {
      data.add(item.getTask());
    } else {
      for(int i = 0; i < data.size(); ++i) {
        if (data.get(i).getId() == item.getId()) {
          prev = data.get(i);
          data.set(i, item.getTask());
        }
      }
    }

    notifyDataSetChanged();

    Snackbar snackbar = Snackbar.make(
      taskListFragment.getActivity().findViewById(R.id.coordinatorLayout),
      Action.message(item.getAction()),
      Snackbar.LENGTH_SHORT
    );

    final Task finalPrev = prev;
    snackbar.setAction("undo", view -> {
      if (item.getAction().equals(Action.ADD)) {
        data.remove(item.getTask());
      } else {
        for(int i = 0; i < data.size(); ++i) {
          if (data.get(i).getId() == item.getTask().getId())
            data.set(i, finalPrev);
        }
      }
      notifyDataSetChanged();
    });

    snackbar.show();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {
    private final ListItemTaskBinding binding;
    private Task task;

    public ViewHolder(ListItemTaskBinding binding) {
      super(binding.getRoot());
      this.binding = binding;

      // set event listeners
      binding.itemDescriptionTextView.setOnClickListener(_view -> Toast.makeText(taskListFragment.getContext(), "onClick", Toast.LENGTH_SHORT).show());
      binding.itemDescriptionTextView.setOnLongClickListener(_view -> editTaskInFragment());
    }

    public void bind(Task task) {
      this.task = task;
      binding.itemDescriptionTextView.setText(task.getDescription());
    }

    public boolean editTaskInFragment() {
      taskListFragment.handleEdit(task);
      return true;
    }
  }
}
