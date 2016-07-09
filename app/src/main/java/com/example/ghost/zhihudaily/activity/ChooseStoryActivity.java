package com.example.ghost.zhihudaily.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.model.DailyDB;
import com.example.ghost.zhihudaily.model.MyRefreshLayout;
import com.example.ghost.zhihudaily.model.NewAdapter;
import com.example.ghost.zhihudaily.model.Story;
import com.example.ghost.zhihudaily.model.StoryAdapter;
import com.example.ghost.zhihudaily.model.SwpipeListViewOnScrollListener;
import com.example.ghost.zhihudaily.util.HttpCallbackListener;
import com.example.ghost.zhihudaily.util.HttpUtil;
import com.example.ghost.zhihudaily.util.Utility;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;


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
    private List<Story> topList = new ArrayList<Story>();
    private List<String >  list = new ArrayList<String>();
    private NewAdapter newAdapter;

    public int currentItem=0;

    private TextView indexText;

    private Button innerViewPagerDemo;

    private List<Integer>       imageIdList;

    private View view1, view2, view3;
    private List<View> viewList;// view����
    private ViewPager viewPager; // ��Ӧ��viewPager
    PagerAdapter pagerAdapter;
    CircleIndicator indicator;

    private boolean list_flag = true;

    private MyRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        path = this.getFilesDir().getPath();
        //Log.i("ChooseStoryActivity",path);

        //viewPager = (ViewPager) findViewById(R.id.view_pager);
        swipeRefreshLayout = (MyRefreshLayout) findViewById(R.id.swipe_container);
        listView = (ListView) findViewById(R.id.list_view);
        adapter = new StoryAdapter(ChooseStoryActivity.this, R.layout.list_item, storyList);

        View hearderViewLayout = getLayoutInflater().inflate(R.layout.view_pager, null);
        indicator = (CircleIndicator) hearderViewLayout.findViewById(R.id.indicator);
        viewPager = (ViewPager) hearderViewLayout.findViewById(R.id.view_pager);


        listView.addHeaderView(hearderViewLayout);
        //adapter = new ArrayAdapter<String>(ChooseStoryActivity.this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        dailyDB = DailyDB.getInstance(this);
        queryStory();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Story story = storyList.get(position - 1);
                Intent intent = new Intent(ChooseStoryActivity.this, NewActivity.class);
                intent.putExtra("id", story.getId());
                startActivity(intent);
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                //Toast.makeText(ChooseStoryActivity.this,"正在刷新",Toast.LENGTH_SHORT).show();
                queryStory();
                //Toast.makeText(ChooseStoryActivity.this,"刷新完成",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentItem = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 0){
                    if(currentItem == (topList.size() - 1)){
                        viewPager.setCurrentItem(1, false);
                    }else if(currentItem == 0){
                        viewPager.setCurrentItem(topList.size() - 2, false);
                    }
                }
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if(listView.getLastVisiblePosition()==listView.getCount()-1){
                    if(list_flag){
                        list_flag = false;
                        Toast.makeText(ChooseStoryActivity.this,"onScrollStateChanged",Toast.LENGTH_SHORT).show();
                        String address;
                        address = "http://news.at.zhihu.com/api/4/news/before/";
                        address += 20160709;
                        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
                            @Override
                            public void onFinish(String response) {
                                storyList.addAll(Utility.handleListStoryResponse(dailyDB, response));
                                Log.i("DB",response);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        queryStory();
                                        list_flag = true;
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
                                        list_flag = true;
                                        Toast.makeText(ChooseStoryActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                View firstView = view.getChildAt(firstVisibleItem);

                // 当firstVisibleItem是第0位。如果firstView==null说明列表为空，需要刷新;或者top==0说明已经到达列表顶部, 也需要刷新
                if (firstVisibleItem == 0 && (firstView == null || firstView.getTop() == 0)) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }
    private void queryStory(){
        if (storyList.size() > 0) {
            Log.i("Daily","ture");
            adapter.notifyDataSetChanged();
            if(newAdapter == null){
                newAdapter = new NewAdapter(topList, this);//句柄，注意
            }
            viewPager.setAdapter(newAdapter);
            indicator.setViewPager(viewPager);
            //setViewPagerAdapter(newAdapter);
            swipeRefreshLayout.setRefreshing(false);
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
                storyList.addAll(Utility.handleListStoryResponse(dailyDB, response));
                topList.addAll(Utility.handlePagerResponse(dailyDB, response));
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

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int pagerCount = viewPager.getAdapter().getCount();

            viewPager.setCurrentItem(currentItem);
            currentItem++;

            if (currentItem > pagerCount-1) {
                currentItem = 0;
            }
            this.sendEmptyMessageDelayed(0, 3000);
//            Log.e("TAG4","current:"+currentItem+"pagercount:"+pagerCount);
            Log.e("TAG4","currentItem4:"+viewPager.getCurrentItem()+"pagercount:"+pagerCount);

        }
    };

    public void setViewPagerAdapter(PagerAdapter adapter) {
        mHandler.sendEmptyMessageDelayed(0, 2000);//让viewpager轮播
        //当viewpager获得焦点时停止轮播
        viewPager.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mHandler.removeCallbacksAndMessages(null);
                } else {
                    mHandler.sendEmptyMessageDelayed(0, 2000);
                }
            }
        });
    }
}
