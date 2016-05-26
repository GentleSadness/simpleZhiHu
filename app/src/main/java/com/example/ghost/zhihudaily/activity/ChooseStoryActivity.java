package com.example.ghost.zhihudaily.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.model.DailyDB;
import com.example.ghost.zhihudaily.model.Story;
import com.example.ghost.zhihudaily.model.StoryAdapter;
import com.example.ghost.zhihudaily.util.HttpCallbackListener;
import com.example.ghost.zhihudaily.util.HttpUtil;
import com.example.ghost.zhihudaily.util.Utility;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/5/25.
 */
public class ChooseStoryActivity extends Activity {

    private ProgressDialog progressDialog;
    private ListView listView;
    private StoryAdapter adapter;
    //private ArrayAdapter<String> adapter;
    private DailyDB dailyDB;
    private List<Story> storyList = new ArrayList<Story>();
    private List<String >  list = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new StoryAdapter(ChooseStoryActivity.this, R.layout.list_item, storyList);
        //adapter = new ArrayAdapter<String>(ChooseStoryActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        dailyDB = DailyDB.getInstance(this);
        queryStory();
    }

    private void queryStory(){
        storyList.clear();
        storyList.addAll(dailyDB.loadStory());
        if (storyList.size() > 0) {
            Log.i("Daily","ture");
            for(Story story: storyList){
                list.add(story.getTitle());
                Log.i("queryStory",story.getTitle());
            }
            adapter.notifyDataSetChanged();
        } else {
            queryFromServer(null, "story");
            Log.i("Daily","false");
        }
    }

    private void queryFromServer(final String code, final String type) {
        String address;
        address = "http://news-at.zhihu.com/api/4/news/latest";
        showProgressDialog();
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleStoryResponse(dailyDB, response);
                Log.i("DB",response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            queryStory();
                            Log.i("notify","Changed");
                        }
                    });
            }
            @Override
            public void onError(Exception e) {
                // 通过runOnUiThread()方法回到主线程处理逻辑
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(ChooseStoryActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 显示进度对话框
     */
    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("正在加载...");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    /**
     * 关闭进度对话框
     */
    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
