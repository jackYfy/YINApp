package cn.yin.com.Presenter;

import android.content.Context;

import cn.yin.com.entity.GankModel;
import cn.yin.com.interfaces.MeiziInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 79859 on 2018/5/11.
 */

public class MeiziPresenter extends BasePresenter {
    public MeiziPresenter(Context context) {
        super(context);
    }
    private boolean isLast=false;
    public void getMeizi(final MeiziInterface meiziInterface, int page){
        gankApi.getGankData("福利","6",page+"")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GankModel>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {
                        meiziInterface.error(e);
                    }
                    @Override
                    public void onNext(GankModel gankModel) {
                        if(gankModel.results==null){
                            isLast=true;
                        }
                        meiziInterface.getWelfaceData(gankModel.results,isLast);
                    }
                });
    }
}
