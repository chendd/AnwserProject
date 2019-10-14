package com.wetin.anwserproject.bean.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 用户收藏表
 */
@Entity
public class Collect {
    @Id(autoincrement = true)
    private Long id;
    //用户id
    private String userId;
    //学科id
    private int xueKeId;
    //章节 id
    private int chapterId;
    //内容 id
    private int contentId;
    @Generated(hash = 1457278212)
    public Collect(Long id, String userId, int xueKeId, int chapterId,
            int contentId) {
        this.id = id;
        this.userId = userId;
        this.xueKeId = xueKeId;
        this.chapterId = chapterId;
        this.contentId = contentId;
    }
    @Generated(hash = 1726975718)
    public Collect() {

    }

    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUserId() {
        return this.userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public int getXueKeId() {
        return this.xueKeId;
    }
    public void setXueKeId(int xueKeId) {
        this.xueKeId = xueKeId;
    }
    public int getChapterId() {
        return this.chapterId;
    }
    public void setChapterId(int chapterId) {
        this.chapterId = chapterId;
    }
    public int getContentId() {
        return this.contentId;
    }
    public void setContentId(int contentId) {
        this.contentId = contentId;
    }


}
