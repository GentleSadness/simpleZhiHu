package com.example.ghost.zhihudaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.util.HttpCallbackListener;
import com.example.ghost.zhihudaily.util.HttpUtil;
import com.example.ghost.zhihudaily.util.Utility;

/**
 * Created by Ghost on 2016/5/27.
 */
public class NewActivity extends Activity {

    private String body;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.news);
        webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);
        String path = "http://news-at.zhihu.com/api/4/news/";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        HttpUtil.sendHttpRequest(path + id, new HttpCallbackListener(){

            @Override
            public void onFinish(String response) {
                body = Utility.handleNewResponse(response);
                body = "<head><style>img{ max-width:100%; height:auto; }</style></head>" + body;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null); ; // 根据传入的参数再去加载新的网页
                    }
                });
            }
            @Override
            public void onError(Exception e) {

            }
        });
    }
}
