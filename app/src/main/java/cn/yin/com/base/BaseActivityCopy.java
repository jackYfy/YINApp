package cn.yin.com.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import cn.yin.com.R;
import cn.yin.com.listenr.IActivity;
import cn.yin.com.tools.ActivityManager;
import cn.yin.com.utils.PreUtils;
import cn.yin.com.utils.StyleColorUtils;
import cn.yin.com.utils.Theme;
import cn.yin.com.utils.ToastUtil;

/**
 * Created by 79859 on 2018/5/4.
 *
 * 这个BaseActivity是在activity里设置toolbar
 */

public abstract class BaseActivityCopy extends AppCompatActivity implements IActivity {

    /**
     * 当前Activity渲染的视图View
     **/
    private View activityView = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    public WeakReference<Activity> context = null;
    public Theme theme;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme();
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init(); //初始化，默认透明状态栏和黑色导航栏
        // 设置渲染视图View
        activityView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(activityView);
        ButterKnife.bind(this);
        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        ActivityManager.getAppManager().addActivity(this);
        // 初始化控件
        initView(activityView);

    }

    public  void setTheme() {
        theme= PreUtils.getCurrentTheme(this);
        PreUtils.changeTheme(this, StyleColorUtils.getStyleColorId(theme.name()),theme.name());
    }

    public void setBaseSupportActionBar(Toolbar toolbar,String title){
        toolbar.setTitle(title);//设置主标题
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    public void showToast(String msg){
        ToastUtil.showToast(this,msg);
    }

    public void showSnackbar(String message){
        Snackbar.make(getCurrentFocus(),message,Snackbar.LENGTH_SHORT).show();
    }

    public Activity getActivity() {
        return context.get();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 将当前Activity移除栈
        ButterKnife.unbind(activityView);
        ImmersionBar.with(this).destroy(); //不调用该方法，如果界面bar发生改变，在不关闭app的情况下，退出此界面再进入将记忆最后一次bar改变的状态
        ActivityManager.getAppManager().finishActivity(this);
    }
}
