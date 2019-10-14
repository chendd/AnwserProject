package com.wetin.anwserproject.model.user.local

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.wetin.anwserproject.bean.UserBean
import com.wetin.anwserproject.model.user.IUser
import java.lang.Exception

class UserLocalModel private constructor(): IUser {

    companion object {
        fun newInstanct():UserLocalModel{
            return Instance.instanct
        }
    }

    private object Instance{
        val instanct=UserLocalModel()
    }


    /**是否登录*/
    override fun isLogin(): Boolean {
        return SPUtils.getInstance().getBoolean("login")
    }

    /**保存登录*/
    override fun saveLogin() {
        SPUtils.getInstance().put("login",true)
    }

    /**用户数据*/
    override fun getUserData(): UserBean? {
        val userString = SPUtils.getInstance().getString("userData")
        try {
            return Gson().fromJson(userString,UserBean::class.java)
        }catch (e:Exception){
            return null
        }

    }

    /**保存用户数据*/
    override fun saveUser(userBean: UserBean) {
        SPUtils.getInstance().put("userData",Gson().toJson(userBean))
    }

    /**清空用户数据*/
    override fun clearUser() {
        SPUtils.getInstance().remove("userData")
        SPUtils.getInstance().put("login",false)
    }
}