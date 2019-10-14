package com.wetin.anwserproject.model.user.remote

import com.wetin.anwserproject.net.ApiManager
import com.wetin.anwserproject.net.api.UserApi

/**
 * Api实例化
 */
object UserRemoteModel {

    //用户Api
    fun getUserApi():UserApi{
       return ApiManager.getInstance().createRequestClass(UserApi::class.java)
    }

    //题库Api

}