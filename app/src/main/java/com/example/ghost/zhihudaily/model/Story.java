package com.example.ghost.zhihudaily.model;

import android.util.Log;

/**
 * Created by Ghost on 2016/5/24.
 */
public class Story {
    private int id;
    private String title;
    private String images;

    public int getId() {
        Log.i("Story",id + "");
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        Log.i("Story", title);
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getImages() {
        Log.i("Story",images);
        return images;
    }
    public void setImages(String images) {
        this.images = images;
    }
}
