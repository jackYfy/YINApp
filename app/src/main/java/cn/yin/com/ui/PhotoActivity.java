package cn.yin.com.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.yin.com.Presenter.PhotoPersenter;
import cn.yin.com.R;
import cn.yin.com.adapter.PhotoFragmentAdapter;
import cn.yin.com.base.ToolbarBaseActivity;
import cn.yin.com.entity.FemaleCurrent;
import cn.yin.com.entity.Gank;
import cn.yin.com.view.PhotoViewPager;

public class PhotoActivity extends ToolbarBaseActivity implements ViewPager.OnPageChangeListener {


    @Bind(R.id.photo_viewpager)
    public PhotoViewPager photoViewpager;

    private PhotoFragmentAdapter photoFragmentAdapter;
    private FemaleCurrent femaleCurrent;
    private PhotoPersenter photoPersenter;

    private List<Gank> listGank;
    private int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarTintColor(Color.parseColor("#000000"));
        supportPostponeEnterTransition();
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);
        photoToolbar=(Toolbar)findViewById(R.id.photo_toolbar);
        init();
        initView();
    }

    private void init() {
        setBaseSupportActionBar(photoToolbar);
        photoPersenter=new PhotoPersenter(this);
        femaleCurrent=new FemaleCurrent(0);
        listGank=(List<Gank>) getIntent().getSerializableExtra("listGank");
        position=getIntent().getIntExtra("position",0);
        photoViewpager.setOnPageChangeListener(this);
        initToolbarTitle();
    }

    private void initToolbarTitle() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                photoToolbar.setTitle(listGank.get(position).desc);
            }
        },300);
    }

    private void initView() {
        photoFragmentAdapter=new PhotoFragmentAdapter(getSupportFragmentManager(),listGank,position,this);
        photoViewpager.setAdapter(photoFragmentAdapter);
        photoViewpager.setCurrentItem(position);
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setSharedElementsUseOverlay(false);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }
    @Override
    public void onPageSelected(int position) {
        photoToolbar.setTitle(listGank.get(position).desc);
        femaleCurrent.current=position;
//        EventBus.getDefault().post(femaleCurrent);
        if(position==1||position==0){
            hideOrShowToolbar();
        }
    }
    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.photomenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_saveimg:
                    photoPersenter.savaImage(this,listGank.get(femaleCurrent.current).url);
                break;
            case R.id.action_shareimg:
//                    photoPersenter.sharedImage(this,getString(R.string.sharedTitle),"",
//                            listGank.get(femaleCurrent.current).url);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
