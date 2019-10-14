package com.wetin.common.utils.loadimg

import android.content.Context
import android.widget.ImageView

interface IImageLoad {
    /**
     * 配置参数
     * @config
     */
    fun init(config: ImageConfig)

    /**
     * 加载图片
     *
     */
    fun loadImg(context: Context, imageView: ImageView, url: String?)

    /**
     * 加载圆形图片
     *
     */
    fun loadCircleImg(context: Context, imageView: ImageView, url: String?)

    fun lowMemory(context: Context)

    fun onTerminate(context: Context)
}