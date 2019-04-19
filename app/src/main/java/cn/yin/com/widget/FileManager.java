package cn.yin.com.widget;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;

import cn.yin.com.entity.Music;
import cn.yin.com.entity.MusicDirectoryEntity;

public class FileManager {

    private static FileManager mInstance;
    private static Context mContext;
    private static ContentResolver mContentResolver;
    private static Object mLock = new Object();
    private List<Music> mDatas = new ArrayList<>();
    public static FileManager getInstance(Context context){
        if (mInstance == null){
            synchronized (mLock){
                if (mInstance == null){
                    mInstance = new FileManager();
                    mContext = context;
                    mContentResolver = context.getContentResolver();
                }
            }
        }
        return mInstance;
    }


    /**
     * 获取本机音乐列表 外置存储卡中的音乐信息
     * @return
     */
    public List<Music> getMusics() {
        /*if(FileUtils.isSDCardAvailable()){

        }*/
        Cursor c = null;
        try {
            c = mContentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                    MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

            while (c.moveToNext()) {
                String path = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));// 路径
//                if (FileUtils.isExists(path)) {
//                    continue;
//                }
                String name = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)); // 歌曲名
                String album = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)); // 专辑
                String artist = c.getString(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)); // 作者
                long size = c.getLong(c.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));// 大小
                int duration = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));// 时长
                int time = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));// 歌曲的id
                // int albumId = c.getInt(c.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
                mDatas.add(new Music(name, path, album, artist, size, duration));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return mDatas;
    }
    //音乐路径父级目录
    public List<MusicDirectoryEntity> getDirectory(List<Music> mDatas){
        List<MusicDirectoryEntity> mDirectory=new ArrayList<>();
        for (Music mPath:mDatas ){
            mDirectory.add(new MusicDirectoryEntity(FileUtils.getPath(mPath.getPath())));//截取文件目录
        }
        return mDirectory;
    }

}
