package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract

/**
 * 评论页面
 */
class CommentActivity :  BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun getContentId(): Int =R.layout.activity_comment

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("评论")
    }

    /**绑定事件*/
    override fun bindEvent() {
    }


    companion object {
        fun launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,CommentActivity::class.java))
        }
    }

}
