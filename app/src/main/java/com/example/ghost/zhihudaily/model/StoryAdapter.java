package com.example.ghost.zhihudaily.model;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.util.DownloadImage;

import java.util.List;

/**
 * Created by Ghost on 2016/5/25.
 */
public class StoryAdapter extends ArrayAdapter<Story> {

    private int resourceId;

    public StoryAdapter(Context context, int textViewResourceId, List<Story> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Story story = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (story.getBoolean()) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.layout2, null);
            TextView storyTitle = (TextView) view.findViewById(R.id.text);
            String date = story.getDate();
            String year = date.substring(0, 4);
            String mouth = date.substring(4, 6);
            String day = date.substring(6 ,8);
            String d = year + "年" + mouth + "月" + day + "日";
            storyTitle.setText(d);
            return view;
        } else {
            if ((convertView != null && convertView.getTag() == null) || convertView == null) {
                view = LayoutInflater.from(getContext()).inflate(resourceId, null);
                viewHolder = new ViewHolder();
                viewHolder.storyImage = (ImageView) view.findViewById(R.id.story_image);
                viewHolder.storyTitle = (TextView) view.findViewById(R.id.story_title);
                view.setTag(viewHolder); //  将ViewHolder 存储在View
            } else {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            }
            Log.i(story + "", story.getTitle());
            viewHolder.storyTitle.setText(story.getTitle());
            new DownloadImage(story.getImages(), viewHolder.storyImage, story.getId()).execute();
            return view;
        }
    }

    class ViewHolder {
        ImageView storyImage;
        TextView storyTitle;
    }
}

