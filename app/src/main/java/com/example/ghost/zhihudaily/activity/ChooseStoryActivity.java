package com.example.ghost.zhihudaily.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
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

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/5/25.
 */
public class ChooseStoryActivity extends Activity {

    public static String path;
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
        path =   this.getExternalCacheDir().getPath();
        //Log.i("ChooseStoryActivity",path);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new StoryAdapter(ChooseStoryActivity.this, R.layout.list_item, storyList);
        //adapter = new ArrayAdapter<String>(ChooseStoryActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        dailyDB = DailyDB.getInstance(this);
        queryStory();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = storyList.get(position);
                Intent intent = new Intent(ChooseStoryActivity.this, NewActivity.class);
                intent.putExtra("id", story.getId());
                startActivity(intent);
            }
        });
    }

    private void queryStory(){
        if (storyList.size() > 0) {
            Log.i("Daily","ture");
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
                storyList.addAll(Utility.handleStoryResponse(dailyDB, response));
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
