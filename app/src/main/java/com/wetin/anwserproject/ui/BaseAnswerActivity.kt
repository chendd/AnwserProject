package com.wetin.anwserproject.ui

import android.view.LayoutInflater
import android.view.View
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.base.BaseActivity
import com.wetin.common.utils.DensityUtils
import com.wetin.anwserproject.widget.TopLayout
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.layout_base.*

abstract class BaseAnswerActivity<T: IBaseContract.BasePresenter> : BaseActivity<T>(),IBaseContract.BaseView{

    override fun initSetContentBefore() {
        DensityUtils.setDefault(this)
    }
    /**布局*/
    override fun getContentLayout(): Int= R.layout.layout_base

    override fun initWidget() {
        wg_content.addView(LayoutInflater.from(this).inflate(getContentId(),null))
        wg_top.setCurrentActivity(this)
    }

    /**设置顶部导航栏显示*/
    fun setTopShow(isShow: Boolean){
        wg_top.visibility=if (isShow) View.VISIBLE else View.GONE
        wg_top.setPadding(0,BarUtils.getStatusBarHeight(),0,0)
    }

    /**顶部布局*/
    fun getTopLayout():TopLayout{
        return wg_top
    }

    /**显示提示*/
    override fun showInfo(info: String) {
        ToastUtils.showShort(info)
    }

    /**加载中*/
    override fun showLoading(info:String) {
        WaitDialog.setCanCancelGlobal(true)
        WaitDialog.show(this,info)
    }

    /**加载失败*/
    override fun showError() {
    }

    /**无网络*/
    override fun showNotNet() {
    }

    /**空页面*/
    override fun showEmpty() {
    }

    /**内容*/
    override fun showContent() {

    }

    override fun dimissLoading() {
        WaitDialog.dismiss()
    }
    abstract fun getContentId(): Int
}