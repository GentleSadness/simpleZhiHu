package com.example.ghost.zhihudaily.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ghost on 2016/5/24.
 */
public class DailyOpenHelper extends SQLiteOpenHelper {
    public static final String CREATE_STORY = "create table Story ("
            + "id integer primary key autoincrement, "
            + "title text, "
            + "images text, "
            + "STORY_id integer)";

    public static final String CREATE_NEW = "create table New ("
            + "id integer primary key autoincrement, "
            + "body text, "
            + "title text, "
            + "image text, "
            + "share_url text, "
            + "js text, "
            + "type integer, "
            + "css text, "
            + "NEW_id integer)";

    public DailyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_STORY);
        db.execSQL(CREATE_NEW);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}

