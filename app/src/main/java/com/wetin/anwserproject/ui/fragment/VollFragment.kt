package com.wetin.anwserproject.ui.fragment


import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.fragment.contract.VollFragContranct
import com.wetin.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_voll.*
/**
 * 首页中的《音视频》frament
 *
 */
class VollFragment : BaseFragment<VollFragContranct.Presenter>()  {
    override fun initPresenter(): VollFragContranct.Presenter? =null

    var frags=arrayListOf(AudioFragment.newInstance(),VideoFragment.newInstance())
     val fragsTitle= arrayListOf("音频","视频")
    companion object {
        fun newInstance():VollFragment{
            val args = Bundle()
            val fragment = VollFragment()
            fragment.arguments = args
            return fragment
        }
    }
    /**setContent 之前处理*/
    override fun initSetContentBefore() {

    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_voll
    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }

    /**初始化组件*/
    override fun initWidget() {

        wg_voll_pager.adapter=MyAdapter(childFragmentManager,context!!,frags,fragsTitle)
        wg_voll_tab.setupWithViewPager(wg_voll_pager)

    }

    /**绑定事件*/
    override fun bindEvent() {

    }


     class MyAdapter( fm: FragmentManager, var  context:Context,var fragmentList:List<Fragment> , var list_Title:List<String>) : FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment {
            return fragmentList[p0]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return list_Title[position]
        }
    }
}
