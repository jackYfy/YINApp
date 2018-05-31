package cn.yin.com;

import android.app.Application;

/**
 * Created by 79859 on 2018/5/3.
 */

public class MyApplication extends Application {

    public static MyApplication myApplication;

    public static Application getContext(){
        return myApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        FIR.init(this);
//        SharedPreferencesMgr.init(this, "derson");
        myApplication=this;
    }

}
