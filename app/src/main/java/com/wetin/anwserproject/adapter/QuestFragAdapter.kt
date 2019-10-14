package com.wetin.anwserproject.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.ui.fragment.WorkQuestFragment

class QuestFragAdapter(fm: FragmentManager?,var datas:ArrayList<ContentBean>?) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return WorkQuestFragment.newInstance(p0)
    }

    override fun getCount(): Int {
        return datas?.size?:0
    }
}