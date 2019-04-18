package com.liuhai.wcbox.lock.act;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.liuhai.wcbox.R;


public class WebViewActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String url =getIntent().getStringExtra("url");
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.webview);
       final  ProgressBar bar= (ProgressBar) findViewById(R.id.progress);


        WebSettings settings=webView.getSettings();
        settings.setDomStorageEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(getCacheDir().getAbsolutePath());
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setSupportMultipleWindows(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDatabaseEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setPluginState(WebSettings.PluginState.ON);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                bar.setVisibility(View.GONE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url1) {
                if (url1.startsWith("weixin://")) {
                    // if (url.contains("wx.tenpay")) {
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setComponent(cmp);
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // TODO: handle exception

                    }
                    return  true;
                }

                Log.d("url",url1);
                webView.loadUrl(url1);
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Log.d("BigAd",request.getUrl().toString());
                    if (request.getUrl().toString().startsWith("weixin://")) {
                        // if (url.contains("wx.tenpay")) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_MAIN);
                            ComponentName cmp = new ComponentName("com.tencent.mm","com.tencent.mm.ui.LauncherUI");
                            intent.addCategory(Intent.CATEGORY_LAUNCHER);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.setComponent(cmp);
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // TODO: handle exception

                        }

                        return  true;
                    }
                }
                Log.d("url",request.getUrl().toString());
                webView.loadUrl(request.getUrl().toString());

                return true;
            }
        });
        webView.loadUrl(url);
    }
}
