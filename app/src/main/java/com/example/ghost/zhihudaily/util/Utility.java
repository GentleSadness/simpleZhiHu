package com.example.ghost.zhihudaily.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.example.ghost.zhihudaily.activity.ChooseStoryActivity;
import com.example.ghost.zhihudaily.model.DailyDB;
import com.example.ghost.zhihudaily.model.Story;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Ghost on 2016/5/25.
 */
public class Utility {

    private static SimpleDateFormat format = new SimpleDateFormat("MM月dd日 E", Locale.CHINA);

    public static String getTime(Date date){
        return format.format(date);
    }

    public static List<Story> handleListStoryResponse(DailyDB dailyDB, String response) {

        List<Story> list = new ArrayList<Story>();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String date = jsonObject.getString("date");
                Story s = new Story();
                s.setDate(date);
                s.setBoolean(true);
                list.add(s);
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
                    story.setDate(date);
                    //dailyDB.saveStory(story);
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

    public static List<Story> handlePagerResponse(DailyDB dailyDB, String response) {

        List<Story> list = new ArrayList<Story>();

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("top_stories");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                    Story story = new Story();
                    int id = jsonObjectItem.getInt("id");
                    story.setId(id);
                    String title = jsonObjectItem.getString("title");
                    Log.i("Utility", "handlePagerResponse");
                    story.setTitle(title);
                    String images = jsonObjectItem.getString("image");
                    story.setImages(images);
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

    public static List<String> handleNewResponse(String response) {

        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String body = jsonObject.getString("body");
                JSONArray c = jsonObject.getJSONArray("css");
                String css = c.getString(0);
                String image = jsonObject.getString("image");
                List<String> list = new ArrayList<String>();
                list.add(css);
                list.add(body);
                list.add(image);
                return list;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }
}