package com.wetin.common.utils.loadimg

import android.content.Context
import android.util.Log
import android.widget.ImageView
import kotlin.NullPointerException

/**
 * 内部采用代理模式，可以自由切换内部图片加载框架
 * 默认：Glide框架
 */
class ImageLoadUtil private constructor(): IImageLoad {
    /**
     * 配置参数
     * @config
     */
    override fun init(config: ImageConfig) {
        imageLoad!!.init(config)
    }

    var imageLoad: IImageLoad?=null

    /**
     * 配置参数，需要在Application初始化
     * @config:配置参数
     * @imageLoad:目标对象
     */
    fun initApp(config: ImageConfig, imageLoad: IImageLoad) {
        this.imageLoad=imageLoad
        init(config)
    }

    /**
     * 加载图片
     *
     */
    override fun loadImg(context: Context, imageView: ImageView, url: String?) {
        try {
             imageLoad!!.loadImg(context,imageView,url)
        }catch (e:NullPointerException){
            Log.e(this.javaClass.name,"ImageLoadUtil not init()")
        }
    }


    /**
     * 加载圆形图片
     *
     */
    override fun loadCircleImg(context: Context, imageView: ImageView, url: String?) {
        try {
            imageLoad!!.loadCircleImg(context,imageView,url)
        }catch (e:NullPointerException){
            Log.e(this.javaClass.name,"ImageLoadUtil not init()")
        }

    }


    override fun lowMemory(context: Context) {
        imageLoad!!.lowMemory(context)
    }


    override fun onTerminate(context: Context) {
        imageLoad!!.onTerminate(context)
    }


    companion object {
        val instance= SingletonHold.hold
    }

    object SingletonHold {
        val hold= ImageLoadUtil()
    }


}