package cn.yin.com.api;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.yin.com.MyApplication;
import cn.yin.com.utils.NetUtils;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 79859 on 2018/5/9.
 */

public class GankApiRetrofit {

      public static GankApi gankApi;
      public static final String GANK_API_URL="http://gank.io/api/";

    public GankApi getGankApiObject(){
        return gankApi;
    }

    GankApiRetrofit(){
        File httpCacheDirectory=new File(MyApplication.getContext().getCacheDir(),"GankCache");
        int cashSize=10*1024*1024;
        Cache  cache=new Cache(httpCacheDirectory,cashSize);
        OkHttpClient client=new OkHttpClient.Builder()
//                            .connectTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置连接超时时间
//                            .readTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置读取超时时间
//                            .writeTimeout(HttpConfig.HTTP_TIME, TimeUnit.SECONDS)//设置写入超时时间
                            .addInterceptor(CACHE_CONTROL_INTERCEPTOR)
                            .cache(cache)
                            .build();
        Retrofit retrofit=new Retrofit.Builder()
                          .baseUrl(GANK_API_URL)
                          .client(client)
                          .addConverterFactory(GsonConverterFactory.create()) //添加gson转换器
                          .addCallAdapterFactory(RxJavaCallAdapterFactory.create())//添加rxjava转换器
                          .build();
        gankApi=retrofit.create(GankApi.class);
    }

    /*
* okhttp的缓存由返回的header 来决定。如果服务器支持缓存的话返回的headers里面会有这一句
* */
    Interceptor CACHE_CONTROL_INTERCEPTOR = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            CacheControl.Builder cacheBuilder = new CacheControl.Builder();
            cacheBuilder.maxAge(0, TimeUnit.SECONDS);
            cacheBuilder.maxStale(365, TimeUnit.DAYS);
            CacheControl cacheControl = cacheBuilder.build();

            Request request = chain.request();
            if (!NetUtils.checkNetWorkIsAvailable(MyApplication.getContext())) {
                request = request.newBuilder()
                        .cacheControl(cacheControl)
                        .build();
            }
            Response originalResponse = chain.proceed(request);
            if (NetUtils.checkNetWorkIsAvailable(MyApplication.getContext())) {
                int maxAge = 0; // read from cache
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public ,max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        }
    };
}
