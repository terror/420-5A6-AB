package com.example.asg4.notification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.asg4.R;
import com.example.asg4.model.Task;
import com.example.asg4.ui.TasksActivity;

public class AlarmReceiver extends BroadcastReceiver {
  private static int taskNotificationId = 0;

  @Override
  public void onReceive(Context context, Intent intent) {
    // intent back to the activity
    Intent resultIntent = new Intent(context, TasksActivity.class)
      .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

    // grab the new task and put it on `resultIntent`
    Task task = Task.fromBundle(intent.getExtras());
    resultIntent.putExtras(task.toBundle());

    // create a notification
    NotificationCompat.Builder builder = new NotificationCompat.Builder(context, TasksActivity.TASKS_NOTIFICATION_CHANNEL)
      .setSmallIcon(R.drawable.ic_baseline_access_alarm_24)
      .setContentText(task.getDescription())
      .setPriority(NotificationCompat.PRIORITY_DEFAULT)
      .setGroup(TasksActivity.TASKS_NOTIFICATION_GROUP)
      .setContentIntent(PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT));

    // notificationId is a unique int for each notification that you must define
    NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
    notificationManager.notify(taskNotificationId++, builder.build());
  }
}
