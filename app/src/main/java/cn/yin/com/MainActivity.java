package cn.yin.com;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gyf.barlibrary.ImmersionBar;
import com.nightonke.boommenu.BoomMenuButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.yin.com.Presenter.TodayRecommPresenter;
import cn.yin.com.adapter.TodayRecommAdapter;
import cn.yin.com.base.BaseActivityCopy;
import cn.yin.com.entity.DateResult;
import cn.yin.com.entity.Gank;
import cn.yin.com.entity.SelectDate;
import cn.yin.com.entity.ThemeModel;
import cn.yin.com.interfaces.TodayRecommInterface;
import cn.yin.com.listenr.OnClickLintener;
import cn.yin.com.tools.PermissionUtil;
import cn.yin.com.ui.AllClassActivity;
import cn.yin.com.ui.MeiziImgActivity;
import cn.yin.com.ui.SelectDateActivity;
import cn.yin.com.ui.ThemeActivity;
import cn.yin.com.ui.WebContentActivity;
import cn.yin.com.utils.ToastUtil;
import cn.yin.com.widget.PromptDialog;

import static cn.yin.com.tools.PermissionUtil.SDK_PERMISSION_REQUEST;

public class MainActivity extends BaseActivityCopy implements NavigationView.OnNavigationItemSelectedListener, TodayRecommInterface, OnClickLintener {

    @Bind(R.id.tool_bar)
    Toolbar tool_bar;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawer_layout;
    @Bind(R.id.nav_view)
    NavigationView nav_view;
    @Bind(R.id.ctb)
    CollapsingToolbarLayout ctb;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.today_recyclerview)
    RecyclerView todayRecyclerview;
    @Bind(R.id.today_fab)
     BoomMenuButton boomMenuButton;
    @Bind(R.id.image)
    ImageView image;
    @Bind(R.id.date)
    TextView date;

    private ImageView imageView;
    private View headerView;
    public TodayRecommPresenter todayRecommPresenter;
    public TodayRecommAdapter todayRecommAdapter;
    public  List<Gank> listGank=null;
    public DateResult dateResult;
    private PermissionUtil mPermissionUtil;
    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(View view) {
        ImmersionBar.with(this)
                .titleBar(tool_bar) //指定标题栏view
                .init();
        mPermissionUtil=new PermissionUtil(this);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mPermissionUtil.checkPermissions(PermissionUtil.needPermissions);
        }
        setDrawer();
        //注册EventBus
        EventBus.getDefault().register(this);
        ininData();
        //分支测试第二次

    }

    private void setDrawer() {
        //获取头布局文件
        headerView = nav_view.getHeaderView(0);
        imageView= (ImageView) headerView.findViewById(R.id.imageView);
        tool_bar.setTitle(" ");//设置主标题
        setSupportActionBar(tool_bar);
        tool_bar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer_layout, tool_bar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                // 得到contentView
                View content = drawer_layout.getChildAt(0);
                int offset = (int) (drawerView.getWidth() * slideOffset);
                content.setTranslationX(offset);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
            }
            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
            }
        };
        toggle.syncState();
        drawer_layout.setDrawerListener(toggle);
        //        修改左侧栏开关图标
        toggle.setDrawerIndicatorEnabled(false);
        tool_bar.setNavigationIcon(R.mipmap.user_center);
        tool_bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer_layout.openDrawer(GravityCompat.START);
            }
        });
        nav_view.setNavigationItemSelectedListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SelectDateActivity.class));
            }
        });

    }

    private void ininData() {
        todayRecyclerview.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        todayRecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        todayRecommPresenter=new TodayRecommPresenter(getActivity());
        dateResult=todayRecommPresenter.getTodayRecomm();//先获取日期
        todayRecommPresenter.getTodayData(this,dateResult.year,dateResult.month,dateResult.day);//获取本日数据，返回到getToadyRecomm（）；
        listGank=new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCollapsingToolbarLayoutTheme(ThemeModel themeModel){
        ctb.setStatusBarScrimColor(getResources().getColor(themeModel.color));
        ctb.setContentScrimColor(getResources().getColor(themeModel.color));
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(themeModel.color)));
        headerView.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(themeModel.color)));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void UpdateTodayData(DateResult dateResult){
        if(dateResult.day!=null){
            this.dateResult=dateResult;
            todayRecommPresenter.getTodayData(this,dateResult.year,dateResult.month,dateResult.day);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_1) {
            showToast("分享");
        }if(id == R.id.action_2){
            showToast("关于");
        }if(id == R.id.action_3){
            showToast("版本");
        }if(id == R.id.action_4){
            showToast("时间");
        }
        return super.onOptionsItemSelected(item);
    }
    //显示toolbar menu的icon，不添加，看不到图标
    @SuppressLint("RestrictedApi")
    @Override
    protected boolean onPrepareOptionsPanel(View view, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equals("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod(
                            "setOptionalIconsVisible", Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    Log.e(getClass().getSimpleName(), "onMenuOpened...unable to set icons for overflow menu", e);
                }
            }
        }
        return super.onPrepareOptionsPanel(view, menu);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_tuijian) {
            Toast.makeText(getApplicationContext(), "第一", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_all) {
            startActivity(new Intent(this,AllClassActivity.class));
        } else if (id == R.id.nav_meizi) {
            startActivity(new Intent(this,MeiziImgActivity.class));
        } else if (id == R.id.nav_collect) {
            Toast.makeText(getApplicationContext(), "第4", Toast.LENGTH_SHORT).show();
        }else if (id == R.id.nav_theme) {
            startActivity(new Intent(this,ThemeActivity.class));
        }else if (id == R.id.nav_about) {
            Toast.makeText(getApplicationContext(), "第6", Toast.LENGTH_SHORT).show();
        }
        drawer_layout.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);//取消EventBus
    }
