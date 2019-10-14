package com.wetin.anwserproject.bean.table;

import com.wetin.anwserproject.utils.StringConverter;
import org.greenrobot.greendao.annotation.*;

import java.util.List;

@Entity
public class ContentBean {
    @Id
    private Long id;
    private int parentId;
    private String parentName;
    private int childId;
    private String childName;
    private String questionContent;
    private String answer;
    private String pointReduction;
    private String answerAnalysis;
    private String video;
    private String videoImg;
    private String ossVideo;
    private String ossVideoImg;
    private String questionType;
    private int questionNumber;
    private int answerCount;
    private int answerRightCount;
    private int commentCount;
    private String createTime;
    private String choiceQuestion;
    @Transient
    private UserHistoryQuest userHistoryQuest;

    @Generated(hash = 832090396)
    public ContentBean(Long id, int parentId, String parentName, int childId,
            String childName, String questionContent, String answer,
            String pointReduction, String answerAnalysis, String video,
            String videoImg, String ossVideo, String ossVideoImg,
            String questionType, int questionNumber, int answerCount,
            int answerRightCount, int commentCount, String createTime,
            String choiceQuestion) {
        this.id = id;
        this.parentId = parentId;
        this.parentName = parentName;
        this.childId = childId;
        this.childName = childName;
        this.questionContent = questionContent;
        this.answer = answer;
        this.pointReduction = pointReduction;
        this.answerAnalysis = answerAnalysis;
        this.video = video;
        this.videoImg = videoImg;
        this.ossVideo = ossVideo;
        this.ossVideoImg = ossVideoImg;
        this.questionType = questionType;
        this.questionNumber = questionNumber;
        this.answerCount = answerCount;
        this.answerRightCount = answerRightCount;
        this.commentCount = commentCount;
        this.createTime = createTime;
        this.choiceQuestion = choiceQuestion;
    }

    @Generated(hash = 1643641106)
    public ContentBean() {
    }

    public UserHistoryQuest getUserHistoryQuest() {
        return userHistoryQuest;
    }

    public void setUserHistoryQuest(UserHistoryQuest userHistoryQuest) {
        this.userHistoryQuest = userHistoryQuest;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getParentId() {
        return this.parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return this.parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public int getChildId() {
        return this.childId;
    }

    public void setChildId(int childId) {
        this.childId = childId;
    }

    public String getChildName() {
        return this.childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getQuestionContent() {
        return this.questionContent;
    }

    public void setQuestionContent(String questionContent) {
        this.questionContent = questionContent;
    }

    public String getAnswer() {
        return this.answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getPointReduction() {
        return this.pointReduction;
    }

    public void setPointReduction(String pointReduction) {
        this.pointReduction = pointReduction;
    }

    public String getAnswerAnalysis() {
        return this.answerAnalysis;
    }

    public void setAnswerAnalysis(String answerAnalysis) {
        this.answerAnalysis = answerAnalysis;
    }

    public String getVideo() {
        return this.video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoImg() {
        return this.videoImg;
    }

    public void setVideoImg(String videoImg) {
        this.videoImg = videoImg;
    }

    public String getOssVideo() {
        return this.ossVideo;
    }

    public void setOssVideo(String ossVideo) {
        this.ossVideo = ossVideo;
    }

    public String getOssVideoImg() {
        return this.ossVideoImg;
    }

    public void setOssVideoImg(String ossVideoImg) {
        this.ossVideoImg = ossVideoImg;
    }

    public String getQuestionType() {
        return this.questionType;
    }

    public void setQuestionType(String questionType) {
        this.questionType = questionType;
    }

    public int getQuestionNumber() {
        return this.questionNumber;
    }

    public void setQuestionNumber(int questionNumber) {
        this.questionNumber = questionNumber;
    }

    public int getAnswerCount() {
        return this.answerCount;
    }

    public void setAnswerCount(int answerCount) {
        this.answerCount = answerCount;
    }

    public int getAnswerRightCount() {
        return this.answerRightCount;
    }

    public void setAnswerRightCount(int answerRightCount) {
        this.answerRightCount = answerRightCount;
    }

    public int getCommentCount() {
        return this.commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getChoiceQuestion() {
        return this.choiceQuestion;
    }

    public void setChoiceQuestion(String choiceQuestion) {
        this.choiceQuestion = choiceQuestion;
    }


}
