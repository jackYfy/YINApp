package cn.yin.com.interfaces;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import java.util.List;

import cn.yin.com.entity.Gank;
import cn.yin.com.entity.SelectDate;

/**
 * Created by 79859 on 2018/5/10.
 */

public interface TodayRecommInterface {

    //获取今天数据
    public void getToadyRecomm(List<Gank> listGank);

    //获取时间
    public void getSelectDate(SelectDate selectDate);

    //失败
    public void onError(Throwable throwable);

    interface AllClassInterface{
        public void initViewData(TabLayout tabLayout, List<Fragment> listFragment, List<String> listTitle);
    }

    interface ClassTabInterface{
        public void getTypeTabData(List<Gank> gankList,boolean isLast);
        public void error(Throwable throwable);
    }
}
