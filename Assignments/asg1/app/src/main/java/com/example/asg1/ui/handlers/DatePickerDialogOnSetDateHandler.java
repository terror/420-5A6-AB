package com.example.asg1.ui.handlers;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import com.example.asg1.ui.editor.TaskEditFragment;
import com.example.asg1.ui.util.TimePickerDialogFragment;

import java.util.Calendar;

public class DatePickerDialogOnSetDateHandler implements DatePickerDialog.OnDateSetListener {
  private TaskEditFragment fragment;

  public DatePickerDialogOnSetDateHandler(TaskEditFragment fragment) {
    this.fragment = fragment;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
    // Set the date on a `Calendar` instance.
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    // Default the time picker to `8:00AM`.
    calendar.set(Calendar.HOUR, 8);
    calendar.set(Calendar.MINUTE, 0);

    // Create a `TimePickerDialogFragment` with a custom event listener
    TimePickerDialogFragment timePickerFragment = TimePickerDialogFragment.create(
      calendar.getTime(),
      new TimePickerDialogOnSetTimeHandler(fragment, calendar)
    );

    // Display the time picker fragment
    timePickerFragment.show(
      fragment.getParentFragmentManager(),
      "timePicker"
    );
  }
}
