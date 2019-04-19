package cn.yin.com.ui;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.yin.com.R;
import cn.yin.com.adapter.MusicDataAdapter;
import cn.yin.com.base.BaseActivity;
import cn.yin.com.entity.Music;
import cn.yin.com.entity.MusicDirectoryEntity;
import cn.yin.com.listenr.OnClickLintener;
import cn.yin.com.widget.FileManager;
import cn.yin.com.widget.FileUtils;

public class MusicPathActivity extends BaseActivity implements OnClickLintener {
    @Bind(R.id.music_recyclerview)
    RecyclerView daterecyclerview;
    private MusicDataAdapter adapter;
    private List<Music>  musicData=new ArrayList();
    @Override
    public int bindLayout() {
        return R.layout.activity_music_path;
    }

    @Override
    public void initView(View view) {
        setBackup();
        daterecyclerview.setLayoutManager(new LinearLayoutManager(this));
        daterecyclerview.addItemDecoration(new DividerItemDecoration(
                this,LinearLayoutManager.VERTICAL));
        musicData= FileManager.getInstance(this).getMusics();
        if(musicData!=null&&musicData.size()>0){
            List<MusicDirectoryEntity> mdtPath= FileUtils.removeDuplicate(FileManager.getInstance(this).getDirectory(musicData));
            adapter=new MusicDataAdapter(MusicPathActivity.this,mdtPath);
            adapter.setOnClickLintener(this);
            daterecyclerview.setAdapter(adapter);
        }

    }

    @Override
    public void onClick(View v, int position) {
        Music music=musicData.get(position);
        Log.i("Music:",music.toString());
    }
}
