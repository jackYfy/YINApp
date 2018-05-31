package cn.yin.com.Presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import cn.yin.com.api.Tag;
import cn.yin.com.entity.Sort;
import cn.yin.com.interfaces.TodayRecommInterface;
import cn.yin.com.ui.AllClassActivity;
import cn.yin.com.ui.fragment.AllClassFragment;
import cn.yin.com.utils.SPUtils;

/**
 * Created by 79859 on 2018/5/14.
 */

public class AllClassPersenter extends BasePresenter {
    public AllClassPersenter(Context context) {
        super(context);
    }

    public void initNotAllTabData(TodayRecommInterface.AllClassInterface allClassInterface,
                                  TabLayout tabLayout, List<Fragment> listFragment) {
            List<Sort> sortList=new ArrayList<>();
            List<String> stringTitle=new ArrayList<>();
        for(int i=0;i<Tag.Aategory.length;i++){
            sortList.add(new Sort(Tag.Aategory[i],false,false,true,true));
        }
//            sortList.add(new Sort("",true,false,false,false));
//            for(int i=0;i<2;i++){
//                sortList.add(new Sort(Tag.Aategory[i],false,false,true,true));
//            }
//            sortList.add(new Sort("",false,true,false,false));
//            for (int i=2;i<Tag.Aategory.length;i++){
//                sortList.add(new Sort(Tag.Aategory[i],false,false,true,false));
//            }
            SPUtils.put(context,"TabMenu","TabGson",new Gson().toJson(sortList));
        for(int i=0;i<sortList.size();i++){
            if(sortList.get(i).normal==true&& sortList.get(i).choose){
                tabLayout.addTab(tabLayout.newTab().setText(sortList.get(i).title));
                stringTitle.add(sortList.get(i).title);
                Bundle bundle=new Bundle();
                bundle.putString("TabTitle",sortList.get(i).title);
                AllClassFragment allClassFragment=new AllClassFragment();
                allClassFragment.setArguments(bundle);
                listFragment.add(allClassFragment);
            }
        }
        allClassInterface.initViewData(tabLayout,listFragment,stringTitle);
    }

    public void initAllTabData(TodayRecommInterface.AllClassInterface allClassInterface, String tabData,
                               TabLayout tabLayout, List<Fragment> listFragment) {
        List<Sort> listSort=new Gson().fromJson(tabData,new TypeToken<List<Sort>>(){}.getType());
        List<String> listTitle=new ArrayList<>();
        for(int i=0;i<listSort.size();i++){
             if (listSort.get(i).normal==true&&listSort.get(i).choose){
                 tabLayout.addTab(tabLayout.newTab().setText(listSort.get(i).title));
                 listTitle.add(listSort.get(i).title);
                 Bundle bundle=new Bundle();
                 bundle.putString("TabTitle",listSort.get(i).title);
                 AllClassFragment allClassFragment=new AllClassFragment();
                 allClassFragment.setArguments(bundle);
                 listFragment.add(allClassFragment);
             }
        }
        allClassInterface.initViewData(tabLayout,listFragment,listTitle);
    }


}