//接受查询的数据
    @Override
    public void getToadyRecomm(List<Gank> listGanks) {
        if(todayRecommPresenter.initData(MainActivity.this,this.listGank,listGanks,
                todayRecommPresenter,image,date,dateResult)){
            return;
        }
        this.listGank=todayRecommPresenter.addHeadItem(this.listGank);//添加头部
        todayRecommAdapter=new TodayRecommAdapter(getActivity(),this.listGank);
        todayRecommAdapter.setOnClickLintener(this);
        todayRecyclerview.setAdapter(todayRecommAdapter);
    }
//    获取发过干货日期
    @Override
    public void getSelectDate(SelectDate selectDate) {
        dateResult=todayRecommPresenter.formatStringDate(selectDate.results.get(0));
        todayRecommPresenter.getTodayData(this,dateResult.year,dateResult.month,dateResult.day);//再次根据日期查询
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onClick(View v, int position) {
        Intent intent=new Intent(getActivity(),WebContentActivity.class);
        intent.putExtra("collectTag",false);
        intent.putExtra("gank",listGank.get(position));
        startActivity(intent);
    }

    //android 6.0及以上版本需要手动开启存储权限
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        if (requestCode == SDK_PERMISSION_REQUEST) {
            if (mPermissionUtil.verifyPermissions(grantResults)) {
                ToastUtil.showToast(this,"权限通过");
            }else{
                Toast.makeText(getApplicationContext(),"有部分权限已被拒绝。",Toast.LENGTH_LONG).show();
                //用户不同意，向用户展示该权限作用
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    new PromptDialog(this)
                            .setDialogType(PromptDialog.DIALOG_TYPE_WARNING)
                            .setAnimationEnable(true)
                            .setTitleText("提示")
                            .setContentText("安卓手机 6.0及以上系统"+"\n"+"应用更新需要赋予访问存储的权限,保存照片需要存储权限")
                            .setPositiveListener("关闭窗口退出", new PromptDialog.OnPositiveListener() {
                                @Override
                                public void onClick(PromptDialog dialog) {
//                                    mPermissionUtil.startAppSettings();
//                                    finish();
                                    dialog.dismiss();
                                }
                            }).show();
                }
            }
        }

    }
}
