package com.example.jiangzuotong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class MottoManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public MottoManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME2;
    }

    public void add(mottoItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date_1", item.getDate_1());
        values.put("date_2", item.getDate_2());
        values.put("content", item.getContent());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public mottoItem getNewest() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null,null, null);
        mottoItem newest = new mottoItem();
        if (cursor.moveToLast()) {
            newest.setDate_1(cursor.getInt(cursor.getColumnIndex("DATE_1")));
            newest.setDate_2(cursor.getInt(cursor.getColumnIndex("DATE_2")));
            newest.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            cursor.close();
        }
        db.close();
        return newest;
    }

    public List<mottoItem> listAll() {
        List<mottoItem> mottoList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if (cursor != null) {
            mottoList = new ArrayList<mottoItem>();
            while (cursor.moveToNext()) {
                mottoItem item = new mottoItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setDate_1(cursor.getInt(cursor.getColumnIndex("DATE_1")));
                item.setDate_2(cursor.getInt(cursor.getColumnIndex("DATE_2")));
                item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                mottoList.add(item);
            }
            cursor.close();
        }
        db.close();
        return mottoList;
    }
}