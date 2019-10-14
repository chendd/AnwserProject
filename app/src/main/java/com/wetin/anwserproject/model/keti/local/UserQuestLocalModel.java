package com.wetin.anwserproject.model.keti.local;

import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.UserBean;
import com.wetin.anwserproject.bean.table.Collect;
import com.wetin.anwserproject.bean.table.ContentBean;
import com.wetin.anwserproject.bean.table.UserHistoryQuest;
import com.wetin.anwserproject.model.user.local.UserLocalModel;

import java.util.ArrayList;
import java.util.Objects;

/**
 * 用户做过的题目
 */
public class UserQuestLocalModel {
    private UserQuestLocalModel(){}
    private static UserQuestLocalModel Instance;
    private  String userId="-1";
    public static UserQuestLocalModel getInstance(){
        if (Instance==null){
            Instance=new UserQuestLocalModel();
        }
        return Instance;
    }

    /**学科下的数目查询*/
    public ArrayList<UserHistoryQuest> getUserDoQuest(String contentId){
        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and CONTENT_ID=?",new String[]{getUserId(),contentId});
        }catch (Exception e){
            return null;
        }
    }


    /**错误的记录*/
    public ArrayList<UserHistoryQuest> getUserDoQuestByFaile(String contentId){

        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and CONTENT_ID=? and ANSWER_STATUS=2",new String[]{getUserId(),contentId});
        }catch (Exception e){
            return null;
        }
    }


    /**
     *
     * @param chapterId
     * @return
     */
    public ArrayList<UserHistoryQuest> getUserQuestByChapterId(String chapterId){

        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and CHAPTER_ID=? ",new String[]{getUserId(),chapterId});
        }catch (Exception e){
            return null;
        }
    }



    /**
     *
     * @param xueKeID
     * @return
     */
    public ArrayList<UserHistoryQuest> getUserQuestByXueKeID(String xueKeID){

        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and XUE_KE_ID=? ",new String[]{getUserId(),xueKeID});
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 查询用户做错的题目
     * @param chapterId
     * @return
     */
    public ArrayList<UserHistoryQuest> getUserQuestFaileByChapterId(String chapterId){

        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and CHAPTER_ID=? and ANSWER_STATUS=2",new String[]{getUserId(),chapterId});
        }catch (Exception e){
            return null;
        }
    }



    /**
     * 查询用户做错的题目
     * @param xueKeID
     * @return
     */
    public ArrayList<UserHistoryQuest> getUserQuestFaileByXueKeID(String xueKeID){

        try {
            return (ArrayList<UserHistoryQuest>)DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=? and XUE_KE_ID=? and ANSWER_STATUS=2",new String[]{getUserId(),xueKeID});
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 查询用户收藏的题目
     * @param contentId
     * @return
     */
    public ArrayList<Collect> getUserDoQuestByCollect(String contentId){

        try {
            return (ArrayList<Collect>)DaoManager.getInstance().getDaoSession().getCollectDao().queryRaw("where USER_ID=? and CONTENT_ID=?",new String[]{getUserId(),contentId});
        }catch (Exception e){
            return null;
        }
    }



    public void insert(UserHistoryQuest userHistoryQuest){
        DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().insertOrReplace(userHistoryQuest);
    }


    /**
     * 游客数据同步到用户
     * @param userId
     */
    public void visitorDataToUser(String userId){
        ArrayList<UserHistoryQuest> userHistoryQuests = (ArrayList<UserHistoryQuest>) DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().queryRaw("where USER_ID=?", new String[]{"-1"});
        for (UserHistoryQuest bean : userHistoryQuests){
            bean.setUserId(userId);
            DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().update(bean);
        }
    }


    /**
     *
     * @param chapterId
     */
    public void clear(String chapterId){
        //清理缓存
        DaoManager.getInstance().getDaoSession().getContentBeanDao().detachAll();
        ArrayList<UserHistoryQuest> userQuestByChapterId = getUserQuestByChapterId(chapterId);
        for (UserHistoryQuest bean :userQuestByChapterId) {
            DaoManager.getInstance().getDaoSession().getUserHistoryQuestDao().delete(bean);
        }
    }

    private String getUserId(){
        UserBean userData = UserLocalModel.Companion.newInstanct().getUserData();
        if (userData!=null) {
            return userData.getUserId();
        }else{
           return "-1";
        }
    }
}
