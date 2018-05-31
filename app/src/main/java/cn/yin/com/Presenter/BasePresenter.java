package cn.yin.com.Presenter;

import android.content.Context;

import cn.yin.com.api.GankApi;
import cn.yin.com.api.GankApiFactory;


/**
 * Created by 79859 on 2018/5/3.
 */

public class BasePresenter {
    public Context context;
    public static final GankApi gankApi= GankApiFactory.getGankApi();

    public BasePresenter(Context context){
        this.context=context;
    }

}
