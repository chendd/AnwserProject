package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import cn.jzvd.Jzvd
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.ProjectApplication
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.QuestFragAdapter
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.model.keti.local.ContentLocalModel
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_question.*
import retrofit2.Call
import retrofit2.Response

class QuestionActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    companion object {
        const val CHAPTERID = "chapterId"
        const val CHAPTER = "chapter"
        const val TITLE = "title"
        const val INDEXPOSITION = "postion"
        fun launcher(fromactivity: Activity, chapterId: String, chapter: String, title: String,postition:Int) {
            val intent = Intent(fromactivity, QuestionActivity::class.java)
            intent.putExtra(CHAPTERID, chapterId)
            intent.putExtra(CHAPTER, chapter)
            intent.putExtra(TITLE, title)
            intent.putExtra(INDEXPOSITION, postition)
            fromactivity.startActivity(intent)
        }
    }

    override fun getContentId(): Int = R.layout.activity_question

    override fun initPresenter(): IBaseContract.BasePresenter? = null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle(intent.getStringExtra(TITLE))
        getTopLayout().setRightImg(righString = "${intent.getIntExtra(INDEXPOSITION,0)+1}/${ProjectApplication.chapterContent.size}")
    }

    override fun initWidget() {
        super.initWidget()
        wg_quest_chapter.text=intent.getStringExtra(CHAPTER)
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_quest_pager.adapter = QuestFragAdapter(
            supportFragmentManager,
            ProjectApplication.chapterContent
        )
        wg_quest_pager.offscreenPageLimit=1
        wg_quest_pager.setCurrentItem(intent.getIntExtra(INDEXPOSITION,0),false)
        wg_quest_pager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                getTopLayout().setRightImg(righString = "${p0+1}/${ProjectApplication.chapterContent.size}")
            }

        })
    }





    override fun onBackPressed() {
        if (Jzvd.backPress()) {
            return
        }
        super.onBackPressed()
    }
}
