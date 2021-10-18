package com.example.asg3.ui.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.asg3.databinding.BottomSheetTaskListBinding;
import com.example.asg3.model.Priority;
import com.example.asg3.model.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class TaskListBottomSheet extends BottomSheetDialog {
  private BottomSheetTaskListBinding binding;
  private Task task;

  public TaskListBottomSheet(@NonNull Context context, Task task) {
    super(context);
    this.task = task;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = BottomSheetTaskListBinding.inflate(getLayoutInflater());

    // Set event listeners
    binding.bottomSheetPrioritizeButton.setOnClickListener(view -> changePriority());
    binding.bottomSheetTrashButton.setOnClickListener(view -> trashTask());

    setContentView(binding.getRoot());
  }

  private void changePriority() {
    // don't set the priority for completed tasks
    if (task.isCompleted()) {
      dismiss();
      return;
    }

    // it's already at the highest priority
    if (task.getPriority() == Priority.HIGH) {
      dismiss();
      return;
    }

    // account for the `null` case
    if (task.getPriority() == null) {
      task.setPriority(Priority.LOW);
      dismiss();
      return;
    }

    // set the appropriate priority
    task.setPriority(Priority.from(task.getPriority().ordinal() + 1));

    // dismiss the dialog
    dismiss();
  }

  private void trashTask() {
    task.setTrash(true);
    dismiss();
  }
}
