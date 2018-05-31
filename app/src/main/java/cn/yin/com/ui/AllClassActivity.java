package cn.yin.com.ui;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;


import com.gyf.barlibrary.ImmersionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.yin.com.Presenter.AllClassPersenter;
import cn.yin.com.R;
import cn.yin.com.adapter.AllTabFragmentAdapter;
import cn.yin.com.api.Tag;
import cn.yin.com.base.BaseActivity;
import cn.yin.com.base.BaseActivityCopy;
import cn.yin.com.interfaces.TodayRecommInterface;
import cn.yin.com.utils.SPUtils;

public class AllClassActivity extends BaseActivityCopy implements TodayRecommInterface.AllClassInterface {

    @Bind(R.id.all_toolbar)
    Toolbar alltiilbar;
    @Bind(R.id.classify_tablayout)
     TabLayout tabLayout;
    @Bind(R.id.classify_viewpager)
    ViewPager viewpager;
    @Bind(R.id.sort_tab)
    RelativeLayout sortTab;

    private String[] tabTitle;
    private List<Fragment> listFragment=new ArrayList<>();
    private AllClassPersenter allClassPersenter;
    private AllTabFragmentAdapter adapter;
    @Override
    public int bindLayout() {
        return R.layout.activity_all_class;
    }

    @Override
    public void initView(View view) {
        ImmersionBar.with(this)
                .titleBar(alltiilbar) //指定标题栏view
                .init();
        setBaseSupportActionBar(alltiilbar,"分类推荐");
        init();
    }

    private void init() {
        tabTitle= Tag.Aategory;
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        allClassPersenter=new AllClassPersenter(this);
        String tabData= SPUtils.getString(this,"TabMenu","TabGson");
        if(tabData!=null){
            allClassPersenter.initAllTabData(this,tabData,tabLayout,listFragment);
        }else {
            allClassPersenter.initNotAllTabData(this,tabLayout,listFragment);
        }
    }


    @Override
    public void initViewData(TabLayout tabLayout, List<Fragment> listFragment, List<String> listTitle) {
        adapter=new AllTabFragmentAdapter(this.getSupportFragmentManager(),listFragment,listTitle);
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
    }
}
