package cn.yin.com.api;

import cn.yin.com.entity.GankModel;
import cn.yin.com.entity.ImageType;
import cn.yin.com.entity.SelectDate;
import cn.yin.com.entity.TodayGank;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;
import rx.observers.Observers;

/**
 * Created by 79859 on 2018/5/9.
 * 注意，这里是interface不是class，所以我们是无法直接调用该方法，我们需要用Retrofit创建一个GankApi的代理对象。（见GankApiRetrofit.class）
 */

public interface GankApi {
    //每日数据： http://gank.io/api/day/年/月/日
    @GET("day/{year}/{month}/{day}")
    Observable<TodayGank> getGankDataOfDate(@Path("year") String year, @Path("month") String month, @Path("day") String day);


    @GET
    Observable<ImageType> GankImageInfo(@Url String url);

    //  获取发过干货日期接口
    @GET("day/history")
    Observable<SelectDate> getAllSelectDate();

    //分类数据: http://gank.io/api/data/数据类型/请求个数/第几页
    //数据类型： 福利 | Android | iOS | 休息视频 | 拓展资源 | 前端 | all
    //请求个数： 数字，大于0
    //第几页：数字，大于0
    @GET("data/{type}/{num}/{page}")
    Observable<GankModel> getGankData(@Path("type") String type,
                                      @Path("num") String num, @Path("page") String page);
}
