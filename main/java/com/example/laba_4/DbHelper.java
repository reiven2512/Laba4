package com.example.laba_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import static com.example.laba_4.Names.con.DB_CREATE;
import static com.example.laba_4.Names.con.SCALE;
import static com.example.laba_4.Names.con.TABLE;
import static com.example.laba_4.Names.con.URL;


public class DbHelper extends SQLiteOpenHelper {

    public DbHelper(Context context) {
        super(context, "DataBase", null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DB_CREATE);
    }

    public List<ImageRow> getAllImages(){
        SQLiteDatabase db = getWritableDatabase();
        List<ImageRow> s = new ArrayList<>();
        Cursor c = db.query(TABLE, null, null, null, null, null,null);
        if(c.moveToFirst()){
            do {
                String url = c.getString(c.getColumnIndex(URL));
                int scale = c.getInt(c.getColumnIndex(SCALE));
                if(c.moveToNext()){
                    String url2 = c.getString(c.getColumnIndex(URL));
                    int scale2 = c.getInt(c.getColumnIndex(SCALE));
                    s.add(new ImageRow(url, url2, scale, scale2));
                }else{
                    s.add(new ImageRow(url, null, scale, 0));
                    break;
                }
            } while (c.moveToNext());
            c.close();
        }
        return s;
    }

    public void insert(String url, int scale) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(URL, url);
        cv.put(SCALE, scale);
        db.insert(TABLE, null, cv);
    }

    public boolean check(String url) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLE, null, null, null, null, null,null);
        if(c.moveToFirst()){
            do {
                String s = c.getString(c.getColumnIndex(URL));
                if(s.equals(url)){
                    c.close();
                    return true;
                }
            } while (c.moveToNext());
            c.close();
        }
        return false;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}