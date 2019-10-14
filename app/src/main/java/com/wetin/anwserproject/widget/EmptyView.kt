package com.wetin.anwserproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.wetin.anwserproject.R
import kotlinx.android.synthetic.main.layout_empty.view.*

class EmptyView(context: Context?, attrs: AttributeSet?) : RelativeLayout(context, attrs) {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_empty,this)
    }

    fun setInfo(info:String):EmptyView{
        wg_info.setText(info)
        return this
    }
}