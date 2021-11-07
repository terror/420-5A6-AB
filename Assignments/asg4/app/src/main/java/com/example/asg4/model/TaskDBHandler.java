package com.example.asg4.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import com.example.asg4.sqlite.Table;

public class TaskDBHandler extends SQLiteOpenHelper {
  public static final String DATABASE_FILE_NAME = "tasks.db";
  public static final int DATABASE_VERSION = 1;

  private Table<Task> taskTable;

  public TaskDBHandler(@Nullable Context context) {
    super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
    taskTable = new TaskTable(this);
  }

  public Table<Task> getTaskTable() {
    return taskTable;
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    taskTable.createTable(database);
    if (taskTable.hasInitialData())
      taskTable.initialize(database);
  }

  @Override
  public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {}
}
