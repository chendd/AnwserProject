package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.TextUtils
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.UserBean
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import com.wetin.common.base.ViewManager
import kotlinx.android.synthetic.main.activity_sms_login.*
import retrofit2.Call
import retrofit2.Response

class SmsLoginActivity : BaseAnswerActivity<IBaseContract.BasePresenter> (){
    var countDownTimer:CountDownTimer?=null
    override fun getContentId(): Int = R.layout.activity_sms_login

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?){
        getTopLayout().setTitle("短信登录",R.color.text_gray)
            .setLeftImg(R.drawable.ic_back)
            .setImgShow(true,false)
    }

    /**绑定事件*/
    override fun bindEvent() {
        //登录操作
        wg_login.setOnClickListener {
            if (wg_ed_code.text.isEmpty()){
                showInfo("验证码不能为空")
                return@setOnClickListener
            }
            WaitDialog.show(this,"登录中...")
            UserRemoteModel.getUserApi().login(phone=wg_ed_phone.text.toString(),smsCode = wg_ed_code.text.toString(),loginType = "2")
                .enqueue(object :ResultCall<UserBean>{
                    override fun onSuccess(call: Call<ResultBean<UserBean>>, response: Response<ResultBean<UserBean>>) {
                        WaitDialog.dismiss()
                        UserLocalModel.newInstanct().saveUser(response?.body()?.result!!)
                        UserLocalModel.newInstanct().saveLogin()
                        ViewManager.instance.finishActivity(LoginActivity::class.java)
                        this@SmsLoginActivity.finish()
                    }

                    override fun onFaile(err: String?) {
                        WaitDialog.dismiss()
                        TipDialog.show(this@SmsLoginActivity, "登录失败", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                    }

                })
        }
        //获取验证码
        wg_get_ma.setOnClickListener {
            if (wg_ed_phone.text.isEmpty()){
                showInfo("请输入手机号码")
                return@setOnClickListener
            }
            UserRemoteModel.getUserApi().sendSms(wg_ed_phone.text.toString(),"1")
                .enqueue(object:ResultCall<String>{
                    override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                        showInfo("发送验证码成功")
                        startCountDown()
                    }

                    override fun onFaile(err: String?) {
                        showInfo("发送验证码失败")
                    }

                })
        }
    }


    fun startCountDown() {
        wg_get_ma.isEnabled=false
        countDownTimer = object : CountDownTimer(60000L, 1000L) {
            override fun onFinish() {
                wg_get_ma.text = "重新获取验证码"
                wg_get_ma.setTextColor(this@SmsLoginActivity.resources.getColor(R.color.bg_red))
                wg_get_ma.isEnabled=true
            }

            override fun onTick(millisUntilFinished: Long) {
                wg_get_ma.text = "已发送(${millisUntilFinished/1000})s"
                wg_get_ma.setTextColor(this@SmsLoginActivity.resources.getColor(R.color.colorAccent))
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.onFinish()
        countDownTimer?.cancel()
    }

    companion object {
        fun launcher(fromActivity:Activity){
            fromActivity.startActivity(Intent(fromActivity,SmsLoginActivity::class.java))
        }
    }

}
