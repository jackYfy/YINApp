package cn.yin.com.Presenter;

import android.content.Context;
import android.support.design.widget.CollapsingToolbarLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.yin.com.MainActivity;
import cn.yin.com.api.Tag;
import cn.yin.com.entity.DateResult;
import cn.yin.com.entity.Gank;
import cn.yin.com.entity.SelectDate;
import cn.yin.com.entity.TodayGank;
import cn.yin.com.interfaces.TodayRecommInterface;
import cn.yin.com.utils.ImageLoad;
import cn.yin.com.utils.SPUtils;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by 79859 on 2018/5/10.
 */

public class TodayRecommPresenter extends BasePresenter {


    public TodayRecommPresenter(Context context) {
        super(context);
    }

  public DateResult getTodayRecomm(){
      Calendar calendar=Calendar.getInstance();
      String year=(calendar.get(Calendar.YEAR)+1)+"";
      int temMonth=calendar.get(Calendar.MONTH);
      int temDay=calendar.get(Calendar.DAY_OF_MONTH);
      String month=temMonth>10?temMonth+"":"0"+temMonth;
      String day=temDay>10?temDay+"":"0"+temDay;
      return new DateResult(year,month,day);
  }

  public void getTodayData(final TodayRecommInterface todayRecommInterface,String year,String month,String day){
         gankApi.getGankDataOfDate(year,month,day)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .map(new Func1<TodayGank, List<Gank>>() {
                     @Override
                     public List<Gank> call(TodayGank todayGank) {
                         SPUtils.put(context,SPUtils.TODAYDATA,"today",new Gson().toJson(todayGank));
                         return addGankItem(todayGank);
                     }
                 })
                 .subscribe(new Subscriber<List<Gank>>() {
                     @Override
                     public void onCompleted() {}
                     @Override
                     public void onError(Throwable e) {todayRecommInterface.onError(e);}
                     @Override
                     public void onNext(List<Gank> ganks) {
                          if (ganks==null){
                              TodayGank todayGank=new Gson().fromJson(
                                      SPUtils.getSharedPreference(SPUtils.TODAYDATA,context).getString("today",""),TodayGank.class);
                              ganks=addGankItem(todayGank);
                          }
                          todayRecommInterface.getToadyRecomm(ganks);
                     }
                 });
  }

    private List<Gank> addGankItem(TodayGank todayGank) {
      List<Gank> listGank=new ArrayList<>();
        if(todayGank.results.androidList!=null)
            listGank.addAll(todayGank.results.androidList);
        if (todayGank.results.iOSList!=null)
            listGank.addAll(todayGank.results.iOSList);
        if (todayGank.results.休息视频List!=null)
            listGank.addAll(todayGank.results.休息视频List);
        if (todayGank.results.妹纸List!=null)
            listGank.addAll(todayGank.results.妹纸List);
        if (todayGank.results.拓展资源List!=null)
            listGank.addAll(todayGank.results.拓展资源List);
        if (todayGank.results.瞎推荐List!=null)
            listGank.addAll(todayGank.results.瞎推荐List);
        if (todayGank.results.appList!=null)
            listGank.addAll(todayGank.results.appList);
        return listGank;
    }

    public boolean initData(MainActivity act, List<Gank> listGank, List<Gank> listGanks,
                            TodayRecommPresenter todayRecommPresenter, ImageView todayImage,
                            TextView date, DateResult  dateResult){
      if (listGanks.size()==0){//如果今日数据为空
          todayRecommPresenter.getSelectDate(act); //获取发过干货日期
          return true;
      }
      if (listGank!=null)
          listGank.clear();
      listGank.addAll(listGanks);
      for(int i=0;i<listGank.size();i++){
          if(listGank.get(i).type.equals("福利")){
              Tag.GankImg=listGank.get(i).url;
              ImageLoad.displayImage(listGank.get(i).url,todayImage);
          }
      }
        date.setText(dateResult.year+"年"+dateResult.month+"月"+dateResult.day+"日");
      return false;
    }
//获取发过干货日期
    private void getSelectDate(final TodayRecommInterface todayRecommInterface) {
             gankApi.getAllSelectDate()
                     .subscribeOn(Schedulers.io())
                     .observeOn(AndroidSchedulers.mainThread())
                     .subscribe(new Subscriber<SelectDate>() {
                         @Override
                         public void onCompleted() {}
                         @Override
                         public void onError(Throwable e) {todayRecommInterface.onError(e);}
                         @Override
                         public void onNext(SelectDate selectDate) {
                               todayRecommInterface.getSelectDate(selectDate);//通过本接口把数据传给主页getSelectDate(selectDate)
                         }
                     });
    }

    public DateResult formatStringDate(String date){
        int firstIndex=date.indexOf("-");
        int lastIndex=date.lastIndexOf("-");
        String year=date.substring(0,firstIndex);
        String month=date.substring(firstIndex+1,lastIndex);
        String day=date.substring(lastIndex+1,date.length());
        return new DateResult(year,month,day);
    }
    public List<Gank> addHeadItem(List<Gank> tempListGank){
        List<Gank> resultListGank=new ArrayList<>();
        String headerStatus="";
        for(int i=0;i<tempListGank.size();i++){
            Gank gank=tempListGank.get(i);
            String header = gank.type;
            if(!gank.type.equals(headerStatus)&&!header.equals("福利")){
                Gank gankHeader = gank.clone();
                headerStatus = header;
                gankHeader.isHeader = true;
                resultListGank.add(gankHeader);
            }
            if(!gank.type.equals("福利")){
                gank.isHeader=false;
                resultListGank.add(gank);
            }
        }
        return resultListGank;
    }
}
