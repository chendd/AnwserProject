package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.ExtendUserAdapter
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.bean.table.XueKeBean
import com.wetin.anwserproject.model.keti.local.CollectLocalModel
import com.wetin.anwserproject.model.keti.local.XueKeLocalModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_collect.*

/**
 * 收藏页面
 */
class CollectActivity : BaseAnswerActivity<IBaseContract.BasePresenter>(){
    var collectDatas:ArrayList<XueKeBean> =ArrayList()
    lateinit var mAdapter:ExtendUserAdapter
    override fun getContentId(): Int =R.layout.activity_collect
    override fun initPresenter(): IBaseContract.BasePresenter? =null


    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        var keTiData = XueKeLocalModel.getInstance().allXueKe
        val userId = UserLocalModel.newInstanct().getUserData()?.userId
        for (xueBean in keTiData){
            val xueKeQuery = CollectLocalModel.getInstance().xueKeQuery(userId, xueBean.id.toString())?:null
            if (xueKeQuery!=null && xueKeQuery.isNotEmpty()){//收藏学科
                var collectXueBean = XueKeBean(xueBean.id,xueBean.name,xueBean.orderId,xueBean.type,
                    xueBean.unlockType,xueKeQuery.size,xueBean.userQuestionCount,xueBean.updateTime,xueBean.createTime)
                var child=ArrayList<ChapterBean>()
                for (chapter in xueBean.child!!){
                    val chapterQuery = CollectLocalModel.getInstance().chapterQuery(userId, xueBean.id.toString(),chapter.id.toString())?:null
                    if (chapterQuery!=null&& chapterQuery?.isNotEmpty()){//添加章节
                        chapter.questionCount=chapterQuery.size
                        child.add(chapter)
                    }
                }
                collectXueBean.child=child
                collectDatas.add(collectXueBean)
            }
        }
    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("收藏")
        mAdapter=ExtendUserAdapter(this,collectDatas)
        wg_listView.setAdapter(mAdapter)
    }


    /**绑定事件*/
    override fun bindEvent() {
        wg_listView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val child = mAdapter.getChild(groupPosition, childPosition) as ChapterBean
            val keTiBean = mAdapter.getGroup(groupPosition) as XueKeBean
            TopicNumActivity.launcher(
                this@CollectActivity,
                keTiBean.name,
                child.name ?: "",
                child.id.toString(),2
            )
            false
        }
    }




    companion object {
        fun launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,CollectActivity::class.java))
        }
    }
}
