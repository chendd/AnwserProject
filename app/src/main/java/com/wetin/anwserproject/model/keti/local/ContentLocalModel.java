package com.wetin.anwserproject.model.keti.local;

import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.table.ContentBean;

import java.util.ArrayList;

public class ContentLocalModel {
    private ContentLocalModel(){}
    private static ContentLocalModel Instance;
    public static ContentLocalModel getInstance(){
        if (Instance==null){
            Instance=new ContentLocalModel();
        }
        return Instance;
    }

    /**
     * 查询章节下的所有题目内容
     */
    public ArrayList<ContentBean> query( String chapterId){
        try {
            return (ArrayList<ContentBean>) DaoManager.getInstance().getDaoSession().getContentBeanDao().queryRaw("where CHILD_ID=? ORDER BY QUESTION_NUMBER",new String[]{chapterId});
        }catch (Exception e){
            return null;
        }
    }


    /**
     * 插入题目内容
     */
    public void insert(ContentBean bean){
        try {
              DaoManager.getInstance().getDaoSession().getContentBeanDao().insertOrReplace(bean);
        }catch (Exception e){
        }
    }
}
