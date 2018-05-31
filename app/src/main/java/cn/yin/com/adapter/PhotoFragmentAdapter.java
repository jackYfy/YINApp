package cn.yin.com.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import cn.yin.com.entity.Gank;
import cn.yin.com.ui.PhotoActivity;
import cn.yin.com.ui.fragment.PhotoFragment;

/**
 * Created by 79859 on 2018/5/31.
 */

public class PhotoFragmentAdapter extends FragmentStatePagerAdapter {

    private List<Gank> listGank=null;
    private int current;
    private PhotoActivity photoActivity;

    public PhotoFragmentAdapter(FragmentManager fm, List<Gank> listGank, int current, PhotoActivity photoActivity) {
        super(fm);
        this.listGank=listGank;
        this.current=current;
        this.photoActivity=photoActivity;
    }
    @Override
    public Fragment getItem(int position) {
        Bundle bundle=new Bundle();
        bundle.putString("url",listGank.get(position).url);
        bundle.putString("id",listGank.get(position)._id);
        bundle.putBoolean("current",current==position);
        PhotoFragment photoFragment=new PhotoFragment();
        photoFragment.setPhotoActivity(photoActivity);
        photoFragment.setArguments(bundle);
        return photoFragment;
    }

    @Override
    public int getCount() {
        return listGank.size();
    }
}
