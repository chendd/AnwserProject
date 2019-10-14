package com.wetin.anwserproject.utils.imgload

import android.content.ComponentCallbacks2.TRIM_MEMORY_COMPLETE
import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.wetin.common.utils.loadimg.IImageLoad
import com.wetin.common.utils.loadimg.ImageConfig
import android.R
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.RequestOptions.bitmapTransform


class GlideImageLoad private constructor() : IImageLoad {
    lateinit var options:RequestOptions
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder= GlideImageLoad()
    }

    /**
     * 配置参数
     * @config
     */
    override fun init(config: ImageConfig){
        options =RequestOptions()
        options.apply {
            this.error(config.errorimg)
            this.placeholder(config.placeImg)
            if (config.skipMemoryCache) {
                this.skipMemoryCache(true)
                this.diskCacheStrategy(DiskCacheStrategy.RESOURCE)
            }
            if (!config.isFade) this.dontAnimate()
        }

    }

    /**
     * 加载图片
     *
     */
    override fun loadImg(context: Context,imageView: ImageView,url:String?){
        Glide.with(context).load(url)
            .apply(options)
            .into(imageView)
    }

    /**
     * 加载圆形图片
     *
     */
    override fun loadCircleImg(context: Context,imageView: ImageView,url:String?){
        val op=options.clone()
        Glide.with(context).load(url)
            .apply(op.circleCrop())
            .transition(DrawableTransitionOptions().crossFade(400))
            .into(imageView)
    }


    override fun lowMemory(context: Context){
        Glide.get(context).onLowMemory()
    }


    override fun onTerminate(context: Context){
        Glide.get(context).trimMemory(TRIM_MEMORY_COMPLETE)
    }
}

