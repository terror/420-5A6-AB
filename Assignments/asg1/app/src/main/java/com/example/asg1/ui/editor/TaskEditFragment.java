package com.example.asg1.ui.editor;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.asg1.R;
import com.example.asg1.databinding.FragmentTaskEditBinding;
import com.example.asg1.model.Priority;
import com.example.asg1.model.Task;
import com.example.asg1.ui.handlers.DatePickerDialogOnSetDateHandler;
import com.example.asg1.ui.util.DatePickerDialogFragment;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Objects;
import java.util.Stack;

public class TaskEditFragment extends Fragment {
  // Generated fragment binding
  private FragmentTaskEditBinding binding;

  // History stack
  private Stack<Task> history;

  // Map radio button ID's -> priority variants
  private HashMap<Integer, Priority> PRIORITY_RADIO_BUTTON_ID_MAP = new HashMap() {{
    put(R.id.priority_high_radioButton,   Priority.HIGH);
    put(R.id.priority_medium_radioButton, Priority.MEDIUM);
    put(R.id.priority_low_radioButton,    Priority.LOW);
  }};

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    binding = FragmentTaskEditBinding.inflate(inflater, container, false);

    // Initialize the task change history stack
    history = new Stack();

    return binding.getRoot();
  }

  public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Set all event listeners
    binding.dateImageButton.setOnClickListener(_view -> chooseDate());
    binding.dateToolbar.setOnClickListener(_view -> editDate());
    binding.dateToolbarClose.setOnClickListener(_view -> closeDate());
    binding.descriptionEditTextTextMultiLine.addTextChangedListener(onTextChanged());
    binding.priorityCloseImageButton.setOnClickListener(_view -> closePriority());
    binding.priorityImageButton.setOnClickListener(_view -> choosePriority());
    binding.undoImageButton.setOnClickListener(_view -> undo());

    // Set a custom on change event listener that adds to the fragment's history
    binding.priorityToolbarRadioGroup.setOnCheckedChangeListener((radioGroup, id) -> {
      RadioButton radioButton = radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
      if (radioButton != null && radioButton.isChecked()) {
        Log.d("[setOnCheckedChangeListener]", PRIORITY_RADIO_BUTTON_ID_MAP.get(radioButton.getId()).toString());
        addTask(getCurrentTask().setPriority(PRIORITY_RADIO_BUTTON_ID_MAP.get(radioButton.getId())));
      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    binding = null;
  }

  //region Date

  private void chooseDate() {
    // Disable the date image button
    binding.dateImageButton.setEnabled(false);

    // Display the DatePickerDialog
    DatePickerDialogFragment.create(new DatePickerDialogOnSetDateHandler( this))
      .show(getParentFragmentManager(), "datePicker");
  }

  private void editDate() {
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

    // Add to the history
    addTask(getCurrentTask().setDue(calendar.getTime()));
  }

  public void hideDate() {
    // Enable the date image button
    binding.dateImageButton.setEnabled(true);

    // Clear the date toolbar's text view
    binding.dateToolbarTextView.setText("");

    // Hide the date toolbar
    binding.dateToolbarLayout.setVisibility(View.GONE);
  }

  private void closeDate() {
    // Hide the date toolbar
    hideDate();

    // Add to the history
    addTask(getCurrentTask().setDue(null));
  }

  //endregion

  //region Priority

  private void choosePriority() {
    // Disable the priority image button
    binding.priorityImageButton.setEnabled(false);

    // Make the priority toolbar visible
    binding.priorityToolbarLayout.setVisibility(View.VISIBLE);

    // Set the `High` priority as default
    binding.priorityToolbarRadioGroup.check(R.id.priority_high_radioButton);
  }

  private void hidePriority() {
    // Enable the priority image button
    binding.priorityImageButton.setEnabled(true);

    // Clear the priority radio group's check
    binding.priorityToolbarRadioGroup.clearCheck();

    // Hide the priority toolbar
    binding.priorityToolbarLayout.setVisibility(View.GONE);
  }

  private void closePriority() {
    // Hide the priority toolbar
    hidePriority();

    // Add to the history
    addTask(getCurrentTask().setPriority(null));
  }

  //endregion

  //region Description

  private TextWatcher onTextChanged() {
    return new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        addTask(getCurrentTask().setDescription(s.toString()));
      }

      @Override
      public void afterTextChanged(Editable editable) { }
    };
  }

  //endregion

  //region Undo

  private void undo() {
    Log.d("[undo]", history.toString());

    // If the history is empty do nothing.
    if (history.isEmpty())
      return;

    // Compare the current task with the previous and change
    // the corresponding UI component accordingly.
    Task current  = history.pop();
    Task previous = !history.isEmpty() ? history.pop() : new Task().setDescription("").setDue(null).setPriority(null);

    if (current.getDescription() != previous.getDescription())
      binding.descriptionEditTextTextMultiLine.setText(previous.getDescription());

    if (current.getDue() != previous.getDue()) {
      // If the current due date is null and the previous one
      // is set, make sure the date toolbar is shown.
      if (current.getDue() == null && previous.getDue() != null) {
        binding.dateToolbarLayout.setVisibility(View.VISIBLE);
        binding.dateToolbarTextView.setText(previous.getDue().toString());
      }
      // If the previous due date is null, hide the toolbar.
      else if (previous.getDue() == null)
        hideDate();
      // Set the date text to the previous one
      else
        binding.dateToolbarTextView.setText(previous.getDue().toString());
    }

    if (current.getPriority() != previous.getPriority()) {
      // If the current priority is null and the previous one
      // is set, make sure the priority toolbar is shown.
      if (current.getPriority() == null && previous.getPriority() != null) {
        binding.priorityToolbarLayout.setVisibility(View.VISIBLE);
        checkPriority(previous.getPriority());
      }
      // If the previous priority is null, hide the toolbar.
      else if (previous.getPriority() == null)
        hidePriority();
      // Check the corresponding radio button (high, medium or low)
      else
        checkPriority(previous.getPriority());
    }
  }

  //endregion

  //region Utilities

  private Date getDateSelection() {
    // Grab the current date selection as a string
    String date = binding.dateToolbarTextView.getText().toString();

    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);

    // Parse the date string
    try {
      calendar.setTime(
        Objects.requireNonNull(
          formatter.parse(date)
        )
      );
    } catch(ParseException e) {
      calendar.setTime(new Date());
    }

    return calendar.getTime();
  }

  private void checkPriority(Priority priority) {
    switch (priority) {
      case HIGH:
        binding.priorityToolbarRadioGroup.check(R.id.priority_high_radioButton);
        break;
      case MEDIUM:
        binding.priorityToolbarRadioGroup.check(R.id.priority_medium_radioButton);
        break;
      case LOW:
        binding.priorityToolbarRadioGroup.check(R.id.priority_low_radioButton);
        break;
    }
  }

  public Task getCurrentTask() {
    // If there's no history then no
    // there is no current `Task` instance.
    if (history.isEmpty())
      return new Task().setDescription("").setDue(null).setPriority(null);

    // The current task is at the top of the stack
    Task current = history.peek().copy();
    current.setId(current.getId() + 1);
    return current;
  }

  public void addTask(Task task) {
    Log.d("[addTask]", history.toString());
    // Push a new task onto the history
    history.push(task);
  }

  //endregion
}
