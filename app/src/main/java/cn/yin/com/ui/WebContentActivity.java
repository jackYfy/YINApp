package cn.yin.com.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.OnClick;
import cn.yin.com.Presenter.WebContentPresenter;
import cn.yin.com.R;
import cn.yin.com.base.BaseActivity;
import cn.yin.com.entity.Gank;
import cn.yin.com.interfaces.WebContentInterface;

public class WebContentActivity extends BaseActivity implements WebContentInterface {

    @Bind(R.id.web_content)
    public WebView webView;
    @Bind(R.id.webview_pb)
    public ProgressBar webviewPb;
    @Bind(R.id.load_error)
    public LinearLayout load_error;


    private Gank gank;
    private WebContentPresenter webContentPresenter;
    @Override
    public int bindLayout() {
        return R.layout.activity_web_content;
    }

    @SuppressLint("NewApi")
    @Override
    public void initView(View view) {
        setBackup();
        setTitle("");
        webContentPresenter=new WebContentPresenter(this);
        gank=(Gank)getIntent().getSerializableExtra("gank");
       if (gank!=null){
           setTitle(gank.desc);
       }
        webContentPresenter.initWebView(this,webView,webviewPb,gank);

    }

    @Override
    public void loadError() {
        webView.setVisibility(View.GONE);
        load_error.setVisibility(View.VISIBLE);
    }

    @Override
    public void saveResult(boolean status) {

    }

    @Override
    public void deleteResult(boolean status) {

    }

    @Override
    public void collectOptionsMenuResult(boolean status) {

    }

    @OnClick(R.id.load_error)
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.load_error:
                webContentPresenter.errorReLoad(webView,load_error,gank);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK) {
            if(webView.canGoBack()) {
                webView.goBack();
                return true;
            }else {
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
