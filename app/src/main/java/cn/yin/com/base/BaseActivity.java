package cn.yin.com.base;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by 79859 on 2018/5/3.
 * 这个BaseActivity不用在activity里设置toolbar
 */

public abstract class BaseActivity extends AppCompatActivity implements IActivity {

    /**
     * 当前Activity渲染的视图View
     **/
    private View activityView = null;
    /**
     * 当前Activity的弱引用，防止内存泄露
     **/
    public WeakReference<Activity> context = null;
    public Theme theme;
    private Toolbar toolbar = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme();//更换主题颜色必须在onCreate之前
        super.onCreate(savedInstanceState);
        // 设置渲染视图View
        activityView = LayoutInflater.from(this).inflate(bindLayout(), null);
        setContentView(activityView);
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setBaseSupportActionBar(toolbar);
        // 将当前Activity压入栈
        context = new WeakReference<Activity>(this);
        ActivityManager.getAppManager().addActivity(this);
        // 初始化控件
        initView(activityView);

        //免去在每个布局文件写 android:fitsSystemWindows="true"
        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }
    }

    private void setTheme() {
        theme= PreUtils.getCurrentTheme(this);
        PreUtils.changeTheme(this, StyleColorUtils.getStyleColorId(theme.name()),theme.name());
    }

    public void setBaseSupportActionBar(Toolbar toolbar){
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * 设置返回为关闭当前页面
     */
    public void setBackup() {
        setNavigation(R.mipmap.back_img1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    /**
     * 设置返回为关闭当前页面
     */
    public void setBackup(View.OnClickListener listener) {
        setNavigation(R.mipmap.back_img1, listener);
    }
    /**
     * 设置标题名称，处理居中
     *
     * @param charSequence
     */
    public void setTitle(CharSequence charSequence) {
        ((TextView) findViewById(R.id.toolbar_title)).setText(charSequence);
    }

    /**
     * 设置navigation
     */
    public void setNavigation(int resId, View.OnClickListener listener) {
        toolbar.setNavigationIcon(resId);
        toolbar.setNavigationOnClickListener(listener);
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
        ActivityManager.getAppManager().finishActivity(this);
    }
}

