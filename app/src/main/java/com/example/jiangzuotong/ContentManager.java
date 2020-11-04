package com.example.jiangzuotong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContentManager {
    private DBHelper dbHelper;
    private String TBNAME;

    public ContentManager(Context context) {
        dbHelper = new DBHelper(context);
        TBNAME = DBHelper.TB_NAME1;
    }

    public void add(contentItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("date", item.getDate());
        values.put("content", item.getContent());
        db.insert(TBNAME, null, values);
        db.close();
    }

    public void delete(int id) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "ID=?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void onDelete(String item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, "TITLE=?", new String[]{item});
        db.close();
    }

    public void deleteAll() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(TBNAME, null, null);
        db.close();
    }

    public contentItem findById(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null,
                "ID=?", new String[]{String.valueOf(id)},
                null,null, null);
        contentItem item = null;
        if (cursor!=null && cursor.moveToFirst()) {
            item = new contentItem();
            item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
            item.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
            item.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
            item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            cursor.close();
        }
        db.close();
        return item;
    }

    public String[] listColumnValues(String item) {
        String[] columnValues = null;
        List<String> valuesList = new ArrayList<String>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, new String[]{item}, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                valuesList.add(cursor.getString(cursor.getColumnIndex(item)));
            }
            cursor.close();
        }
        db.close();
        columnValues = valuesList.toArray(new String[valuesList.size()]);
        return columnValues;
    }

    public List<contentItem> listAll() {
        List<contentItem> rateList = null;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, null, null, null, null, null);
        if (cursor != null) {
            rateList = new ArrayList<contentItem>();
            while (cursor.moveToNext()) {
                contentItem item = new contentItem();
                item.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                item.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                item.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
                item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
                rateList.add(item);
            }
            cursor.close();
        }
        db.close();
        return rateList;
    }

    public boolean isExist(String item) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String whereClause = "TITLE=?";
        String[] whereArgs = new String[]{item};
        Cursor cursor = db.query(TBNAME, null, whereClause, whereArgs, null, null, null);
        boolean exist = (cursor.getCount()>0);
        cursor.close();
        db.close();
        return exist;
    }

    public Integer getCount() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, new String[]{"TITLE"}, null, null, null, null, null);
        int i = 0;
        if (cursor != null) {
            while (cursor.moveToNext()) {
                i++;
            }
        }
        return i;
    }

    public contentItem getRow(String title) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TBNAME, null, "TITLE=?", new String[]{title}, null, null, null);
        contentItem item = new contentItem();
        if (cursor != null) {
            while (cursor.moveToNext()) {
                item.setTitle(cursor.getString(cursor.getColumnIndex("TITLE")));
                item.setDate(cursor.getString(cursor.getColumnIndex("DATE")));
                item.setContent(cursor.getString(cursor.getColumnIndex("CONTENT")));
            }
        }
        return item;
    }

    public void update(contentItem item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", item.getTitle());
        values.put("date", item.getDate());
        values.put("content", item.getContent());
        db.close();
    }
}