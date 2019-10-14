package com.wetin.anwserproject.ui.me

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.tsy.sdk.social.PlatformType
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.UserBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.me.contract.LoginContract
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Response
import com.tsy.sdk.social.SocialApi
import com.tsy.sdk.social.listener.AuthListener
import com.wetin.anwserproject.model.keti.local.UserQuestLocalModel
import com.wetin.anwserproject.model.user.local.UserLocalModel


class LoginActivity : BaseAnswerActivity<LoginContract.Presenter>() {
    lateinit var mSocialApi: SocialApi
    override fun getContentId(): Int = R.layout.activity_login

    override fun initPresenter(): LoginContract.Presenter? = null

    companion object {
        fun launcher(fromActivity: Context) {
            fromActivity.startActivity(Intent(fromActivity, LoginActivity::class.java))
        }
    }

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("登录", R.color.text_gray)
            .setLeftImg(R.drawable.ic_back)
            .setImgShow(true, false)
            .setBackgroundColor(Color.WHITE)
        mSocialApi = SocialApi.get(this)
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_btn_register.setOnClickListener { RegisterActivity.launcher(this) }
        wg_quit_login.setOnClickListener { SmsLoginActivity.launcher(this) }
        wg_btn_forget.setOnClickListener { ForgetPassActivity.launcher(this) }

        //登录事件
        wg_login.setOnClickListener {
            if (wg_ed_pass.text.isEmpty()) {
                showInfo("密码不能为空")
                return@setOnClickListener
            }
            if (wg_ed_phone.text.isEmpty()) {
                showInfo("手机号不能为空")
                return@setOnClickListener
            }
            WaitDialog.show(this, "登录中...")
            UserRemoteModel.getUserApi()
                .login(phone = wg_ed_phone.text.toString(), passWord = wg_ed_pass.text.toString(), loginType = "1")
                .enqueue(object : ResultCall<UserBean> {
                    override fun onSuccess(call: Call<ResultBean<UserBean>>, response: Response<ResultBean<UserBean>>) {
                        WaitDialog.dismiss()
                        UserLocalModel.newInstanct().saveUser(response?.body()?.result!!)
                        UserLocalModel.newInstanct().saveLogin()
                        synchronousTiKuData(response?.body()?.result?.userId!!)
                        this@LoginActivity.finish()
                    }

                    override fun onFaile(err: String?) {
                        WaitDialog.dismiss()
                        TipDialog.show(this@LoginActivity, err, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                    }

                })
        }

        //微信登录
        wg_btn_wechat.setOnClickListener {
            WaitDialog.show(this, "登录中...")
            mSocialApi.doOauthVerify(this, PlatformType.WEIXIN, object : AuthListener {
                override fun onComplete(platform_type: PlatformType?, map: MutableMap<String, String>?) {
                    UserRemoteModel.getUserApi().wetChatLogin(map?.get("code").toString())
                        .enqueue(object : ResultCall<UserBean> {
                            override fun onSuccess(
                                call: Call<ResultBean<UserBean>>,
                                response: Response<ResultBean<UserBean>>
                            ) {
                                WaitDialog.dismiss()
                                UserLocalModel.newInstanct().saveUser(response?.body()?.result!!)
                                UserLocalModel.newInstanct().saveLogin()
                                synchronousTiKuData(response?.body()?.result?.userId!!)
                                this@LoginActivity.finish()
                            }

                            override fun onFaile(err: String?) {
                                WaitDialog.dismiss()
                                TipDialog.show(this@LoginActivity, err, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                            }

                        })
                }

                override fun onCancel(platform_type: PlatformType?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@LoginActivity, "登录取消", TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                }

                override fun onError(platform_type: PlatformType?, err_msg: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@LoginActivity, err_msg, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                }
            })
        }
    }

    /**
     * 同步题库游客数据到用户
     */
    fun synchronousTiKuData(userId: String) {
        UserQuestLocalModel.getInstance().visitorDataToUser(userId)
    }

}
