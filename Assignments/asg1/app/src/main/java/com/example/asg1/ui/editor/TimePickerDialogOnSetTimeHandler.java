package com.example.asg1.ui.editor;

import android.app.TimePickerDialog;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePickerDialogOnSetTimeHandler implements TimePickerDialog.OnTimeSetListener {
  private TaskEditFragment fragment;
  private Calendar calendar;

  public TimePickerDialogOnSetTimeHandler(TaskEditFragment fragment, Calendar calendar) {
    this.fragment = fragment;
    this.calendar = calendar;
  }

  @Override
  public void onTimeSet(TimePicker timePicker, int hour, int minute) {
    // Set the chosen `hour` and `minute` on the
    // passed in `Calendar instance.
    calendar.set(Calendar.HOUR, hour);
    calendar.set(Calendar.MINUTE, minute);

    // Set the date toolbar text and display it
    fragment.displayDate(calendar);
  }
}
