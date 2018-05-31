package cn.yin.com.entity;

/**
 * Created by 79859 on 2018/5/14.
 */

public class Sort {
    public String title;
    public boolean classify;
    public boolean more;
    public boolean normal;
    public boolean choose;

    public Sort(){}
    public Sort(String title, boolean classify,
                boolean more, boolean normal,boolean choose) {
        this.title = title;
        this.classify = classify;
        this.more = more;
        this.normal = normal;
        this.choose=choose;
    }
}
