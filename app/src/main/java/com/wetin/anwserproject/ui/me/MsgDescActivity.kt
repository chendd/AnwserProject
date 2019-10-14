package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_msg_desc.*

class MsgDescActivity : BaseAnswerActivity<IBaseContract.BasePresenter>(){
    override fun getContentId(): Int= R.layout.activity_msg_desc

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("消息详情",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))

    }

    override fun initWidget() {
        super.initWidget()
        wg_msg_title.text=intent.getStringExtra(INTENT_TITLE)
        wg_msg_content.text=intent.getStringExtra(INTENT_DESC)
    }
    /**绑定事件*/
    override fun bindEvent() {
    }

    companion object {
        const val INTENT_TITLE="title"
        const val INTENT_DESC="desc"
        fun launcher(fromActivity: Activity,title:String,desc:String){
            val intent = Intent(fromActivity, MsgDescActivity::class.java)
            intent.putExtra(INTENT_TITLE,title)
            intent.putExtra(INTENT_DESC,desc)
            fromActivity.startActivity(intent)
        }
    }
}
