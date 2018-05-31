package cn.yin.com.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.yin.com.Presenter.MeiziPresenter;
import cn.yin.com.R;
import cn.yin.com.adapter.BaseRecyclerAdapter;
import cn.yin.com.adapter.SmartViewHolder;
import cn.yin.com.base.BaseActivityCopy;
import cn.yin.com.entity.FemaleCurrent;
import cn.yin.com.entity.Gank;
import cn.yin.com.interfaces.MeiziInterface;
import cn.yin.com.utils.ImageLoad;
import cn.yin.com.utils.ScreenUtils;

public class MeiziImgActivity extends BaseActivityCopy implements  MeiziInterface, OnRefreshListener, OnLoadmoreListener {

    @Bind(R.id.meizi_toolbar)
    Toolbar meizi_toolbar;
    @Bind(R.id.welfare_recyclerview)
    RecyclerView welfareRecyclerview;
    @Bind(R.id.complaint_srl)
    SmartRefreshLayout refreshLayout ;

    public MeiziPresenter meiziPresenter;
    public List<Gank> listGank;
    public int page=1;
    private BaseRecyclerAdapter<Gank> mAdapter;
    private List<Integer> listHeight;
    private int welfareCurrent=0;
    @Override
    public int bindLayout() {
        return R.layout.activity_meizi_img;
    }

    @Override
    public void initView(View view) {
        ImmersionBar.with(this)
                .titleBar(meizi_toolbar) //指定标题栏view
                .init();
        setBaseSupportActionBar(meizi_toolbar,"妹子图片");
//        EventBus.getDefault().register(this);
         setData();
    }

    private void setData() {
        listGank=new ArrayList<>();
        listHeight=new ArrayList<>();
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        welfareRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        meiziPresenter=new MeiziPresenter(getActivity());
        meiziPresenter.getMeizi(this,page);//获取数据
        welfareRecyclerview.setAdapter(mAdapter=new BaseRecyclerAdapter<Gank>(this.listGank,R.layout.item_welfare_view) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, Gank model, int position) {
                TextView date=holder.getView(R.id.welfare_date);
                ImageView itemimg=holder.getView(R.id.welfare_item);
                date.setText(model.desc);
                ViewGroup.LayoutParams lp = itemimg.getLayoutParams();
                lp.height = listHeight.get(position);
                itemimg.setLayoutParams(lp);
                ImageLoad.displayImage(model.url+"?ImageView2/0/w/"+
                        (ScreenUtils.getScreenWidthDp(getActivity())/2-350),itemimg);
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                Glide.with(getActivity()).load(listGank.get(position).url).asBitmap()  //无论资源是不是gif动画，都作为Bitmap对待。如果是gif动画会停在第一帧。
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)  //缓存原始数据
                        .into(new SimpleTarget<Bitmap>() {  //设置资源将被加载到的Target。
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                Intent intent=new Intent(getActivity(), PhotoActivity.class);
                                intent.putExtra("listGank",(Serializable)listGank);
                                intent.putExtra("position",position);
                                ActivityOptionsCompat compat=ActivityOptionsCompat.
                                        makeSceneTransitionAnimation(getActivity(),view,listGank.get(position)._id);
                                startActivity(intent,compat.toBundle());
                            }
                        });
            }
        });
                refreshLayout.setOnRefreshListener(this);
                refreshLayout.setOnLoadmoreListener(this);
    }

//接收数据
    @Override
    public void getWelfaceData(List<Gank> listGank, boolean isLast) {
        for(int i=0;i<listGank.size();i++){
            this.listGank.add(listGank.get(i));
            listHeight.add((int)(600+Math.random()*150));
        }
        if(page==1){
            mAdapter.refresh(listGank);//刷新
        }else if(page!=1){
            mAdapter.loadMore(listGank);//加载更多
        }
    }

    @Override
    public void error(Throwable e) {

    }
    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(1500);
        listGank.clear();
        meiziPresenter.getMeizi(this,page=1);
        refreshLayout.resetNoMoreData();//恢复上拉状态
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        meiziPresenter.getMeizi(this,page);
        refreshlayout.finishLoadmore();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void updateCurrent(FemaleCurrent femaleCurrent){
//        welfareCurrent=femaleCurrent.current;
//    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


}
