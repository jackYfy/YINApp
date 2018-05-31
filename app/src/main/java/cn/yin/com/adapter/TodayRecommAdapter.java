package cn.yin.com.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import cn.yin.com.R;
import cn.yin.com.api.GankApiFactory;
import cn.yin.com.api.Tag;
import cn.yin.com.entity.Gank;
import cn.yin.com.entity.ImageType;
import cn.yin.com.interfaces.IImageInfo;
import cn.yin.com.listenr.OnClickLintener;
import cn.yin.com.utils.ImageLoad;

/**
 * Created by 79859 on 2018/5/9.
 */

public class TodayRecommAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private static Context context;
    private List<Gank> listGank;
    private final int HEADER = 0;
    private final int NORMAL = 1;
    private OnClickLintener onClickLintener;

    public TodayRecommAdapter(Context context, List<Gank> listGank) {
        this.context = context;
        this.listGank = listGank;
    }

    public void setOnClickLintener(OnClickLintener onClickLintener) {
        this.onClickLintener = onClickLintener;
    }
//创建布局
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getItemViewType(viewType) == HEADER) {
            return new HeaderView(LayoutInflater.from(context).inflate(R.layout.item_today_header, parent, false));
        } else {
            return new ItemView(LayoutInflater.from(context).inflate(R.layout.item_today_main, parent, false));
        }
    }
//绑定数据
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == HEADER) {
            HeaderView headerView = (HeaderView) holder;
            headerView.bindItem(listGank.get(position).type);
        } else if (getItemViewType(position) == NORMAL) {
            final ItemView itemView = (ItemView) holder;
            itemView.bindItem(listGank.get(position));
        }
    }


    @Override
    public int getItemCount() {
        return listGank.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (listGank.get(position).isHeader) {
            return HEADER;
        } else {
            return NORMAL;
        }
    }

    class HeaderView extends RecyclerView.ViewHolder {
        public TextView header;

        public HeaderView(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.main_header);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void bindItem(String title) {
            header.setText(title);
           setTextViewDrawableLeft(header, getTitleIcon(title));
        }
    }

    class ItemView extends RecyclerView.ViewHolder {
        public TextView itemText;
        public ImageView itemImage;
        public ImageView collected_content;
        public TextView who_content;

        public ItemView(View itemView) {
            super(itemView);
            itemText =itemView.findViewById(R.id.main_item_content);
            itemImage =itemView.findViewById(R.id.main_item_image);
            collected_content=itemView.findViewById(R.id.collected_content);
            who_content=itemView.findViewById(R.id.who_content);
            if (onClickLintener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickLintener.onClick(view, getAdapterPosition());
                    }
                });
            }
        }

        public void bindItem(final Gank gank){
            itemText.setText(gank.desc);
            who_content.setText(gank.who);
            if(gank.images!=null){
                if(!gank.hasLoadImage){
                    //后缀添加？imageInfo 获取的是图片的信息，用于判断是什么格式的图片，GIF过滤掉
                    GankApiFactory.GankApiImageInfo(gank.images.get(0)+ "?imageInfo", new IImageInfo() {
                        @Override
                        public void error() {}
                        @Override
                        public void seccess(ImageType imageType) {
                            if (!imageType.format.equals("gif")) {
                                itemImage.setVisibility(View.VISIBLE);
                                ImageLoad.displayImage(gank.images.get(0),itemImage);
                                gank.hasLoadImage=true;
                            }
                        }
                    });
                }else{
                    itemImage.setVisibility(View.VISIBLE);
                    ImageLoad.displayImage(gank.images.get(0),itemImage);

                }
            }
        }
    }

    private int getTitleIcon(String title) {
        switch (title) {
            case Tag.Category.ANDROID:
                return R.drawable.ic_android_128;
            case Tag.Category.IOS:
                return R.drawable.ic_ios_128;
            case Tag.Category.WEB:
                return R.drawable.ic_web_128;
            case Tag.Category.EXTEND:
                return R.drawable.ic_extend_128;
            case Tag.Category.VIDEO:
                return R.drawable.ic_video_128;
            case Tag.Category.MEIZI:
                return R.drawable.ic_the_heart_stealer_128;
            case Tag.Category.SUGGEST:
                return R.drawable.ic_recommend_128;
            case Tag.Category.APP:
                return R.drawable.ic_app_128;
            default:
                return R.drawable.ic_the_heart_stealer_128;
        }
    }

    /**
     * set textView left drawable
     *
     * @param tv target
     * @param drawableRes drawable resId
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP) public static void setTextViewDrawableLeft(
            TextView tv, @DrawableRes int drawableRes) {
        Drawable drawable =context.getDrawable(drawableRes);
        // 这一步必须要做,否则不会显示.
        assert drawable != null;
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(drawable, null, null, null);
    }

}