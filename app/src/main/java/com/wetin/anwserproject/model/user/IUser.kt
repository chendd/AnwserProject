package com.wetin.anwserproject.model.user

import com.wetin.anwserproject.bean.UserBean

interface IUser {
    /**是否登录*/
    fun isLogin():Boolean

    /**保存登录*/
    fun saveLogin()
    /**用户数据*/
    fun getUserData():UserBean?

    /**保存用户数据*/
    fun saveUser(userBean: UserBean)

    /**清空用户数据*/
    fun clearUser()
}