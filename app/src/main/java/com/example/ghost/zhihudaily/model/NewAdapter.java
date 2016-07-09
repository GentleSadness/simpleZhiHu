package com.example.ghost.zhihudaily.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.activity.ChooseStoryActivity;
import com.example.ghost.zhihudaily.activity.NewActivity;
import com.example.ghost.zhihudaily.util.DownloadImage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/7/3.
 */
public class NewAdapter extends PagerAdapter {

    private List<Story> list;
    private final LayoutInflater inflater;
    //private Story story;
    private Context context;
    private List<View> aaa = new ArrayList<View>();

    public NewAdapter(List<Story> list, Context context){
        list.add(0,list.get(list.size() - 1));
        list.add(list.get(1));
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
        return list.size() ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // TODO Auto-generated method stub
        container.removeView((View)object);
    }

    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        // TODO Auto-generated method stub
        final Story story;
        View view;
        ImageView pagerIamge;

            story = list.get(position);
            view = inflater.inflate(R.layout.view_item, container, false);
            TextView pagerTitle = (TextView) view.findViewById(R.id.view_text);
            pagerIamge = (ImageView) view.findViewById(R.id.view_image);
            pagerTitle.setText(story.getTitle());
            new DownloadImage(story.getImages(), pagerIamge, story.getId()).execute();
            Log.i("view", story.getTitle());
            set(view, story);
            container.addView(view);
            return view;


/*        if (aaa.size() > position) {
            story = list.get(position);
            view = inflater.inflate(R.layout.view_item, container, false);
            TextView pagerTitle = (TextView) view.findViewById(R.id.view_text);
            pagerIamge = (ImageView) view.findViewById(R.id.view_image);
            pagerTitle.setText(story.getTitle());
            new DownloadImage(story.getImages(), pagerIamge, story.getId()).execute();
            Log.i("view", story.getTitle());
            set(view, story);
            container.addView(view);
            return view;
        } else if (aaa.size() <= position) {
            if(aaa == null){
                story = list.get(position);
                view = inflater.inflate(R.layout.view_item, container, false);
                TextView pagerTitle = (TextView) view.findViewById(R.id.view_text);
                pagerIamge = (ImageView) view.findViewById(R.id.view_image);
                pagerTitle.setText(story.getTitle());
                new DownloadImage(story.getImages(), pagerIamge, story.getId()).execute();
                Log.i("view", story.getTitle());
                if(position == 0){
                    aaa.add(0, view);
                }else {
                    aaa.add(view);
                }

                set(view, story);
                container.addView(view);
            }else if(aaa != null){
                story = list.get(position);
                view = inflater.inflate(R.layout.view_item, container, false);
                TextView pagerTitle = (TextView) view.findViewById(R.id.view_text);
                pagerIamge = (ImageView) view.findViewById(R.id.view_image);
                pagerTitle.setText(story.getTitle());
                new DownloadImage(story.getImages(), pagerIamge, story.getId()).execute();
                Log.i("view", story.getTitle());
                if(position == 0){
                    view = aaa.get(0);
                }else {
                    view = aaa.get(1);
                }
                set(view, story);
                container.addView(view);
                return view;
            }
            container.addView(aaa.get(position));
            return aaa.get(position);
        }*/

        //给当前页添加监听

    }
    private void set(View view,final Story story){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewActivity.class);
                intent.putExtra("id", story.getId());
                context.startActivity(intent);
                //Log.i("view", story.getTitle());
            }
        });
    }

}
