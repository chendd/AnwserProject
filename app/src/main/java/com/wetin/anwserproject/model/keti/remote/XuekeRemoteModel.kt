package com.wetin.anwserproject.model.keti.remote

import com.wetin.anwserproject.net.ApiManager
import com.wetin.anwserproject.net.api.KeTiApi
import com.wetin.anwserproject.net.api.UserApi

/**
 * Api实例化
 */
object XuekeRemoteModel {

    //首页题目
    fun getKeTiApi():KeTiApi{
       return ApiManager.getInstance().createRequestClass(KeTiApi::class.java)
    }

}