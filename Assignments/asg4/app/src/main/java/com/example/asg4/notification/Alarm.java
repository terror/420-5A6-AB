package com.example.asg4.notification;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.asg4.model.Task;
import com.example.asg4.ui.TasksActivity;
import java.util.HashMap;

public class Alarm {
  // all set alarms
  private static HashMap<Task, PendingIntent> alarms = new HashMap<>();

  public static void set(TasksActivity activity, Task task) {
    // Don't set alarms in the past and don't process tasks without a due date
    if (task.isOverdue() || task.getDue() == null)
      return;

    // Grab the alarm manager
    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

    // Passing our intent data
    Intent alarmIntent = new Intent(activity, AlarmReceiver.class);
    alarmIntent.putExtras(task.toBundle());

    // Craft the pending intent
    PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    // Save task -> pendingIntent
    alarms.put(task, pendingIntent);

    // Set the alarm
    alarmManager.set(
      AlarmManager.RTC_WAKEUP,
      task.getDue().getTime(),
      pendingIntent
    );
  }

  public static void cancel(TasksActivity activity, Task task) {
    // If there's no currently stored intent with `task`, return
    if (!alarms.containsKey(task))
      return;

    // Grab the alarm manager
    AlarmManager alarmManager = (AlarmManager) activity.getSystemService(Context.ALARM_SERVICE);

    // Cancel the alarm
    alarmManager.cancel(alarms.get(task));

    // Delete the task from alarms
    alarms.remove(task);
  }

  public static void update(TasksActivity activity, Task task) {
    // Cancel the current set alarm for this task
    cancel(activity, task);

    // Set a new alarm for this task
    set(activity, task);
  }
}
