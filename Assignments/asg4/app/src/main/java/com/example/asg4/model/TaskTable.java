package com.example.asg4.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.asg4.sqlite.Column;
import com.example.asg4.sqlite.DatabaseException;
import com.example.asg4.sqlite.Table;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskTable extends Table<Task> {
  public static final String TABLE_NAME         = "task";
  public static final String COLUMN_DESCRIPTION = "description";
  public static final String COLUMN_STATUS      = "status";
  public static final String COLUMN_PRIORITY    = "priority";
  public static final String COLUMN_TAGS        = "tags";
  public static final String COLUMN_DUE         = "due";
  public static final String COLUMN_URGENCY     = "urgency";

  public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

  /**
   * Create a database table
   *
   * @param dbh the handler that connects to the sqlite database.
   */
  public TaskTable(SQLiteOpenHelper dbh) {
    super(dbh, TABLE_NAME);
    addColumn(new Column(COLUMN_DESCRIPTION, Column.Type.TEXT));
    addColumn(new Column(COLUMN_STATUS,      Column.Type.INTEGER));
    addColumn(new Column(COLUMN_PRIORITY,    Column.Type.INTEGER));
    addColumn(new Column(COLUMN_TAGS,        Column.Type.INTEGER)); // {0, 1}
    addColumn(new Column(COLUMN_DUE,         Column.Type.TEXT));    // DATE_FORMAT
    addColumn(new Column(COLUMN_URGENCY,     Column.Type.REAL));
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Overridden methods                                                       ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  @Override
  protected ContentValues toContentValues(Task task) throws DatabaseException {
    ContentValues values = new ContentValues();
    values.put(COLUMN_DESCRIPTION, task.getDescription());
    values.put(COLUMN_STATUS,      task.getStatus().ordinal());
    values.put(COLUMN_PRIORITY,    task.getPriority().ordinal());
    values.put(COLUMN_TAGS,        booleanToInteger(task.getHasTags()));
    values.put(COLUMN_DUE,         formatDate(task.getDue()));
    values.put(COLUMN_URGENCY,     task.getUrgency());
    return values;
  }

  @Override
  protected Task fromCursor(Cursor cursor) throws DatabaseException {
    Task task = (Task) new Task()
      .setDescription (cursor.getString(1))
      .setStatus      (Status.values()[cursor.getInt(2)])
      .setPriority    (Priority.values()[cursor.getInt(3)])
      .setHasTags     (integerToBoolean(cursor.getInt(4)))
      .setDue         (parseDate(cursor.getString(5)))
      .setUrgency     (cursor.getDouble(6))
      .setId          (cursor.getLong(0));
    return task;
  }

  @Override
  public boolean hasInitialData() {
    return true;
  }

  @Override
  public void initialize(SQLiteDatabase database) {
    for (Task task : TaskData.getData()) {
      try {
        database.insert(TABLE_NAME, null, toContentValues(task));
      } catch (DatabaseException e) {
        e.printStackTrace();
      }
    }
  }

  /*───────────────────────────────────────────────────────────────────────────│─╗
  │ Utilities                                                                ─╬─│┼
  ╚────────────────────────────────────────────────────────────────────────────│*/

  private String formatDate(Date date) {
    if (date == null)
      return null;
    return new SimpleDateFormat(DATE_FORMAT).format(date);
  }

  private Date parseDate(String s) {
    Date ret = null;

    try {
      if (s != null)
        ret = new SimpleDateFormat(DATE_FORMAT).parse(s);
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return ret;
  }

  private boolean integerToBoolean(int i) {
    return i >= 1 ? true : false;
  }

  private int booleanToInteger(boolean b) {
    return b ? 1 : 0;
  }
}
