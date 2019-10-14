package com.wetin.anwserproject

import android.app.Application
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.support.multidex.MultiDex
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.danikula.videocache.HttpProxyCacheServer
import com.kongzue.dialog.util.TextInfo
import com.kongzue.dialog.v2.DialogSettings
import com.kongzue.dialog.v2.DialogSettings.THEME_LIGHT
import com.tencent.bugly.Bugly
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tsy.sdk.social.PlatformConfig
import com.wetin.anwserproject.application.ForegroundObserver
import com.wetin.anwserproject.bean.DaoManager
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.net.ApiConstact
import com.wetin.anwserproject.net.ApiManager
import com.wetin.anwserproject.net.help.ApiInterceptor
import com.wetin.anwserproject.pay.PayUtils
import com.wetin.anwserproject.service.PlayService
import com.wetin.anwserproject.utils.Config
import com.wetin.anwserproject.utils.Config.WECHATID
import com.wetin.anwserproject.utils.Preferences
import com.wetin.anwserproject.utils.imgload.GlideImageLoad
import com.wetin.common.utils.DensityUtils
import com.wetin.common.utils.loadimg.ImageConfig
import com.wetin.common.utils.loadimg.ImageLoadUtil

/**
 *
 * */

class ProjectApplication: Application() {
    var proxy: HttpProxyCacheServer? = null

    companion object {
        lateinit var api: IWXAPI
        private lateinit var  instance: ProjectApplication
        lateinit var chapterContent:ArrayList<ContentBean>
        fun getInstance(): ProjectApplication {
            return instance
        }
        fun getProxy(): HttpProxyCacheServer {
            val app = instance
            if (app.proxy==null){
                app.proxy=app.newProxys()
            }
            return app.proxy as HttpProxyCacheServer
        }


    }


    /**视频缓存初始化*/
    private fun newProxys(): HttpProxyCacheServer {
        return HttpProxyCacheServer(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance =this
        initConfig()
        PayUtils.registerWx(this)
        ForegroundObserver.init(this)
        var intent =  Intent(this, PlayService::class.java)
        startService(intent)
        Preferences.init(this)

    }

    private fun initConfig(){
        DensityUtils.setDensity(this)
        Bugly.init(this, Config.BUGLYID, false)
        val config = LogUtils.getConfig()
        config.isLogSwitch=Config.LOGSWITCH
        //图片
        ImageLoadUtil.instance.initApp(
            ImageConfig(
                this, true,
                R.drawable.ic_logo,0,
                true
            ),
            GlideImageLoad.instance)
        //网络
        ApiManager.getInstance().with(this,true).addIntercepter(ApiInterceptor()).build(ApiConstact.SERVICE_HOST)
        //shareprefer 初始化
        SPUtils.getInstance("file_answer")
        //微信
        PlatformConfig.setWeixin(WECHATID)
        //dialog
        initDialog()
        DaoManager.getInstance().init(this)
    }




    /***
     * 初始化全局对话框
     */
    private fun initDialog() {
        DialogSettings.use_blur = false
        DialogSettings.tip_theme = THEME_LIGHT
        DialogSettings.dialog_theme = DialogSettings.THEME_LIGHT
        DialogSettings.style = DialogSettings.STYLE_IOS
        DialogSettings.dialogOkButtonTextInfo = TextInfo()
            .setBold(false)
            .setFontColor(Color.rgb(51, 51, 51))
        DialogSettings.dialogButtonTextInfo = TextInfo()
            .setBold(false)
            .setFontColor(Color.rgb(33, 33, 33))
        DialogSettings.menuTextInfo= TextInfo()
            .setBold(false)
            .setFontColor(Color.rgb(33, 33, 33))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

    override fun onLowMemory() {
        super.onLowMemory()
        ImageLoadUtil.instance.lowMemory(this)
    }


    override fun onTerminate() {
        super.onTerminate()
        ImageLoadUtil.instance.onTerminate(this)
    }


}