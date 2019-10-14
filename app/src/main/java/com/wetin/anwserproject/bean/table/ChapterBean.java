package com.wetin.anwserproject.bean.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity
public class ChapterBean implements Serializable {
    static final long serialVersionUID = 42L;
    //视频
    @Id
    private Long id;
    private String name;
    private Long parentId;
    private String videoUrl;
    private String imgUrl;
    private String author;
    private int playCount;
    private int orderId;
    private String videoTime;
    private String createTime;
    //文章
    private int unlockType;
    private int type;
    private int questionCount;
    private int userQuestionCount;
    private String updateTime;
    @Generated(hash = 195180467)
    public ChapterBean(Long id, String name, Long parentId, String videoUrl, String imgUrl,
            String author, int playCount, int orderId, String videoTime, String createTime,
            int unlockType, int type, int questionCount, int userQuestionCount,
            String updateTime) {
        this.id = id;
        this.name = name;
        this.parentId = parentId;
        this.videoUrl = videoUrl;
        this.imgUrl = imgUrl;
        this.author = author;
        this.playCount = playCount;
        this.orderId = orderId;
        this.videoTime = videoTime;
        this.createTime = createTime;
        this.unlockType = unlockType;
        this.type = type;
        this.questionCount = questionCount;
        this.userQuestionCount = userQuestionCount;
        this.updateTime = updateTime;
    }
    @Generated(hash = 1028095945)
    public ChapterBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getParentId() {
        return this.parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public String getVideoUrl() {
        return this.videoUrl;
    }
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    public String getImgUrl() {
        return this.imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
    public String getAuthor() {
        return this.author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }
    public int getPlayCount() {
        return this.playCount;
    }
    public void setPlayCount(int playCount) {
        this.playCount = playCount;
    }
    public int getOrderId() {
        return this.orderId;
    }
    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
    public String getVideoTime() {
        return this.videoTime;
    }
    public void setVideoTime(String videoTime) {
        this.videoTime = videoTime;
    }
    public String getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public int getUnlockType() {
        return this.unlockType;
    }
    public void setUnlockType(int unlockType) {
        this.unlockType = unlockType;
    }
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public int getQuestionCount() {
        return this.questionCount;
    }
    public void setQuestionCount(int questionCount) {
        this.questionCount = questionCount;
    }
    public int getUserQuestionCount() {
        return this.userQuestionCount;
    }
    public void setUserQuestionCount(int userQuestionCount) {
        this.userQuestionCount = userQuestionCount;
    }
    public String getUpdateTime() {
        return this.updateTime;
    }
    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
