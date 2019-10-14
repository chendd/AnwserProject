package com.wetin.anwserproject.bean.table;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.io.Serializable;

/**
 * 用户题目记录表
 */
@Entity
public class UserHistoryQuest implements Serializable {
    static final long serialVersionUID = 42L;

    @Id(autoincrement = true)
    private Long id;
    //用户id
    private String userId;
    //学科id
    private Long xueKeId;
    //章节 id
    private Long chapterId;
    //内容 id
    private Long contentId;
    //用户选择答案
    private String userAnswer;

    //选择正确状态:1对,2错
    private int answerStatus;

    private boolean isUpload=false;

    @Generated(hash = 1686019301)
    public UserHistoryQuest(Long id, String userId, Long xueKeId, Long chapterId,
            Long contentId, String userAnswer, int answerStatus, boolean isUpload) {
        this.id = id;
        this.userId = userId;
        this.xueKeId = xueKeId;
        this.chapterId = chapterId;
        this.contentId = contentId;
        this.userAnswer = userAnswer;
        this.answerStatus = answerStatus;
        this.isUpload = isUpload;
    }



    @Generated(hash = 603348397)
    public UserHistoryQuest() {
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

    public Long getXueKeId() {
        return this.xueKeId;
    }

    public void setXueKeId(Long xueKeId) {
        this.xueKeId = xueKeId;
    }

    public Long getChapterId() {
        return this.chapterId;
    }

    public void setChapterId(Long chapterId) {
        this.chapterId = chapterId;
    }

    public Long getContentId() {
        return this.contentId;
    }

    public void setContentId(Long contentId) {
        this.contentId = contentId;
    }

    public String getUserAnswer() {
        return this.userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public int getAnswerStatus() {
        return this.answerStatus;
    }

    public void setAnswerStatus(int answerStatus) {
        this.answerStatus = answerStatus;
    }

    public boolean getIsUpload() {
        return this.isUpload;
    }

    public void setIsUpload(boolean isUpload) {
        this.isUpload = isUpload;
    }


}
