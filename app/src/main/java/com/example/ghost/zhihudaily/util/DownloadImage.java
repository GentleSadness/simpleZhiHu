package com.example.ghost.zhihudaily.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;

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
public class DownloadImage extends AsyncTask<String, Integer, Bitmap> {

    private String path;
    private ImageView image;

    public DownloadImage(String path, ImageView image) {
        this.path = path;
        this.image = image;
    }

    // 后台运行的子线程子线程
    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            return BitmapFactory.decodeStream(getImageStream(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 这个放在在ui线程中执行
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        // 完成图片的绑定
        if (image != null && bitmap != null) {
            image.setImageBitmap(bitmap);
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
}
