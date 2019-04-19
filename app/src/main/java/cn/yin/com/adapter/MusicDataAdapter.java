package cn.yin.com.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.yin.com.R;
import cn.yin.com.entity.Music;
import cn.yin.com.entity.MusicDirectoryEntity;
import cn.yin.com.listenr.OnClickLintener;
import cn.yin.com.widget.FileUtils;

public class MusicDataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private OnClickLintener onClickLintener;
    private List<Music> musiclist;
    private List<MusicDirectoryEntity> mdtPath;
    public MusicDataAdapter(Context context,List<MusicDirectoryEntity> mdtPath){
        this.context=context;
        this.mdtPath=mdtPath;
//        this.mdtPath= FileUtils.removeDuplicate(FileManager.getInstance(context).getDirectory(musiclist));
    }
    public void setOnClickLintener(OnClickLintener onClickLintener) {
        this.onClickLintener = onClickLintener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MusicDataAdapter.DateItemView(LayoutInflater.from(context).inflate(R.layout.item_selectdate,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MusicDataAdapter.DateItemView dateItemView= (MusicDataAdapter.DateItemView) holder;
//        dateItemView.bindView(musiclist.get(position));
        dateItemView.bindTitleView(mdtPath.get(position));
    }

    @Override
    public int getItemCount() {
        return mdtPath.size();
    }

    class DateItemView extends RecyclerView.ViewHolder{
        TextView selectitem_date;
        TextView music_path_this;
        public DateItemView(View itemView) {
            super(itemView);
            selectitem_date=(TextView)itemView.findViewById(R.id.selectitem_date);
            music_path_this=(TextView)itemView.findViewById(R.id.music_path_this);
            if(onClickLintener!=null){
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickLintener.onClick(view,getAdapterPosition());
                    }
                });
            }
        }
//        public void bindView(Music date){
////            selectitem_date.setText(date.getName());
//            music_path_this.setText(musicDirectoryEntity.getDirectory());
//            selectitem_date.setText(FileUtils.getFilesAllName(musicDirectoryEntity.getDirectory()).toString());
//        }

        public void bindTitleView(MusicDirectoryEntity musicDirectoryEntity) {
            music_path_this.setText(musicDirectoryEntity.getDirectory());
            selectitem_date.setText(FileUtils.getFilesAllName(musicDirectoryEntity.getDirectory()).toString());
        }
    }
}
