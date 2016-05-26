package com.example.ghost.zhihudaily.util;

import android.content.Context;
import android.util.Log;

import com.example.ghost.zhihudaily.model.DailyDB;
import com.example.ghost.zhihudaily.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ghost on 2016/5/25.
 */
public class Utility {
    public static void handleStoryResponse(DailyDB dailyDB, String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                Story story = new Story();
                int id = jsonObjectItem.getInt("id");
                story.setId(id);
                String title = jsonObjectItem.getString("title");
                Log.i("Utility", i +"");
                story.setTitle(title);
                String images = jsonObjectItem.getString("images");
                story.setImages(images);
                dailyDB.saveStory(story);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
