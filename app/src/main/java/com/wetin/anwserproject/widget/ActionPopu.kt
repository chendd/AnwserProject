package com.wetin.anwserproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import com.wetin.anwserproject.R

class ActionPopu(context: Context?) :
    PopupWindow(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT) {
    var btnReplay: TextView
    var btnFuzhi: TextView
    var btnDel: TextView

    init {
        this.contentView = LayoutInflater.from(context).inflate(R.layout.layout_popu_action, null)
        btnReplay = this.contentView.findViewById(R.id.wg_btn_replay)
        btnFuzhi = this.contentView.findViewById(R.id.wg_btn_fuzhi)
        btnDel = this.contentView.findViewById(R.id.wg_btn_del)
        this.setBackgroundDrawable(null)
        this.isFocusable = true
        this.isOutsideTouchable = true
    }

    fun isShowDel(boolean: Boolean = false):ActionPopu{
        btnDel.visibility = if (boolean) View.VISIBLE else View.GONE
        return this
    }

    fun setlistener(lis:View.OnClickListener):ActionPopu{
        this.btnDel.setOnClickListener(lis)
        this.btnReplay.setOnClickListener(lis)
        this.btnFuzhi.setOnClickListener(lis)
        return this
    }
}