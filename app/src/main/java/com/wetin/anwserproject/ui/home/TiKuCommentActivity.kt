package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import com.wetin.anwserproject.ui.home.fragment.CommentFragment

class TiKuCommentActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun getContentId(): Int =R.layout.activity_ti_ku_comment
     var mMainFragment:CommentFragment?=null
    override fun initPresenter(): IBaseContract.BasePresenter? =null
    override fun initDatas(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            mMainFragment = supportFragmentManager
                .getFragment(savedInstanceState, "MainFragment") as CommentFragment?
        } else {
            mMainFragment = CommentFragment.newIntance(intent.getStringExtra("id"),1)
        }

        mMainFragment?.let {
            supportFragmentManager.beginTransaction()
                .replace(R.id.wg_main, it)
                .commit()
        }
    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("评价",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
    }


    /**
     * 当活动被回收时，存储当前的状态。
     *
     * @param outState 状态数据。
     */
    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        // 存储 Fragment 的状态。
        mMainFragment?.let { supportFragmentManager.putFragment(outState!!, "MainFragment", it) }
    }

    override fun bindEvent() {
    }


    companion object {
        fun launcher(fromActivity: Activity,tiId:String){
            val intent = Intent(fromActivity, TiKuCommentActivity::class.java)
            intent.putExtra("id",tiId)
            fromActivity.startActivity(intent)
        }
    }

}
