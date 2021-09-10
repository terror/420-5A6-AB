package com.example.asg1.ui.handlers;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.widget.DatePicker;
import com.example.asg1.ui.editor.TaskEditFragment;
import com.example.asg1.ui.util.TimePickerDialogFragment;
import java.util.Calendar;
import java.util.Date;

public class DatePickerDialogOnSetDateHandler implements DatePickerDialog.OnDateSetListener {
  private TaskEditFragment fragment;
  private int hour = 8;
  private int minute = 0;

  public DatePickerDialogOnSetDateHandler(TaskEditFragment fragment) {
    this.fragment = fragment;
  }

  public DatePickerDialogOnSetDateHandler(TaskEditFragment fragment, int hour, int minute) {
    this.fragment = fragment;
    this.hour = hour;
    this.minute = minute;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
    // Ensure the date is not in the past
    if (!validDate(year, month, day)) {
      // Display the alert dialog
      new AlertDialog.Builder(fragment.getContext())
        .setTitle("Invalid due date")
        .setMessage("Please select a date in the future.")
        .setNegativeButton(android.R.string.yes, null)
        .setIcon(android.R.drawable.ic_dialog_alert)
        .show();

      // Ensure we're able to select a date again
      fragment.hideDate();

      return;
    }

    // Set the date on a `Calendar` instance.
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    // Default the time picker to `8:00AM` or the currently
    // selected time.
    calendar.set(Calendar.HOUR, hour);
    calendar.set(Calendar.MINUTE, minute);

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

  private boolean validDate(int year, int month, int day) {
    // Set the time on a `Calendar` instance
    Calendar calendar = Calendar.getInstance();
    calendar.set(year, month, day);

    // If the calendar time is not before the current
    // time, it is a valid date
    return !calendar.getTime().before(new Date());
  }
}
