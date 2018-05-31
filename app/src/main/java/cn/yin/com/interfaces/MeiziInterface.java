package cn.yin.com.interfaces;

import java.util.List;

import cn.yin.com.entity.Gank;

/**
 * Created by 79859 on 2018/5/11.
 */

public interface MeiziInterface {
    public void getWelfaceData(List<Gank> listGank, boolean isLast);
    public void error(Throwable e);
}
