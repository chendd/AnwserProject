package com.wetin.anwserproject.model.keti.local;

import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.table.Collect;

import java.util.ArrayList;
import java.util.List;

public class CollectLocalModel {
    private CollectLocalModel(){}
    private static CollectLocalModel Instance;
    public static CollectLocalModel getInstance(){
        if (Instance==null){
            Instance=new CollectLocalModel();
        }
        return Instance;
    }
    /**学科下的数目查询*/
    public ArrayList<Collect> xueKeQuery(String userId,String xueKeid){
        try {
            return (ArrayList<Collect>) DaoManager.getInstance().getDaoSession().getCollectDao().queryRaw("where XUE_KE_ID=? and USER_ID=?",new String[]{xueKeid,userId});
        }catch (Exception e){
            return null;
        }
    }

    /**章节数目下查询*/
    public ArrayList<Collect> chapterQuery(String userId, String xueKeid, String chapterId){
        try {
            return (ArrayList<Collect>) DaoManager.getInstance().getDaoSession().getCollectDao().queryRaw("where CHAPTER_ID=? and XUE_KE_ID=? and USER_ID=?",new String[]{chapterId,xueKeid,userId});
        }catch (Exception e){
            return null;
        }

    }

    /**收藏操作*/
    public long CollectContent(Collect collect){
        return DaoManager.getInstance().getDaoSession().getCollectDao().insert(collect);
    }

    /**是否收藏过*/
    public Collect isCollect(String id,String userId){
        List<Collect> collects = DaoManager.getInstance().getDaoSession().getCollectDao().queryRaw("where CONTENT_ID=? and USER_ID=?", new String[]{id, userId});
        if (collects.size()>0){
            return collects.get(0);
        }
        return null;
    }

    /**取消收藏*/
    public void cancelCollectContent(Collect collect){
         DaoManager.getInstance().getDaoSession().getCollectDao().deleteInTx(collect);
         DaoManager.getInstance().getDaoSession().getCollectDao().detach(collect);
    }

}
