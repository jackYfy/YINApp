package cn.yin.com.Presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import cn.yin.com.entity.Gank;
import cn.yin.com.interfaces.WebContentInterface;
import cn.yin.com.utils.MyWebViewClient;

/**
 * Created by 79859 on 2018/5/10.
 */

public class WebContentPresenter extends BasePresenter {

    public WebContentPresenter(Context context) {
        super(context);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initWebView(final WebContentInterface webContentInterface,
                            WebView webView, ProgressBar webviewPb, Gank gank){
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//是否允许执行js，默认为false。设置true时，会提醒可能造成XSS漏洞
        settings.setSupportZoom(true);//是否可以缩放，默认true
        settings.setLoadWithOverviewMode(true);//和setUseWideViewPort(true)一起解决网页自适应问题
        settings.setAppCacheEnabled(true);//是否使用缓存
        settings.setAllowFileAccess(true);//设置可以访问文件
        settings.setDomStorageEnabled(true);//DOM Storage
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);//加载https和http混合模式
        webView.setWebChromeClient(new MyWebViewClient(webviewPb));
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url){
                return false;
            }
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                webContentInterface.loadError();
            }
        });
        if(gank!=null){
            webView.loadUrl(gank.url);
        }else{
            webContentInterface.loadError();
        }
    }

    public void errorReLoad(WebView webView, LinearLayout load_error, Gank gank){
        webView.setVisibility(View.VISIBLE);
        load_error.setVisibility(View.GONE);
        webView.loadUrl(gank.url);
    }
}
