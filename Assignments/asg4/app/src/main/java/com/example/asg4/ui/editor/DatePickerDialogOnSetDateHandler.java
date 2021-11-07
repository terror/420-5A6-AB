package com.example.asg4.ui.editor;

import android.app.DatePickerDialog;
import android.widget.DatePicker;
import com.example.asg4.ui.util.TimePickerDialogFragment;
import java.util.Calendar;

public class DatePickerDialogOnSetDateHandler implements DatePickerDialog.OnDateSetListener {
  private TaskEditFragment fragment;
  private int hour   = 8;
  private int minute = 0;

  public DatePickerDialogOnSetDateHandler(TaskEditFragment fragment) {
    this.fragment = fragment;
  }

  public DatePickerDialogOnSetDateHandler(TaskEditFragment fragment, int hour, int minute) {
    this.fragment = fragment;
    this.hour     = hour;
    this.minute   = minute;
  }

  @Override
  public void onDateSet(DatePicker datePicker, int year, int month, int day) {
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
}
