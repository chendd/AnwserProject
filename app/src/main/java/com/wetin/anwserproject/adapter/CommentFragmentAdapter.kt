package com.wetin.anwserproject.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wetin.anwserproject.ui.me.MyCommentFragment

class CommentFragmentAdapter (fm: FragmentManager?, var data:List<String>) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return MyCommentFragment.newIntance(p0+1)
    }


    override fun getCount(): Int {
        return data.size
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return data[position]
    }
}