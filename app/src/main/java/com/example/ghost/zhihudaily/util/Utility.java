package com.example.ghost.zhihudaily.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.example.ghost.zhihudaily.model.DailyDB;
import com.example.ghost.zhihudaily.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/5/25.
 */
public class Utility {
    public static List<Story> handleStoryResponse(DailyDB dailyDB, String response) {

        List<Story> list = new ArrayList<Story>();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("stories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                    Story story = new Story();
                    int id = jsonObjectItem.getInt("id");
                    story.setId(id);
                    String title = jsonObjectItem.getString("title");
                    Log.i("Utility", i + "");
                    story.setTitle(title);
                    JSONArray images = jsonObjectItem.getJSONArray("images");
                    String image = (String) images.get(0);
                    story.setImages(image);
                    dailyDB.saveStory(story);
                    list.add(story);
                }
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}