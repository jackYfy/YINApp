package cn.yin.com.api;

import cn.yin.com.entity.ImageType;
import cn.yin.com.interfaces.IImageInfo;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 79859 on 2018/5/9.
 */

public class GankApiFactory {

    public static final Object object=new Object();
    public  static GankApi gankApi=null;

    public static GankApi getGankApi(){
        synchronized (object){
            if(gankApi==null){
                gankApi=new GankApiRetrofit().getGankApiObject();
            }
            return gankApi;
        }
    }

    public static void GankApiImageInfo(String imageUrl,final IImageInfo imageInfo){
          getGankApi().GankImageInfo(imageUrl)
                  .subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<ImageType>() {
                      @Override
                      public void onCompleted() {}//事件队列完结。RxJava 不仅把每个事件单独处理，还会把它们看做一个队列。RxJava 规定，当不会再有新的 onNext() 发出时，需要触发 onCompleted() 方法作为标志。
                      @Override
                      public void onError(Throwable e) {}//事件队列异常。在事件处理过程中出异常时，onError() 会被触发，同时队列自动终止，不允许再有事件发出
                      @Override
                      public void onNext(ImageType imageType) {
                            imageInfo.seccess(imageType);
                      }
                  });

    }
}
