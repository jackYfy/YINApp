package cn.yin.com.utils;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * 类名称：MyWebViewClient.java <br>
 * @author 谢汉杰
 */

public class MyWebViewClient extends WebChromeClient {

    private ProgressBar pb;

    public MyWebViewClient(ProgressBar pb){
        this.pb=pb;
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        pb.setProgress(newProgress);
        if(newProgress==100){
            pb.setVisibility(View.GONE);
        }
        super.onProgressChanged(view, newProgress);
    }

}
