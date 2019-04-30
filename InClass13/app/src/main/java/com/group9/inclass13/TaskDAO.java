package com.group9.inclass13;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

public class TaskDAO {
    private SQLiteDatabase db;

    public TaskDAO(SQLiteDatabase db) {
        this.db = db;
    }

    public long save(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.COLUMN_TEXT, task.getText());
        contentValues.put(TasksTable.COLUMN_PRIORITY, task.getPriority());
        contentValues.put(TasksTable.COLUMN_DATE, persistDate(task.getDate()));
        return db.insert(TasksTable.TABLENAME, null, contentValues);
    }

    public boolean update(Task task) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TasksTable.COLUMN_TEXT, task.getText());
        contentValues.put(TasksTable.COLUMN_PRIORITY, task.getPriority());
        contentValues.put(TasksTable.COLUMN_DATE, persistDate(task.getDate()));
        return db.update(TasksTable.TABLENAME, contentValues, TasksTable.COLUMN_ID + "=?", new String[]{task.get_id() + ""}) > 0;
    }

    public boolean delete(Task task) {
        return db.delete(TasksTable.TABLENAME, TasksTable.COLUMN_ID + "=?", new String[]{task.get_id() + ""}) > 0;
    }

    public Task get(Long id) {
        Task task = null;
        Cursor c = db.query(true, TasksTable.TABLENAME, new String[]{TasksTable.COLUMN_ID, TasksTable.COLUMN_TEXT, TasksTable.COLUMN_PRIORITY, TasksTable.COLUMN_DATE},
                TasksTable.COLUMN_ID + "=?", new String[]{id + ""}, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            task = buildTaskFromCursor(c);
            if (!c.isClosed()) c.close();
        }
        return task;
    }

    public ArrayList<Task> getAll() {
        ArrayList<Task> tasks = new ArrayList<>();

        Cursor c = db.query(true, TasksTable.TABLENAME, new String[]{TasksTable.COLUMN_ID, TasksTable.COLUMN_TEXT, TasksTable.COLUMN_PRIORITY, TasksTable.COLUMN_DATE},
                null, null, null, null, null, null);

        if (c != null && c.moveToFirst()) {
            do {
                Task task = buildTaskFromCursor(c);
                if (task != null) {
                    tasks.add(task);
                }
            }
            while (c.moveToNext());
            if (!c.isClosed()) c.close();
        }
        return tasks;
    }

    public Task buildTaskFromCursor(Cursor c) {
        Task task = null;
        if (c != null) {
            task = new Task();
            task.set_id(c.getLong(0));
            task.setText(c.getString(1));
            task.setPriority(c.getString(2));
            task.setDate(loadDate(c, 3));
        }
        return task;
    }

    public static Long persistDate(Date date) {
        if (date != null) {
            return date.getTime();
        }
        return null;
    }

    public static Date loadDate(Cursor cursor, int index) {
        if (cursor.isNull(index)) {
            return null;
        }
        return new Date(cursor.getLong(index));
    }
}
