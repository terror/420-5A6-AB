package com.example.asg1.ui.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.asg1.databinding.FragmentTaskEditBinding;
import com.example.asg1.model.Task;
import com.example.asg1.ui.handlers.DatePickerDialogOnSetDateHandler;
import com.example.asg1.ui.util.DatePickerDialogFragment;

public class TaskEditFragment extends Fragment {
  private FragmentTaskEditBinding binding;
  private Task task;

  @Override
  public View onCreateView(
    LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState
  ) {
    binding = FragmentTaskEditBinding.inflate(inflater, container, false);

    // *** Event Listeners ***
    binding.dateImageButton.setOnClickListener(view -> {
        binding.dateImageButton.setEnabled(false);
        DatePickerDialogFragment.create(new DatePickerDialogOnSetDateHandler( this))
          .show(getParentFragmentManager(), "datePicker");
      }
    );

    binding.priorityImageButton.setOnClickListener(view -> {
        binding.priorityImageButton.setEnabled(false);
        binding.priorityToolbarLayout.setVisibility(View.VISIBLE);
      }
    );

    binding.dateToolbarClose.setOnClickListener(view -> {
      binding.dateImageButton.setEnabled(true);
      binding.dateToolbarTextView.setText("");
        binding.dateToolbarLayout.setVisibility(View.GONE);
      }
    );

    binding.priorityCloseImageButton.setOnClickListener(view -> {
        binding.priorityImageButton.setEnabled(true);
        binding.priorityToolbarRadioGroup.clearCheck();
        binding.priorityToolbarLayout.setVisibility(View.GONE);
      }
    );

    // Create our empty new task
    task = new Task();

    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public FragmentTaskEditBinding getBinding() {
    return binding;
  }
}
