package com.zuoer.sofa.blog.web.home.miniprog.model;

/**
 * @author zuoer
 * @version Item, v 0.1 2020/3/10 10:00 zuoer Exp $
 */
public class Item {
    private String type;

    private String photoVideoUrl;

    private String id;

    private String thumbnailUrl;

    private String age;

    public Item(String type, String photoVideoUrl, String id, String thumbnailUrl,String age) {
        this.type = type;
        this.photoVideoUrl = photoVideoUrl;
        this.id = id;
        this.thumbnailUrl = thumbnailUrl;
        this.age=age;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoVideoUrl() {
        return photoVideoUrl;
    }

    public void setPhotoVideoUrl(String photoVideoUrl) {
        this.photoVideoUrl = photoVideoUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
