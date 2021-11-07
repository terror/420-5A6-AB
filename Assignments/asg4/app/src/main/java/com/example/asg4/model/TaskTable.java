package com.example.asg4.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.asg4.sqlite.DatabaseException;
import com.example.asg4.sqlite.Table;

public class TaskTable extends Table<Task> {
  public static final String TABLE_NAME         = "task";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_STATUS      = "status";
  public static final String COLUMN_PRIORITY    = "priority";
  public static final String COLUMN_PROJECT     = "project";
  public static final String COLUMN_ANNOTATIONS = "annotations";
  public static final String COLUMN_TAGS        = "tags";
  public static final String COLUMN_ENTRY       = "entry";
  public static final String COLUMN_MODIFIED    = "modified";
  public static final String COLUMN_DUE         = "due";
  public static final String COLUMN_START       = "start";
  public static final String COLUMN_END         = "end";
  public static final String COLUMN_WAIT        = "wait";
  public static final String COLUMN_SCHEDULED   = "scheduled";
  public static final String COLUMN_RECUR       = "recur";
  public static final String COLUMN_MASK        = "mask";
  public static final String COLUMN_IMASK       = "imask";
  public static final String COLUMN_UNTIL       = "until";
  public static final String COLUMN_PARENT      = "parent";
  public static final String COLUMN_DEPENDS     = "depends";
  public static final String COLUMN_URGENCY     = "urgency";
  public static final String COLUMN_TRASH       = "trash";

  /**
   * Create a database table
   *
   * @param dbh the handler that connects to the sqlite database.
   */
  public TaskTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
  }

  @Override
  protected ContentValues toContentValues(Task element) throws DatabaseException {
    ContentValues values = new ContentValues();
    return values;
  }

  @Override
  protected Task fromCursor(Cursor cursor) throws DatabaseException {
    Task task = new Task();
    return task;
  }

  @Override
  public boolean hasInitialData() {
    return true;
  }

  @Override
  public void initialize(SQLiteDatabase database) {
    super.initialize(database);
  }
}
