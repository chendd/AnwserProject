package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.ExtendUserAdapter
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.bean.table.XueKeBean
import com.wetin.anwserproject.model.keti.local.UserQuestLocalModel
import com.wetin.anwserproject.model.keti.local.XueKeLocalModel
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_answer_faile.*

/**
 * 错题显示页面
 */
class AnswerFaileActivity :  BaseAnswerActivity<IBaseContract.BasePresenter>(){
    var faileDatas:ArrayList<XueKeBean> =ArrayList()
    lateinit var mAdapter:ExtendUserAdapter
    override fun getContentId(): Int =R.layout.activity_answer_faile

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("错题")


    }
    /**绑定事件*/
    override fun bindEvent() {
        wg_listView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val child = mAdapter.getChild(groupPosition, childPosition) as ChapterBean
            val keTiBean = mAdapter.getGroup(groupPosition) as XueKeBean
            TopicNumActivity.launcher(
                this@AnswerFaileActivity,
                keTiBean.name,
                child.name ?: "",
                child.id.toString(),1
            )
            false
        }
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    companion object {
        fun launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,AnswerFaileActivity::class.java))
        }
    }

    private fun getData(){
        faileDatas.clear()
        var keTiData = XueKeLocalModel.getInstance().allXueKe
        for (xueBean in keTiData){
            val xueKeQuery =  UserQuestLocalModel.getInstance().getUserQuestFaileByXueKeID(xueBean.id.toString())?:null
            if (xueKeQuery!=null && xueKeQuery.isNotEmpty()){//错误学科
                var collectXueBean = XueKeBean(xueBean.id,xueBean.name,xueBean.orderId,xueBean.type,
                    xueBean.unlockType,xueKeQuery.size,xueBean.userQuestionCount,xueBean.updateTime,xueBean.createTime)
                var child=ArrayList<ChapterBean>()
                for (chapter in xueBean.child!!){
                    val chapterQuery = UserQuestLocalModel.getInstance().getUserQuestFaileByChapterId(chapter.id.toString())?:null
                    if (chapterQuery!=null&& chapterQuery?.isNotEmpty()){//添加章节
                        chapter.questionCount=chapterQuery.size
                        child.add(chapter)
                    }
                }
                collectXueBean.child=child
                faileDatas.add(collectXueBean)
            }
        }
        mAdapter= ExtendUserAdapter(this,faileDatas)
        wg_listView.setAdapter(mAdapter)
    }
}
