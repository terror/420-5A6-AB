package com.example.asg2.ui.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.asg2.databinding.BottomSheetTaskListBinding;
import com.example.asg2.model.Priority;
import com.example.asg2.model.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.HashMap;

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
    // it's already at the highest priority
    if (task.getPriority() == Priority.HIGH)
      return;

    // account for the `null` case
    if (task.getPriority() == null)
      task.setPriority(Priority.LOW);

    // map priority -> priority + 1
    HashMap<Priority, Priority> levels = new HashMap() {{
      put(Priority.MEDIUM, Priority.HIGH);
      put(Priority.LOW, Priority.MEDIUM);
      put(Priority.NONE, Priority.LOW);
    }};

    // set the appropriate priority
    task.setPriority(levels.get(task.getPriority()));

    // dismiss the dialog
    dismiss();
  }

  private void trashTask() {
    task.setTrash(true);
    dismiss();
  }
}
