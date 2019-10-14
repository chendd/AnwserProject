package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_search.*


class SearchActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    companion object {
        fun launcher(fromActivity: Activity,shareView:View){
            val options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(fromActivity, shareView,"search")
            fromActivity.startActivity(Intent(fromActivity,SearchActivity::class.java),options.toBundle())
        }
    }
    override fun getContentId(): Int =R.layout.activity_search

    override fun initPresenter(): IBaseContract.BasePresenter? =null
    override fun initWidget() {
        super.initWidget()
        getTopLayout().visibility=View.GONE

    }
    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_back.setOnClickListener { this.onBackPressed() }
    }

}
