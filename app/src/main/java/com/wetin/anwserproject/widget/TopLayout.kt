package com.wetin.anwserproject.widget

import android.app.Activity
import android.content.Context
import android.support.v7.widget.Toolbar
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.wetin.anwserproject.R
import kotlinx.android.synthetic.main.layout_top.view.*

class TopLayout(context: Context, attrs: AttributeSet?) : Toolbar(context, attrs),View.OnClickListener{
    var listener: TopClickListener?=null
    var mActivity:Activity?=null
    init {
        //root 为null 在addView显示宽高 为wrapcontent 会影响布局
        View.inflate(context, R.layout.layout_top,this)
        initEvent()
    }

    private fun initEvent() {
        wg_top_right.setOnClickListener(this)
        wg_top_left.setOnClickListener(this)
    }

    /**设置文字*/
    fun setTitle(info:String,color:Int=android.R.color.black):TopLayout{
        wg_top_title.text=info
        wg_top_title.setTextColor(context.resources.getColor(color))
        return this
    }

    /**设置左边图片*/
    fun setLeftImg(res:Int):TopLayout{
        wg_top_left.setImageResource(res)
        return this
    }

    /**设置右边图片*/
    fun setRightImg(res:Int=0,righString: String):TopLayout{
        wg_top_right.visibility=View.VISIBLE
        if (res==0){
            wg_top_right.text = righString
        }else{
            wg_top_right.setBackgroundResource(res)
        }
        return this
    }

    /**左右边图片显示*/
    fun setImgShow(isLeft:Boolean=true,isRight:Boolean=true):TopLayout{
        wg_top_left.visibility=if (isLeft) View.VISIBLE else View.GONE
        wg_top_right.visibility=if(isRight) View.VISIBLE else View.GONE
        return this
    }

    /**事件监听回调*/
    fun setClickListener(listener: TopClickListener):TopLayout{
        this.listener=listener
        return this
    }


    fun setCurrentActivity(activity: Activity){
        mActivity=activity
    }


    override fun onClick(v: View?) {
        if(listener!=null){
            listener?.click(v?.id!!)
        }else {
            when (v?.id) {
                R.id.wg_top_left -> {
                    mActivity?.finish()
                    mActivity = null
                }
            }
        }

    }

    interface TopClickListener{
        fun click(id:Int)
    }
}