package com.wetin.anwserproject.ui.home.help

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        //不是第一个的格子都设一个左边和底部的间距
        outRect.bottom = space

    }

}