package com.example.ghost.zhihudaily.util;

/**
 * Created by Ghost on 2016/5/24.
 */
public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
