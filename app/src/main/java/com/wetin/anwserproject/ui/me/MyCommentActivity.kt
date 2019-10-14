package com.wetin.anwserproject.ui.me

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.CommentFragmentAdapter
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_my_comment.*

class MyCommentActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun getContentId(): Int= R.layout.activity_my_comment

    override fun initPresenter(): IBaseContract.BasePresenter?=null

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("我的评论",R.color.white).setLeftImg(R.drawable.ic_white_back).setBackgroundResource(R.color.bg_red)
        wg_pager.adapter=CommentFragmentAdapter(supportFragmentManager, arrayListOf("我的评论","评论我"))
        wg_tablayout.setupWithViewPager(wg_pager)
    }

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }

    /**绑定事件*/
    override fun bindEvent() {
        
    }


    companion object {
        fun launcher(fromActivity: Context){
            fromActivity.startActivity(Intent(fromActivity,MyCommentActivity::class.java))
        }
    }
}
