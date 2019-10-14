package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import com.wetin.anwserproject.R
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.me.contract.RegisterContract
import com.wetin.anwserproject.ui.me.prestener.RegisterPrenstener
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseAnswerActivity<RegisterContract.Prestener>(),RegisterContract.view{

     var countDownTimer:CountDownTimer?=null
    companion object {
        fun launcher(fromActivity:Activity){
            fromActivity.startActivity(Intent(fromActivity,RegisterActivity::class.java))
        }
    }
    override fun getContentId(): Int =R.layout.activity_register

    override fun initPresenter(): RegisterContract.Prestener? =RegisterPrenstener(this)

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("注册",R.color.text_gray)
            .setLeftImg(R.drawable.ic_back)
            .setImgShow(true,false)
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_get_ma.setOnClickListener {
            if (!wg_ed_phone.text.isEmpty()) {
                mPresenter?.sendSms(wg_ed_phone.text.toString())
            }else{
                showInfo("请输入手机号")
            }
        }
        wg_register.setOnClickListener {
            if (!wg_ed_pass.text.isEmpty()){
                showLoading("注册中...")
                mPresenter?.register(wg_ed_phone.text.toString(),wg_ed_code.text.toString(),wg_ed_pass.text.toString())
            }else{
                showInfo("请完善填空")
            }
        }
    }


    override fun showSendSmsSuc() {
        showInfo("验证码发送成功")
        startCountDown()
    }

    override fun showSendSmsFaile(info:String?) {
        showInfo("验证码发送失败")
    }

    override fun showRegisterSuc() {
        dimissLoading()
        showInfo("注册成功")
        this.finish()
    }

    override fun showRegisterFaile(err:String?) {
        dimissLoading()
        showInfo(err!!)
    }

    fun startCountDown() {
        wg_get_ma.isEnabled=false
        countDownTimer = object : CountDownTimer(60000L, 1000L) {
            override fun onFinish() {
                wg_get_ma.text = "重新获取验证码"
                wg_get_ma.setTextColor(this@RegisterActivity.resources.getColor(R.color.bg_red))
                wg_get_ma.isEnabled=true
            }

            override fun onTick(millisUntilFinished: Long) {
                wg_get_ma.text = "已发送(${millisUntilFinished/1000})s"
                wg_get_ma.setTextColor(this@RegisterActivity.resources.getColor(R.color.colorAccent))
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.onFinish()
        countDownTimer?.cancel()
    }
}
