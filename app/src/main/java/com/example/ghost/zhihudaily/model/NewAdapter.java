package com.example.ghost.zhihudaily.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.activity.NewActivity;
import com.example.ghost.zhihudaily.util.DownloadImage;

import java.util.List;

/**
 * Created by Ghost on 2016/7/3.
 */
public class NewAdapter extends PagerAdapter {

    private List<Story> list;
    private final LayoutInflater inflater;
    //private Story story;
    private Context context;

    public NewAdapter(List<Story> list, Context context){
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        // TODO Auto-generated method stub
        final Story story = list.get(position);
        View view = inflater.inflate(R.layout.view_item, container, false);
        TextView pagerTitle = (TextView) view.findViewById(R.id.view_text);
        ImageView pagerIamge = (ImageView) view.findViewById(R.id.view_image);
        pagerTitle.setText(story.getTitle());
        new DownloadImage(story.getImages(),pagerIamge , story.getId()).execute();
        Log.i("view", story.getTitle());
        container.addView(view);

        //给当前页添加监听
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("id", story.getId());
                context.startActivity(intent);
                Log.i("view", story.getTitle());
            }
        });
        return view;
    }
}
