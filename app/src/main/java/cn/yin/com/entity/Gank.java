package cn.yin.com.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 79859 on 2018/5/9.
 */

public class Gank implements Cloneable,Serializable {

    public String _id;
    public String createdAt;
    public String desc;
    public String publishedAt;
    public String source;
    public String type;
    public String url;
    public boolean used;
    public String who;

    public List<String> images;

    public boolean isHeader;
    public boolean hasLoadImage=false;

    public boolean isMeizi(){
        return type.equals("福利");
    }

    @Override
    public Gank clone() {
        Gank gank = null;
        try{
            gank = (Gank)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return gank;
    }
}
