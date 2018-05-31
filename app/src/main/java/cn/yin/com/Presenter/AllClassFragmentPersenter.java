package cn.yin.com.Presenter;

import android.content.Context;

import cn.yin.com.entity.GankModel;
import cn.yin.com.interfaces.TodayRecommInterface;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 79859 on 2018/5/22.
 */

public class AllClassFragmentPersenter extends BasePresenter {
    public AllClassFragmentPersenter(Context context) {
        super(context);
    }

    private boolean isLast=false;

    public void getClassTabData(final TodayRecommInterface.ClassTabInterface classTabInterface, String type, int page){
          gankApi.getGankData(type,"10", String.valueOf(page))
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<GankModel>() {
                      @Override
                      public void onCompleted() {
                      }
                      @Override
                      public void onError(Throwable e) {
                          classTabInterface.error(e);
                      }
                      @Override
                      public void onNext(GankModel gankModel) {
                          if(gankModel.results==null){
                              isLast=true;
                          }
                          classTabInterface.getTypeTabData(gankModel.results,isLast);
                      }
                  });
    }
}
