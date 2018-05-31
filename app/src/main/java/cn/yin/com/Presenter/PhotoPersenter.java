package cn.yin.com.Presenter;

import android.content.Context;

import org.greenrobot.eventbus.Subscribe;

import java.util.concurrent.ExecutionException;

import cn.yin.com.utils.PictureUtils;
import cn.yin.com.utils.ToastUtil;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.observers.Observers;
import rx.schedulers.Schedulers;

/**
 * Created by 79859 on 2018/5/31.
 */

public class PhotoPersenter extends BasePresenter {
    public PhotoPersenter(Context context) {
        super(context);
    }

    public void savaImage(final Context context,final String url){
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                try {
                    subscriber.onNext(PictureUtils.saveBitmapFromUrl(context,url));
                } catch (Exception e) {
                    subscriber.onNext(false);
                }
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {}
                    @Override
                    public void onNext(Boolean aBoolean) {
                        ToastUtil.showToast(context,"保存成功");
                    }
                });
    }

}
