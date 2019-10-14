package com.wetin.anwserproject.ui.me

import android.os.Bundle
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract

class SuggestActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun getContentId(): Int =R.layout.activity_suggest

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    /**绑定事件*/
    override fun bindEvent() {

    }


}
