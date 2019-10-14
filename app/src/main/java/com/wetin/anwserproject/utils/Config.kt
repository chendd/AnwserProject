package com.wetin.anwserproject.utils

object Config {
    //打印log开关
    val LOGSWITCH=true

    //bugly appid
    val BUGLYID="0748187b44"

    //微信
    val WECHATID="wxa1648663532cbef4"


    enum class LockType{
        //0 随便看 1 微信分享 2 QQ分享 3收费
        Free,WeChatShare,QQShare,Charge
    }
}