package com.group9.inclass13;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseDataManger {
    private Context mContext;
    private SQLiteOpenHelper dbOpenHelper;
    private SQLiteDatabase db;
    private TaskDAO taskDAO;

    public DatabaseDataManger(Context mContext) {
        this.mContext = mContext;
        dbOpenHelper = new DatabaseOpenHelper(this.mContext);
        db = dbOpenHelper.getWritableDatabase();
        taskDAO = new TaskDAO(db);
    }

    public void close() {
        if (db != null) {
            db.close();
        }
    }

    public TaskDAO getTaskDAO() {
        return this.taskDAO;
    }

    public long saveTask(Task task) {
        return this.taskDAO.save(task);
    }

    public boolean updateTask(Task task) {
        return this.taskDAO.update(task);
    }

    public boolean deleteTask(Task task) {
        return this.taskDAO.delete(task);
    }

    public Task getTask(Long id) {
        return this.taskDAO.get(id);
    }

    public ArrayList<Task> getAllTasks() {
        return this.taskDAO.getAll();
    }


}
