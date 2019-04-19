package cn.yin.com.entity;

import cn.yin.com.widget.PinyinUtils;

public class MusicDirectoryEntity implements Comparable<MusicDirectoryEntity> {


    private String directory;
    private String pinyin;

    public MusicDirectoryEntity(String directory) {
        this.directory=directory;
        pinyin= PinyinUtils.getPinyin(directory);
    }

    public String getDirectory() {
        return directory;
    }
    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    @Override
    public int compareTo(MusicDirectoryEntity musicDirectoryEntity) {
        return this.pinyin.compareTo(musicDirectoryEntity.getPinyin());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicDirectoryEntity that = (MusicDirectoryEntity) o;
        return directory != null ? directory.equals(that.directory) : that.directory == null;
    }

    @Override
    public int hashCode() {
        return directory != null ? directory.hashCode() : 0;
    }
}
