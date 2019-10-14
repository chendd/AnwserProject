package com.wetin.anwserproject


import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.view.View
import com.wetin.anwserproject.adapter.FragmentAdapter
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.fragment.HomeFragment
import com.wetin.anwserproject.ui.fragment.MeFragment
import com.wetin.anwserproject.ui.fragment.VollFragment
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun initSetContentBefore() {
        super.initSetContentBefore()
    }
    override fun initPresenter(): IBaseContract.BasePresenter? =null

    override fun getContentId(): Int =R.layout.activity_main

    lateinit var fragmentDatas:ArrayList<Fragment>


    /**初始化组件*/
    override fun initWidget() {
        super.initWidget()
        getTopLayout().visibility= View.GONE
        //viewpager，tabyout绑定
        wg_content_pager.adapter=FragmentAdapter(supportFragmentManager,fragmentDatas)
        wg_bottom.setTabs(arrayOf("题库","音视频","我的"), intArrayOf(R.drawable.tab_home,R.drawable.tab_voll,R.drawable.tab_me),0)
        wg_bottom.addOnTabSelectedListener(object :TabLayout.BaseOnTabSelectedListener<TabLayout.Tab>{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                wg_content_pager.setCurrentItem(p0?.position!!,false)
            }

        })
        wg_content_pager.offscreenPageLimit=3
    }

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        if (savedInstanceState==null) {
            fragmentDatas = ArrayList()
            fragmentDatas.add(HomeFragment.newInstance())
            fragmentDatas.add(VollFragment.newInstance())
            fragmentDatas.add(MeFragment.newInstance())
        }
    }

    /**绑定事件*/
    override fun bindEvent() {
    }


}
