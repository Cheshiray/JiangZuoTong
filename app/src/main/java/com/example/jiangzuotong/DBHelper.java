package com.example.jiangzuotong;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DB_NAME = "mycontent.db";
    public static final String TB_NAME1 = "tb_content";
    public static final String TB_NAME2 = "tb_motto";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TB_NAME1+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,TITLE TEXT,DATE TEXT,CONTENT TEXT)");
        db.execSQL("CREATE TABLE "+TB_NAME2+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,DATE_1 INT,DATE_2 INT,CONTENT TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}