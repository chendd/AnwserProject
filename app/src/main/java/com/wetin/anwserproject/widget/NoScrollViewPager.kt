package com.wetin.anwserproject.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

class NoScrollViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
         return false
    }
}