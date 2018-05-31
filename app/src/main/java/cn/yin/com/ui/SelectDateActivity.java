package cn.yin.com.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import cn.yin.com.R;
import cn.yin.com.adapter.SelectDateAdapter;
import cn.yin.com.api.GankApi;
import cn.yin.com.api.GankApiFactory;
import cn.yin.com.base.BaseActivity;
import cn.yin.com.entity.DateResult;
import cn.yin.com.entity.SelectDate;
import cn.yin.com.interfaces.SelectDateInterface;
import cn.yin.com.listenr.OnClickLintener;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SelectDateActivity extends BaseActivity implements SelectDateInterface, OnClickLintener {

    @Bind(R.id.date_recyclerview)
    RecyclerView daterecyclerview;

    public static final GankApi gankApi= GankApiFactory.getGankApi();

    private SelectDate selectDate;
    private SelectDateAdapter adapter;
    @Override
    public int bindLayout() {
        return R.layout.activity_select_date;
    }

    @Override
    public void initView(View view) {
      setBackup();
      setTitle("选择日期");
        init();
    }

    private void init() {
        daterecyclerview.setLayoutManager(new LinearLayoutManager(this));
        daterecyclerview.addItemDecoration(new DividerItemDecoration(
                this,LinearLayoutManager.VERTICAL));
        getSelectDateMethod(this);
    }

    private void getSelectDateMethod(final SelectDateInterface selectDateInterface) {
        gankApi.getAllSelectDate()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<SelectDate>() {
                    @Override
                    public void onCompleted() {}
                    @Override
                    public void onError(Throwable e) {selectDateInterface.onError(e); }
                    @Override
                    public void onNext(SelectDate selectDate) {
                         selectDateInterface.getSelectDate(selectDate);
                    }
                });

    }

    @Override
    public void getSelectDate(SelectDate selectDate) {
        this.selectDate=selectDate;
        if(selectDate.results!=null&&selectDate.results.size()>0){
            adapter=new SelectDateAdapter(SelectDateActivity.this,selectDate.results);
            adapter.setOnClickLintener(this);
            daterecyclerview.setAdapter(adapter);
        }
    }

    @Override
    public void onError(Throwable e) {
        showSnackbar("网络出错");
    }

    public DateResult formatStringDate(String date){
        int firstIndex=date.indexOf("-");
        int lastIndex=date.lastIndexOf("-");
        String year=date.substring(0,firstIndex);
        String month=date.substring(firstIndex+1,lastIndex);
        String day=date.substring(lastIndex+1,date.length());
        return new DateResult(year,month,day);
    }
    @Override
    public void onClick(View v, int position) {
        showSnackbar(selectDate.results.get(position));
        DateResult dateResult= formatStringDate(selectDate.results.get(position));
        EventBus.getDefault().post(dateResult);
        finish();
    }

}
