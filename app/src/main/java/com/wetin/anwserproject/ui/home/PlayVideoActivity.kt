package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import com.wetin.anwserproject.ProjectApplication
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.ui.fragment.VideoFragment
import com.wetin.anwserproject.ui.home.fragment.CommentFragment
import kotlinx.android.synthetic.main.activity_play_video.*


/**
 * 视频播放
 */

class PlayVideoActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    lateinit var chapterBean: ChapterBean
    override fun getContentId(): Int=R.layout.activity_play_video
    override fun initSetContentBefore() {

    }
    override fun initPresenter(): IBaseContract.BasePresenter?=null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        chapterBean=intent.getSerializableExtra(INTENT_BEAN) as ChapterBean
    }

    override fun initWidget() {
        super.initWidget()
        setTopShow(false)
        initVideo()
        initTablayout()
    }

    private fun initTablayout() {
        wg_tablayout.setupWithViewPager(wg_viewpager)
        val mAdapter=Adapter(supportFragmentManager, arrayOf("目录","评论"), arrayListOf(VideoFragment.newInstance(true,chapterBean), CommentFragment.newIntance(chapterBean.id.toString(),3)))
        wg_viewpager.adapter=mAdapter
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_back.setOnClickListener { onBackPressed() }
    }


    /**fragment适配器*/
    class Adapter(fm: FragmentManager?, var tabDatas: Array<String>, var fragsData: ArrayList<Fragment>) :
        FragmentStatePagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment {
            return fragsData[p0]
        }

        override fun getCount(): Int {
            return tabDatas.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabDatas[position]
        }
    }



    private fun initVideo(){
        val arrayMap = LinkedHashMap<String, String>()
        val proxyUrl = ProjectApplication.getProxy().getProxyUrl(chapterBean.videoUrl)
        arrayMap["高清"] = proxyUrl
        val jzDataSource = JZDataSource(arrayMap, chapterBean.name)
        jzDataSource.looping = false
        jzDataSource.currentUrlIndex = 0
        jzDataSource.headerMap["key"] = "value"
        wg_play.setUp(
            jzDataSource,
            Jzvd.SCREEN_NORMAL
        )
        wg_play.startButton.performClick()
    }

    override fun onPause() {
        super.onPause()
        Jzvd.goOnPlayOnPause()
    }

    override fun onResume() {
        super.onResume()
        Jzvd.goOnPlayOnResume()
    }

    override fun onStop() {
        super.onStop()
        Jzvd.resetAllVideos()
    }
    companion object {
        val INTENT_BEAN="bean"
        fun launcher(fromActivity: Activity,chapterBean: ChapterBean){
            val intent = Intent(fromActivity, PlayVideoActivity::class.java)
            intent.putExtra(INTENT_BEAN,chapterBean)
            fromActivity.startActivity(intent)
        }
    }

    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }
}
