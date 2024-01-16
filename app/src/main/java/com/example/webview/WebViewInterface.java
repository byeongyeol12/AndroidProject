package com.example.webview;

import android.app.Activity;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class WebViewInterface {
    private WebView mWebView;
    private Activity mContext;
    private final Executor executor= Executors.newSingleThreadExecutor();

    public WebViewInterface(Activity activity, WebView view){
        mWebView=view;
        mContext=activity;
    }

    @JavascriptInterface
    public void callSettingsActivity(){
        Toast.makeText(mContext,"setting in ...", Toast.LENGTH_LONG).show();

        Intent intent=new Intent(mContext, SelectActivity.class);
        mContext.startActivity(intent);
    }

    @JavascriptInterface
    public void doBackPressed() {
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.onBackPressed();
            }
        });
    }

    @JavascriptInterface
    public void closeApp(){
        mContext.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mContext.finish();
            }
        });
    }
}
