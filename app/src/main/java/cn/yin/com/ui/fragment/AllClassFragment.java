package cn.yin.com.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.yin.com.Presenter.AllClassFragmentPersenter;
import cn.yin.com.R;
import cn.yin.com.adapter.BaseRecyclerAdapter;
import cn.yin.com.adapter.SmartViewHolder;
import cn.yin.com.api.GankApiFactory;
import cn.yin.com.entity.Gank;
import cn.yin.com.entity.ImageType;
import cn.yin.com.interfaces.IImageInfo;
import cn.yin.com.interfaces.TodayRecommInterface;
import cn.yin.com.ui.WebContentActivity;
import cn.yin.com.utils.ImageLoad;

public class AllClassFragment extends Fragment implements TodayRecommInterface.ClassTabInterface, OnRefreshListener, OnLoadmoreListener {

    @Bind(R.id.classFragment_srl)
    SmartRefreshLayout refreshLayout;
    @Bind(R.id.classFragmente_recyclerview)
    RecyclerView recyclerview;

    private AllClassFragmentPersenter classTabPersenter;
    private BaseRecyclerAdapter<Gank> mAdapter;
    private int page=1;
    private List<Gank> tabListGank;
    private boolean isLast=false;
    private String tabTitle;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_all_class,container,false);
        ButterKnife.bind(this,view);
        tabTitle=getArguments().getString("TabTitle");
        init();
        return view;
    }

    private void init() {
        tabListGank=new ArrayList<>();
        refreshLayout.setEnableFooterFollowWhenLoadFinished(true);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(1,StaggeredGridLayoutManager.VERTICAL));
        classTabPersenter=new AllClassFragmentPersenter(getActivity());
        classTabPersenter.getClassTabData(this,tabTitle,page);
        recyclerview.setAdapter(mAdapter=new BaseRecyclerAdapter<Gank>(this.tabListGank,R.layout.item_today_main) {
            @Override
            protected void onBindViewHolder(SmartViewHolder holder, final Gank gank, int position) {
               TextView itemText =holder.getView(R.id.main_item_content);
              final ImageView itemImage =holder.getView(R.id.main_item_image);
               ImageView collected_content=holder.getView(R.id.collected_content);
               TextView who_content=holder.getView(R.id.who_content);
                itemText.setText(gank.desc);
                who_content.setText(gank.who);
                if(gank.images!=null){
                    if(!gank.hasLoadImage){
                        //后缀添加？imageInfo 获取的是图片的信息，用于判断是什么格式的图片，GIF过滤掉
                        GankApiFactory.GankApiImageInfo(gank.images.get(0)+ "?imageInfo", new IImageInfo() {
                            @Override
                            public void error() {}
                            @Override
                            public void seccess(ImageType imageType) {
                                if (!imageType.format.equals("gif")) {
                                    itemImage.setVisibility(View.VISIBLE);
                                    ImageLoad.displayImage(gank.images.get(0),itemImage);
                                    gank.hasLoadImage=true;
                                }
                            }
                        });
                    }else{
                        itemImage.setVisibility(View.VISIBLE);
                        ImageLoad.displayImage(gank.images.get(0),itemImage);
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getActivity(),WebContentActivity.class);
                intent.putExtra("collectTag",false);
                intent.putExtra("gank",tabListGank.get(position));
                startActivity(intent);
            }
        });
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setOnLoadmoreListener(this);
    }


    @Override
    public void getTypeTabData(List<Gank> gankList, boolean isLast) {
        this.isLast=isLast;
        tabListGank.addAll(gankList);
        if(page==1){
            mAdapter.refresh(tabListGank);//刷新
        }else if(page!=1){
            mAdapter.loadMore(tabListGank);//加载更多
        }
    }

    @Override
    public void error(Throwable throwable) {

    }

    @Override
    public void onRefresh(RefreshLayout refreshlayout) {
        refreshlayout.finishRefresh(1500);
        tabListGank.clear();
        classTabPersenter.getClassTabData(this,tabTitle,page=1);
        refreshLayout.resetNoMoreData();//恢复上拉状态
    }

    @Override
    public void onLoadmore(RefreshLayout refreshlayout) {
        page++;
        classTabPersenter.getClassTabData(this,tabTitle,page);
        refreshlayout.finishLoadmore();
    }
}
