package com.example.asg4.ui.util;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * A DatePickerDialogFragment customizable with default time and with user specified event handling
 * <p>
 * - based on class found in https://developer.android.com/guide/topics/ui/controls/pickers.html
 *
 * @author Ian Clement (ian.clement@johnabbott.qc.ca)
 */
public class DatePickerDialogFragment extends DialogFragment {

  // constants for bundling default hour/minute
  private static final String EXTRA_YEAR = "year";
  private static final String EXTRA_MONTH = "month";
  private static final String EXTRA_DAY_OF_MONTH = "day_of_month";

  // custom event listener
  private DatePickerDialog.OnDateSetListener listener;

  /**
   * Create a DatePickerDialogFragment with a default day, month and year, and an event listener.
   *
   * @param initialDate The date displayed when the dialog first displays.
   * @param listener    A date-set event handler.
   * @return
   */
  public static DatePickerDialogFragment create(Date initialDate, DatePickerDialog.OnDateSetListener listener) {
    DatePickerDialogFragment f = new DatePickerDialogFragment();
    f.setDateSetListener(listener);

    Calendar calendar = Calendar.getInstance();
    calendar.setTime(initialDate);

    Bundle bundle = new Bundle(3);
    bundle.putInt(EXTRA_YEAR, calendar.get(Calendar.YEAR));
    bundle.putInt(EXTRA_MONTH, calendar.get(Calendar.MONTH));
    bundle.putInt(EXTRA_DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
    f.setArguments(bundle);
    return f;
  }

  /**
   * Create a TimePickerDialogFragment with an event listener. The default year/month/day will be the current date.
   *
   * @param listener A date-set event handler.
   * @return
   */
  public static DatePickerDialogFragment create(DatePickerDialog.OnDateSetListener listener) {
    return create(new Date(), listener);
  }

  /**
   * Set the event listener of the dialog.
   *
   * @param listener A date-set event handler.
   */
  public void setDateSetListener(DatePickerDialog.OnDateSetListener listener) {
    this.listener = listener;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    int year;
    int month;
    int dayOfMonth;

    Bundle args = getArguments();
    if (args.containsKey(EXTRA_YEAR)) {
      year = args.getInt(EXTRA_YEAR);
      month = args.getInt(EXTRA_MONTH);
      dayOfMonth = args.getInt(EXTRA_DAY_OF_MONTH);
    } else {
      // Use the current time as the default values for the picker
      final Calendar c = Calendar.getInstance();
      year = c.get(Calendar.YEAR);
      month = c.get(Calendar.MONTH);
      dayOfMonth = c.get(Calendar.DAY_OF_MONTH);
    }

    // Create a new instance of TimePickerDialog and return it
    return new DatePickerDialog(getActivity(), listener, year, month, dayOfMonth);
  }
}
