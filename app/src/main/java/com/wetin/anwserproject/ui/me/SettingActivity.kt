package com.wetin.anwserproject.ui.me

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v2.SelectDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.utils.DataCleanManager
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseAnswerActivity<IBaseContract.BasePresenter>(){
    override fun getContentId(): Int =R.layout.activity_setting

    override fun initPresenter(): IBaseContract.BasePresenter?=null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("设置",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))

        wg_tv_imgsize.text="${DataCleanManager.getTotalCacheSize(this)}"
        wg_item_cache.setOnClickListener {
            SelectDialog.show(
                this, "提示", "是否清除缓存数据?","清除",
                DialogInterface.OnClickListener { dialog, which ->
                    DataCleanManager.clearAllCache(this)
                    wg_tv_imgsize.text=""
                },"取消", null
            )
        }

        val login = UserLocalModel.newInstanct().isLogin()
        wg_item_quite.visibility=if (login) View.VISIBLE else View.GONE
    }
    /**绑定事件*/
    override fun bindEvent() {
        wg_item_comment.setOnClickListener {
            goCommentApp()
        }

        wg_item_suggest.setOnClickListener {

        }

        wg_item_forget.setOnClickListener {
            ForgetPassActivity.launcher(this)
        }

        //退出登录
        wg_item_quite.setOnClickListener {
            SelectDialog.build(this,"退出提示","是否退出登录?","确定",DialogInterface.OnClickListener { dialog, which ->
                UserLocalModel.newInstanct().clearUser()
                this.finish()
            },"取消",null).showDialog()
        }
    }

    companion object {
        fun launcher(fromActivity: Context){
            fromActivity.startActivity(Intent(fromActivity,SettingActivity::class.java))
        }
    }


    /**
     * 市场评分
     */
    private fun goCommentApp(){
        try {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }catch (e:Exception){
            ToastUtils.showShort("本机没有应用市场")
        }
    }
}
