package com.wetin.anwserproject.net.util;

import android.content.Context;
import android.net.ConnectivityManager;
import com.wetin.anwserproject.bean.DaoManager;
import com.wetin.anwserproject.bean.table.ChapterBean;
import com.wetin.anwserproject.bean.table.XueKeBean;

import java.util.ArrayList;
import java.util.List;

public class NetWorkConnection {
    /** 判断网络是否连接
     *  true 连接
     *  false 没网络
     * */
    public static boolean checkNetworkConnection(Context context) {
        ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        android.net.NetworkInfo wifi = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        android.net.NetworkInfo mobile = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (wifi.isAvailable() || mobile.isAvailable())  //getState()方法是查询是否连接了数据网络
            return true;
        else
            return false;
    }


}
