package me.warm.rxjava_rxandroid_example.entity;

import java.util.List;

/**
 * Created by warm on 17/6/23.
 */

public class Data {


    /**
     * _id : 594109e5421aa92c769a8c84
     * createdAt : 2017-06-14T18:03:17.393Z
     * desc : RecyclerView：利用打造悬浮效果
     * images : ["http://img.gank.io/775b8ae5-4c21-4553-a77e-a0842248e1af"]
     * publishedAt : 2017-06-15T13:55:57.947Z
     * source : web
     * type : Android
     * url : http://www.jianshu.com/p/b335b620af39
     * used : true
     * who : null
     */

    private String _id;
    private String createdAt;
    private String desc;
    private String publishedAt;
    private String source;
    private String type;
    private String url;
    private boolean used;
    private Object who;
    private List<String> images;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public Object getWho() {
        return who;
    }

    public void setWho(Object who) {
        this.who = who;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Data{" +
                "_id='" + _id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", desc='" + desc + '\'' +
                ", publishedAt='" + publishedAt + '\'' +
                ", source='" + source + '\'' +
                ", type='" + type + '\'' +
                ", url='" + url + '\'' +
                ", used=" + used +
                ", who=" + who +
                ", images=" + images +
                '}';
    }
}
