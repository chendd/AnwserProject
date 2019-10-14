package com.wetin.anwserproject.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.wetin.anwserproject.R

abstract class BaseUpDownDialog(context: Context) : Dialog(context, R.style.dialog){
    init {
        setContentView(getContentView())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //处理宽度和动画执行
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window.decorView.setPadding(0, 0, 0, 0)
        window.setWindowAnimations(R.style.dialogWindowAnim)
        window!!.attributes = layoutParams
        super.onCreate(savedInstanceState)
    }
    /**布局id*/
    abstract fun getContentView():Int
}