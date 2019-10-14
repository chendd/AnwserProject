package com.wetin.anwserproject.widget

import android.content.Context
import android.support.design.widget.TabLayout
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import com.wetin.anwserproject.R
import android.widget.TextView


class BottomLayout(context: Context, attrs: AttributeSet?) : TabLayout(context, attrs) {
    init {
        this.setSelectedTabIndicatorColor(context.resources.getColor(android.R.color.white))
    }

     fun setTabs(tab_titles: Array<String>, tab_imgs: IntArray,defaultSect:Int=0) {
        for (i in tab_titles.indices) {
            //获取TabLayout的tab
            val tab = this.newTab()
            //初始化条目布局view
            val view = LayoutInflater.from(context).inflate(R.layout.layout_bottom_bar, null)
            tab.customView = view
            //tab的文字
            val tvTitle = view.findViewById<TextView>(R.id.tv_des)
            tvTitle.text = tab_titles[i]
            //tab的图片
            val imgTab = view.findViewById<ImageView>(R.id.iv_top)
            imgTab.setImageResource(tab_imgs[i])
            if (i == defaultSect) {
                //设置第一个默认选中
                this.addTab(tab, true)
            } else {
                this.addTab(tab, false)
            }
        }
    }

    /**
     * 选中指定位置
     * @index:位置索引
     */
    fun setSelectTab(index:Int){
        this.getTabAt(index)?.select()
    }

}