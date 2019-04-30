package com.group9.inclass13;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TasksTable {
    static final String TABLENAME = "tasks";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TEXT = "text";
    static final String COLUMN_PRIORITY = "priority";
    static final String COLUMN_DATE = "date";

    static public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + TABLENAME + " (");
        sb.append(COLUMN_ID + " integer PRIMARY KEY AUTOINCREMENT , ");
        sb.append(COLUMN_TEXT + " text not null ,");
        sb.append(COLUMN_PRIORITY + " text not null ,");
        sb.append(COLUMN_DATE + " text not null);");
        try {
            db.execSQL(sb.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
        TasksTable.onCreate(db);
    }

}
