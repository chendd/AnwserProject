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

public class AudioLocalModel {
    private AudioLocalModel() {
    }

    private static AudioLocalModel Instance;

    public static AudioLocalModel getInstance() {
        if (Instance == null) {
            Instance = new AudioLocalModel();
        }
        return Instance;
    }


    /*保存数据*/
    public void save(String resultString) {
        SPUtils.getInstance().remove("audios");
        SPUtils.getInstance().put("audios", resultString);
    }


    /*得到数据*/
    public ArrayList<XueKeBean> getAudioList() {
        try {
            String video = SPUtils.getInstance().getString("audios");
            Type type = new TypeToken<List<XueKeBean>>(){}.getType();
            List<XueKeBean> list = new Gson().fromJson(video,type);
            return (ArrayList<XueKeBean>) list;
        } catch (Exception e) {
            return null;
        }
    }


}
