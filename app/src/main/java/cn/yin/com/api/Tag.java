package cn.yin.com.api;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 79859 on 2018/5/9.
 */

public class Tag {

    //所有tab的类型
    public static final String[] Aategory= new String[]{
            "all","Android","iOS","休息视频","拓展资源","前端","瞎推荐","App"};

    public static String GankImg;

    @StringDef
    @Retention(RetentionPolicy.SOURCE) public @interface Category {
        String ANDROID = "Android";
        String IOS = "iOS";
        String WEB = "前端";
        String SUGGEST = "瞎推荐";
        String MEIZI = "福利";
        String VIDEO = "休息视频";
        String EXTEND = "拓展资源";
        String APP = "App";
        String ALL = "all";
    }
}
