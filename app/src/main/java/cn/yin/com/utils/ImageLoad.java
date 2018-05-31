package cn.yin.com.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import cn.yin.com.MyApplication;
import cn.yin.com.R;

/**
 * Created by Jack on 2016/11/6.
 */

public class ImageLoad {

    public static void displayImage(String imgUrl, ImageView imageView){
        Glide.with(MyApplication.getContext())
                .load(imgUrl)
                .diskCacheStrategy( DiskCacheStrategy.ALL )
                .placeholder(R.mipmap.nv)
                .error(R.mipmap.nv)
                .crossFade()
                .centerCrop()
                .into(imageView);
    }

    public static void loadCirclrImage(int url, ImageView imageView) {
        Glide.with(MyApplication.getContext())
                .load(url)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .transform(new GlideRoundTransform(MyApplication.getContext()))
                .into(imageView);
    }

}
