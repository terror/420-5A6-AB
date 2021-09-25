package com.example.asg2.ui.list;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.example.asg2.databinding.BottomSheetTaskListBinding;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class TaskListBottomSheet extends BottomSheetDialog {
  private BottomSheetTaskListBinding binding;

  public TaskListBottomSheet(@NonNull Context context) {
    super(context);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = BottomSheetTaskListBinding.inflate(getLayoutInflater());
    setContentView(binding.getRoot());
  }
}
