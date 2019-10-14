package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_forget.*
import retrofit2.Call
import retrofit2.Response

class ForgetPassActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var countDownTimer:CountDownTimer?=null
    companion object {
        fun  launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,ForgetPassActivity::class.java))
        }
    }
    override fun getContentId(): Int= R.layout.activity_forget

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    override fun initDatas(savedInstanceState: Bundle?) {
    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("修改密码",R.color.text_gray)
            .setLeftImg(R.drawable.ic_back)
            .setImgShow(true,false)
    }
    /**事件绑定*/
    override fun bindEvent() {
        /**确认修改*/
        wg_btn_sure.setOnClickListener {
            //空提示
            if (wg_ed_phone.text.isNullOrBlank()){
                showInfo("手机号不能为空")
                return@setOnClickListener
            }
            if (wg_ed_pass.text.isNullOrBlank()){
                showInfo("密码不能为空")
                return@setOnClickListener
            }
            if (wg_get_ma.text.isNullOrBlank()){
                showInfo("请输入验证码")
                return@setOnClickListener
            }
            WaitDialog.show(this,"修改中..")
            forgetApi(wg_ed_pass.text.toString(),wg_ed_pass.text.toString(),wg_get_ma.text.toString())
        }
        /**获取验证码*/
        wg_get_ma.setOnClickListener {
            sendSms(wg_ed_pass.text.toString())
        }
    }
    /**
     * 修改密码Api
     */
    private fun forgetApi(phone:String,pass:String,smsCode:String){
        UserRemoteModel.getUserApi().forget(phone,pass,smsCode)
            .enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    WaitDialog.dismiss()
                    showInfo(response?.body()?.resp_message?:"密码修改成功")
                    this@ForgetPassActivity.finish()
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@ForgetPassActivity,err,TipDialog.SHOW_TIME_SHORT,TipDialog.TYPE_ERROR)
                }

            })
    }

    private fun sendSms(phone:String){
        UserRemoteModel.getUserApi().sendSms(phone,"3")
            .enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    startCountDown()
                }

                override fun onFaile(err: String?) {
                    showInfo(err?:"未知错误")
                }
            })
    }

    fun startCountDown() {
        wg_get_ma.isEnabled=false
        countDownTimer = object : CountDownTimer(60000L, 1000L) {
            override fun onFinish() {
                wg_get_ma.text = "重新获取验证码"
                wg_get_ma.setTextColor(this@ForgetPassActivity.resources.getColor(R.color.bg_red))
                wg_get_ma.isEnabled=true
            }

            override fun onTick(millisUntilFinished: Long) {
                wg_get_ma.text = "已发送(${millisUntilFinished/1000})s"
                wg_get_ma.setTextColor(this@ForgetPassActivity.resources.getColor(R.color.colorAccent))
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.onFinish()
        countDownTimer?.cancel()
    }
}
