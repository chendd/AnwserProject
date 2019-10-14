package com.wetin.anwserproject.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class FragmentAdapter(fm: FragmentManager?,var data:List<Fragment>) : FragmentPagerAdapter(fm) {
    override fun getItem(p0: Int): Fragment {
        return data[p0]
    }

    override fun getCount(): Int {
        return data.size
    }
}