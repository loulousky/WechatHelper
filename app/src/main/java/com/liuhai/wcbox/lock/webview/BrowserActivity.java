package com.liuhai.wcbox.lock.webview;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.liuhai.wcbox.R;
import com.liuhai.wcbox.lock.webview.utils.X5WebView;
import com.tencent.smtt.sdk.CookieSyncManager;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebSettings.LayoutAlgorithm;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.net.URISyntaxException;

public class BrowserActivity extends Activity {
    private static final String TAG = "BrowserActivity";
    private X5WebView mWebView;
    private ViewGroup mViewParent;
    private ProgressBar progressBar = null;
    private TextView titleView = null;

    private String linkUrl = "";
    private String title = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        linkUrl = getIntent().getStringExtra("linkUrl");
        title = getIntent().getStringExtra("title");
        init();
    }

    private void initProgressBar() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setMax(100);
        progressBar.setProgressDrawable(this.getResources()
                .getDrawable(R.drawable.color_progressbar));

    }

    public void backAction(View v) {
        finish();
    }

    private void init() {
        try {


            mViewParent = (ViewGroup) findViewById(R.id.webView1);
            titleView = (TextView) findViewById(R.id.title);
            titleView.setText(title + "");
            initProgressBar();
            X5WebView.setSmallWebViewEnabled(true);
            mWebView = new X5WebView(this, null);
            mViewParent.addView(mWebView, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
            mWebView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url.startsWith("http")) {
                        linkUrl = url;
                        return false;
                    } else
                        try {
                            {
                                Intent intent = null;
                                try {
                                    try {
                                        intent = Intent.parseUri(url,
                                                Intent.URI_INTENT_SCHEME);
                                    } catch (URISyntaxException e) {
                                        e.printStackTrace();
                                    }
                                    if (intent != null) {
                                        BrowserActivity.this.startActivity(intent);
                                    }

                                } catch (ActivityNotFoundException ex) {
                                    return false;
                                }

                                return false;
                            }
                        } catch (Exception e) {
                            return false;
                        }
                }
            });

            mWebView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onProgressChanged(WebView webView, int i) {
                    super.onProgressChanged(webView, i);
                    if (i < 100)
                        progressBar.setProgress(i);
                    else
                        progressBar.setVisibility(View.GONE);
                }

                /**
                 *  设置title
                 */
                @Override
                public void onReceivedTitle(WebView arg0, final String arg1) {
                    super.onReceivedTitle(arg0, arg1);
                    if (title == null || "".equals(title))
                        titleView.setText("微视频");
                }
            });

            mWebView.setDownloadListener(new DownloadListener() {

                @Override
                public void onDownloadStart(String arg0, String arg1, String arg2,
                                            String arg3, long arg4) {
                    mWebView.Download(arg0);
                }
            });


            WebSettings webSetting = mWebView.getSettings();
            webSetting.setAllowFileAccess(true);
            webSetting.setLayoutAlgorithm(LayoutAlgorithm.NARROW_COLUMNS);
            webSetting.setSupportZoom(true);
            webSetting.setBuiltInZoomControls(true);
            webSetting.setUseWideViewPort(true);
            webSetting.setSupportMultipleWindows(false);
            webSetting.setAppCacheEnabled(true);
            webSetting.setDomStorageEnabled(true);
            webSetting.setJavaScriptEnabled(true);
            webSetting.setGeolocationEnabled(true);
            webSetting.setAppCacheMaxSize(Long.MAX_VALUE);
            webSetting.setAppCachePath(this.getDir("appcache", 0).getPath());
            webSetting.setDatabasePath(this.getDir("databases", 0).getPath());
            webSetting.setGeolocationDatabasePath(this.getDir("geolocation", 0).getPath());
            webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND);
            mWebView.loadUrl(linkUrl);
            CookieSyncManager.createInstance(this);
            CookieSyncManager.getInstance().sync();
        } catch (Exception e) {

        }
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            if (mWebView != null && mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                return super.dispatchKeyEvent(event);
            }
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void finish() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        super.finish();
    }

    @Override
    protected void onDestroy() {
        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }


    public void share(View v) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, "分享链接:" + linkUrl);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shareIntent.setType("text/plain");
        startActivity(Intent.createChooser(shareIntent, "分享到"));

    }


}
