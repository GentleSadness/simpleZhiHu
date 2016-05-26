package com.example.ghost.zhihudaily.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.ghost.zhihudaily.db.DailyOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/5/24.
 */
public class DailyDB {

    /**
     * 数据库名
     */
    public static final String DB_NAME = "Daily";
    /**
     * 数据库版本
     */
    public static final int VERSION = 1;
    private static DailyDB dailyDB;
    private SQLiteDatabase db;

    private DailyDB(Context context) {
        DailyOpenHelper dbHelper = new DailyOpenHelper(context, DB_NAME, null, VERSION);
        db = dbHelper.getWritableDatabase();
    }

    /**
     * 获取DaliyDB的实例。
     */
    public synchronized static DailyDB getInstance(Context context) {
        if (dailyDB == null) {
            dailyDB = new DailyDB(context);
        }
        return dailyDB;
    }

    /**
     * 将Story实例存储到数据库。
     */
    public void saveStory(Story story) {
        if (story != null) {
            ContentValues values = new ContentValues();
            values.put("title", story.getTitle());
            Log.i("LoadStory", "save " + story.getTitle());
            values.put("images", story.getImages());
            db.insert("Story", null, values);
        }
    }

    public List<Story> loadStory() {
        Log.i("DailyDB","loadStory");
        List<Story> list = new ArrayList<Story>();
        Cursor cursor = db.query("Story", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Story story = new Story();
                story.setId(cursor.getInt(cursor.getColumnIndex("STORY_id")));
                story.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                story.setImages(cursor.getString(cursor.getColumnIndex("images")));
                list.add(story);
                Log.i("LoadStory", "load_STORY_id " + story.getId());
                Log.i("LoadStory", "load_title " + story.getTitle());
                Log.i("LoadStory", "load_images " + story.getImages());
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }
}
