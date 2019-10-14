package com.wetin.anwserproject.model.video.local;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.table.ChapterBean;
import com.wetin.anwserproject.bean.table.XueKeBean;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoLocalModel {
    private VideoLocalModel(){}
    private static VideoLocalModel Instance;
    public static VideoLocalModel getInstance(){
        if (Instance==null){
            Instance=new VideoLocalModel();
        }
        return Instance;
    }

    /*保存数据*/
    public void save(String resultString){
        SPUtils.getInstance().remove("video");
        SPUtils.getInstance().put("video",resultString);
    }



    /*得到数据*/
    public ArrayList<XueKeBean> getVideoList(){
        try {
            String video = SPUtils.getInstance().getString("video");
            Type type = new TypeToken<List<XueKeBean>>(){}.getType();
            List<XueKeBean> list = new Gson().fromJson(video,type);
            return (ArrayList<XueKeBean>) list;
        }catch (Exception e){
            return null;
        }
    }



}
