package cn.yin.com.ui;

import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.gyf.barlibrary.ImmersionBar;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.yin.com.Presenter.ThemePersenter;
import cn.yin.com.R;
import cn.yin.com.adapter.ThemeAdapter;
import cn.yin.com.base.BaseActivity;
import cn.yin.com.entity.ThemeModel;
import cn.yin.com.listenr.OnClickLintener;
import cn.yin.com.utils.PreUtils;
import cn.yin.com.utils.Theme;

public class ThemeActivity extends BaseActivity implements OnClickLintener {
    @Bind(R.id.theme_recyclerview)
    RecyclerView themeRecyclerview;
    @Bind(R.id.theme_header)
    RelativeLayout themeHeader;
    @Bind(R.id.theme_statusbar)
    RelativeLayout themeStatusbar;
    @Bind(R.id.fab_theme_bg)
    RelativeLayout fabThemeBg;

    public ThemeAdapter themeAdapter;
    private ThemePersenter themePersenter;
    private int currentPosition;
    private List<ThemeModel> listTheme=new ArrayList<>();

    private int[] themeColor=new int[]{R.color.colorBluePrimary,R.color.colorRedPrimary
            ,R.color.colorBrownPrimary,R.color.colorGreenPrimary,R.color.colorPurplePrimary
            ,R.color.colorTealPrimary,R.color.colorPinkPrimary,R.color.colorDeepPurplePrimary
            ,R.color.colorOrangePrimary,R.color.colorIndigoPrimary,R.color.colorCyanPrimary
            ,R.color.colorLightGreenPrimary,R.color.colorLimePrimary,R.color.colorDeepOrangePrimary
            ,R.color.colorBlueGreyPrimary};

    @Override
    public int bindLayout() {
        return R.layout.activity_theme;
    }

    @Override
    public void initView(View view) {
        setBackup();
        setTitle("更换主题");
        init();
        initData();
    }

    private void init() {
        themePersenter=new ThemePersenter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        themeRecyclerview.setLayoutManager(linearLayoutManager);
        themeAdapter=new ThemeAdapter(this,listTheme);
        themeAdapter.setOnClickLintener(this);
        themeRecyclerview.setAdapter(themeAdapter);
    }

    private void initData() {
        themePersenter.getListColor(themeColor,listTheme,theme.name().toString());
        themeAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.theme_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_sure:
                PreUtils.changeTheme(ThemeActivity.this,themePersenter.getThemeStyle(
                        listTheme.get(currentPosition).color),
                        themePersenter.getTheme(listTheme.get(currentPosition).color));
                PreUtils.changeColorImpl(ThemeActivity.this,ThemeActivity.this.getTheme());
                EventBus.getDefault().post(listTheme.get(currentPosition));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v, int position) {
        currentPosition=position;
        themeHeader.setBackgroundColor(getResources().getColor(listTheme.get(position).color));
        themeStatusbar.setBackgroundColor(getResources().getColor(listTheme.get(position).color));
        ((GradientDrawable)fabThemeBg.getBackground()).setColor(
                getResources().getColor(listTheme.get(position).color));
        themePersenter.selectListColor(position,listTheme);
        themeAdapter.notifyDataSetChanged();
    }
}
