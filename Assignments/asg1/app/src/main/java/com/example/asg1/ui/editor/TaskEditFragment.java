package com.example.asg1.ui.editor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.asg1.databinding.FragmentTaskEditBinding;
import com.example.asg1.ui.handlers.DatePickerDialogOnSetDateHandler;
import com.example.asg1.ui.util.DatePickerDialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TaskEditFragment extends Fragment {
  private FragmentTaskEditBinding binding;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTaskEditBinding.inflate(inflater, container, false);
    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    // Set all event listeners
    binding.dateImageButton.setOnClickListener(_view -> chooseDate());
    binding.priorityImageButton.setOnClickListener(_view -> choosePriority());
    binding.dateToolbarClose.setOnClickListener(_view -> closeDate());
    binding.dateToolbar.setOnClickListener(_view -> editDate());
    binding.priorityCloseImageButton.setOnClickListener(_view -> closePriority());
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  public void chooseDate() {
    // Disable the date image button
    binding.dateImageButton.setEnabled(false);

    // Display the DatePickerDialog
    DatePickerDialogFragment.create(new DatePickerDialogOnSetDateHandler( this))
      .show(getParentFragmentManager(), "datePicker");
  }

  public void editDate() {
    // Get the current date
    Calendar curr = Calendar.getInstance();
    curr.setTime(getDateSelection());

    // Display the DatePickerDialog with the currently selected date
    DatePickerDialogFragment.create(
      curr.getTime(),
      new DatePickerDialogOnSetDateHandler(
        this,
        curr.get(Calendar.HOUR),
        curr.get(Calendar.MINUTE)
      )
    ).show(getParentFragmentManager(), "datePicker");
  }

  public void displayDate(Calendar calendar) {
    // Set the date toolbar's text to the calendars date string
    binding.dateToolbarTextView.setText(calendar.getTime().toString());

    // Make the date toolbar visible
    binding.dateToolbarLayout.setVisibility(View.VISIBLE);
  }

  public void choosePriority() {
    // Disable the priority image button
    binding.priorityImageButton.setEnabled(false);

    // Make the priority toolbar visible
    binding.priorityToolbarLayout.setVisibility(View.VISIBLE);
  }

  public void closeDate() {
    // Enable the date image button
    binding.dateImageButton.setEnabled(true);

    // Clear the date toolbar's text view
    binding.dateToolbarTextView.setText("");

    // Hide the date toolbar
    binding.dateToolbarLayout.setVisibility(View.GONE);
  }

  public void closePriority() {
    // Enable the priority image button
    binding.priorityImageButton.setEnabled(true);

    // Clear the priority radio group's check
    binding.priorityToolbarRadioGroup.clearCheck();

    // Hide the priority toolbar
    binding.priorityToolbarLayout.setVisibility(View.GONE);
  }

  public Date getDateSelection() {
    // Grab the current date selection as a string
    String date = binding.dateToolbarTextView.getText().toString();

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    // Parse the date string
    try {
      calendar.setTime(formatter.parse(date));
    } catch(ParseException e) {
      calendar.setTime(new Date());
    }

    return calendar.getTime();
  }
}
