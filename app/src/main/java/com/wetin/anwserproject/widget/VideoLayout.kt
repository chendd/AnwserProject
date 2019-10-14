package com.wetin.anwserproject.widget

import android.content.Context
import android.util.AttributeSet
import cn.jzvd.JzvdStd

class VideoLayout(context: Context?, attrs: AttributeSet?) : JzvdStd(context, attrs) {
    var listeners:ProgressListener?=null
    override fun onProgress(progress: Int, position: Long, duration: Long) {
        super.onProgress(progress, position, duration)
        listeners?.progress(progress,position,duration)
    }


    fun setListener(listener:ProgressListener){
        this.listeners=listener
    }

     interface ProgressListener{
        fun progress(progress:Int, position:Long, duration:Long)
    }
}