package com.example.ghost.zhihudaily.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghost.zhihudaily.R;

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
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        TextView storyTitle = (TextView) view.findViewById(R.id.story_title);
        storyTitle.setText(story.getTitle());
        return view;
    }
}
