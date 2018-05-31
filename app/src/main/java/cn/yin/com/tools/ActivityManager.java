package cn.yin.com.tools;
import android.app.Activity;
import android.content.Context;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/**
 *
 */
public class ActivityManager {

    public  static LinkedList<Activity> activityStack;
    private static ActivityManager instance;
    /***
     * 寄存整个应用Activity
     **/
    private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();
    private ActivityManager() {
    }

    /**
     * 单一实例
     */
    public static ActivityManager getAppManager() {
        if (instance == null) {
            instance = new ActivityManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new LinkedList<Activity>();
        }
        activityStack.add(activity);
    }
    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.getLast();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.getLast();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (activityStack != null) {
//                activityStack.remove(activity); //不能用移除，会报java.util.ConcurrentModificationException
                if (!activity.isFinishing())
                    activity.finish();
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    public void finishOther(Activity activity) {
        for (int i = 0; i < activityStack.size(); i++) {
            if (null != activityStack.get(i) && activityStack.get(i)!=activity) {
                activityStack.get(i).finish();
                activityStack.remove(i);
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0; i <activityStack.size(); i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 退出应用程序
     */
   /* public void AppExit(Context context) {
        try {
            // finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            // activityMgr.restartPackage(context.getPackageName());
            // Log.d("TAG", "AppExit：" + context.getPackageName());
            // activityMgr.killBackgroundProcesses(context.getPackageName());
            // System.exit(0);
            List<android.app.ActivityManager.RunningAppProcessInfo> list = activityMgr.getRunningAppProcesses();
            StringBuffer sb = new StringBuffer();
            for (android.app.ActivityManager.RunningAppProcessInfo info : list) {
                if (info.pid != android.os.Process.myPid()) {
                    sb.append(info.pid + "=" + info.processName + ";");
                    android.os.Process.killProcess(info.pid);
                }
            }
            Log.d("TAG", "sb:" + sb.toString());
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
        }
    }*/

    public int ActivityStackSize() {
        return activityStack == null ? 0 : activityStack.size();
    }

    /**
     * 返回app运行状态
     * 1:程序在前台运行
     * 2:程序在后台运行
     * 3:程序未启动
     * 注意：需要配置权限<uses-permission android:name="android.permission.GET_TASKS" />
     */
    public int getAppSatus(Context context, String pageName) {

        android.app.ActivityManager am = (android.app.ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<android.app.ActivityManager.RunningTaskInfo> list = am.getRunningTasks(20);

        //判断程序是否在栈顶
        if (list.get(0).topActivity.getPackageName().equals(pageName)) {
            return 1;
        } else {
            //判断程序是否在栈里
            for (android.app.ActivityManager.RunningTaskInfo info : list) {
                if (info.topActivity.getPackageName().equals(pageName)) {
                    return 2;
                }
            }
            return 3;//栈里找不到，返回3
        }
    }
}
