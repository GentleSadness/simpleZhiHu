package com.example.ghost.zhihudaily.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.ghost.zhihudaily.R;
import com.example.ghost.zhihudaily.util.DownloadImage;
import com.example.ghost.zhihudaily.util.HttpCallbackListener;
import com.example.ghost.zhihudaily.util.HttpUtil;
import com.example.ghost.zhihudaily.util.Utility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ghost on 2016/5/27.
 */
public class NewActivity extends Activity {

    private String body;
    private WebView webView;
    private String aaa;
    private View view;
    private List<String> list;
    private String a;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news);
        webView = (WebView) findViewById(R.id.web_view);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        setTitle(intent.getStringExtra("title"));
        String path = "http://news-at.zhihu.com/api/4/news/";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        view = getLayoutInflater().inflate(R.layout.layout3,webView, false);
        HttpUtil.sendHttpRequest(path + id, new HttpCallbackListener(){

            @Override
            public void onFinish(String response) {
                list = new ArrayList<String>();
                list = Utility.handleNewResponse(response);
                body = "<head><style>img{ max-width:100%; height:auto; }</style></head>" + body;
                aaa = convertToHtml(list);
                a = list.get(2);

                ImageView image = (ImageView) view.findViewById(R.id.new_image);
                File file = new File(ChooseStoryActivity.path + "/" + id);
                new DownloadImage(a , image, id+1000).execute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadDataWithBaseURL(null, aaa, null, null, null); ; // 根据传入的参数再去加载新的网页
                    }
                });
            }
            @Override
            public void onError(Exception e) {

            }
        });


        webView.addView(view, 0);
        //view = getLayoutInflater().inflate(R.layout.layout1,webView, true);
        //webView.addView(view, 0);

    }
    public String convertToHtml(List<String> webContent){
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append("<html>\n" +
                "<head>\n" +
                "<link rel=\"stylesheet\" href=\"").append(webContent.get(0)).append(
                "\" type=\"text/css\">\n" +
                        "\t\t<meta charset=\"utf-8\">\n"
        ).append("</head>\n" +"<body>\n").append(webContent.get(1)).append("</body>\n" +
                "</html>");
        return stringBuilder.toString();
    }
}
