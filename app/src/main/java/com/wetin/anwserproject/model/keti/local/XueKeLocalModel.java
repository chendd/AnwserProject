package com.wetin.anwserproject.model.keti.local;

import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.table.ChapterBean;
import com.wetin.anwserproject.bean.table.XueKeBean;

import java.util.ArrayList;
import java.util.List;

public class XueKeLocalModel {
    private XueKeLocalModel(){}
    private static XueKeLocalModel Instance;
    public static XueKeLocalModel getInstance(){
        if (Instance==null){
            Instance=new XueKeLocalModel();
        }
        return Instance;
    }

    /**
     * 查询所有学科和章节
     */
    public ArrayList<XueKeBean> getAllXueKe(){
        return (ArrayList<XueKeBean>) DaoManager.getInstance().getDaoSession().getXueKeBeanDao()
                .queryBuilder().list();
    }

    /**
     * 插入学科
     */
    public void insertXueKe(XueKeBean datas){
        DaoManager.getInstance().getDaoSession().getXueKeBeanDao()
                .insertOrReplace(datas);
        List<ChapterBean> child = datas.getChild();
        for (ChapterBean bean:child){
            DaoManager.getInstance().getDaoSession().getChapterBeanDao().insertOrReplace(bean);
        }

    }

}
