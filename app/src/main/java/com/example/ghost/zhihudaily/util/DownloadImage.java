package com.example.ghost.zhihudaily.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

import com.example.ghost.zhihudaily.activity.ChooseStoryActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ghost on 2016/5/26.
 */
public class DownloadImage extends AsyncTask<String, Integer, Uri> {

     private int id;
    private String path;
    private ImageView image;

    public DownloadImage(String path, ImageView image , int id) {
        this.path = path;
        this.image = image;
        this.id = id;
    }

    // 后台运行的子线程子线程
    @Override
    protected Uri doInBackground(String... params) {
        try {
            return getImageURI(path, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 这个放在在ui线程中执行
    @Override
    protected void onPostExecute(Uri result) {
        super.onPostExecute(result);
        // 完成图片的绑定
        if (image != null && result != null) {
            image.setImageURI(result);
        }
    }
    public InputStream getImageStream(String path) throws Exception{
        URL url = new URL(path);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(5000);
        conn.setRequestMethod("GET");
        if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
            return conn.getInputStream();
        }
        return null;
    }

 public Uri getImageURI(String path, int id) throws Exception {
        String cache = ChooseStoryActivity.path;
        File file = new File(cache + id);
        // 如果图片存在本地缓存目录，则不去服务器下载
        if (file.exists()) {
            return Uri.fromFile(file);//Uri.fromFile(path)这个方法能得到文件的URI
        } else {
            // 从网络上获取图片
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            if (conn.getResponseCode() == 200) {

                InputStream is = conn.getInputStream();
                FileOutputStream fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                }
                is.close();
                fos.close();
                // 返回一个URI对象
                return Uri.fromFile(file);
            }
        }
        return null;
    }
}
