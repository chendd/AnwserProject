package com.wetin.anwserproject.ui.fragment


import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.wetin.anwserproject.MainActivity
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.UserBean
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.ui.fragment.contract.MeFragContranct
import com.wetin.anwserproject.ui.me.*
import com.wetin.anwserproject.utils.CountDownUtil
import com.wetin.anwserproject.widget.ServiceDialog
import com.wetin.common.base.BaseFragment
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_me.*
import java.util.*

/**
 * 首页中《我的》fragment
 */
class MeFragment : BaseFragment<MeFragContranct.Presenter>() {
    var userBean:UserBean?=null
    override fun initPresenter(): MeFragContranct.Presenter? = null

    //考试时间 时间戳
    var testTime = 1552148223000
    //倒计时组件集合
    lateinit var CountDownWg: kotlin.Array<TextView>

    /**setContent 之前处理*/
    override fun initSetContentBefore() {
    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_me

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        userBean=UserLocalModel.newInstanct().getUserData()
    }

    /**初始化组件*/
    override fun initWidget() {
        CountDownWg = arrayOf(wg_tv_hundreds, wg_tv_ten, wg_tv_ge, wg_tv_tentime, wg_tv_getime)
    }

    /**绑定事件*/
    override fun bindEvent() {
        /**点击登录*/
        wg_no_user.setOnClickListener {
            LoginActivity.launcher(activity as MainActivity)
        }
        /**订单*/
        wg_rl_order.setOnClickListener { MyOrderActivity.launcher(activity as MainActivity)}
        /**客服*/
        wg_rl_service.setOnClickListener { ServiceDialog(context).show() }
        /**我的资料*/
        wg_item_meterial.setOnClickListener {
            if(!UserLocalModel.newInstanct().isLogin()){
                LoginActivity.launcher(activity as MainActivity)
                return@setOnClickListener
            }
            UserInfoActivity.launcher(context!!)
        }
        /**我的消息*/
        wg_item_msg.setOnClickListener { MyMsgActivity.launcher(activity as MainActivity)}
        /**我的评论*/
        wg_item_comemt.setOnClickListener {
            MyCommentActivity.launcher(activity as MainActivity)
        }
        /**我的设置*/
        wg_item_set.setOnClickListener { SettingActivity.launcher(activity as MainActivity)}
    }

    companion object {
        fun newInstance(): MeFragment {
            val args = Bundle()
            val fragment = MeFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onResume() {
        super.onResume()
        changeUiByLogin()
        setCountDownUi()
    }


    private fun setCountDownUi() {
        val diffCountDown = diffCountDown()
        for (index in diffCountDown!!.indices) {
            CountDownWg[index].text = diffCountDown[index].toString()
        }
    }


    /**
     * 计算倒计时
     */
    private fun diffCountDown(): ArrayList<Char>? {
        val currentTimeMillis= System.currentTimeMillis()
        testTime= CountDownUtil.dateToStamp("${userBean?.testYear?:2019}-03-09 00:00:00").toLong()
        //比较当前时间，大于比较值，加下一年
        if (testTime < currentTimeMillis) {
            val cal = Calendar.getInstance()
            cal.time = Date(testTime)//设置起时间
            cal.add(Calendar.YEAR, 1)//增加一年
            testTime = cal.timeInMillis
            wg_tv_testime.text="距离考试${cal.get(Calendar.YEAR)}年考试还有"
        }else{
            wg_tv_testime.text="距离考试${userBean?.testYear?:2019}年考试还有"
        }
        return CountDownUtil.getTimeDifference(testTime, currentTimeMillis)
    }


    private fun changeUiByLogin() {
        userBean=UserLocalModel.newInstanct().getUserData()
        if (UserLocalModel.newInstanct().isLogin()) {
            wg_tv_name.text=userBean?.nickName
            ImageLoadUtil.instance.loadCircleImg(context!!, wg_iv_head,userBean?.headImg)
            wg_login_user.visibility = View.VISIBLE
            wg_no_user.visibility = View.GONE
            wg_rl_oder_service.visibility = View.VISIBLE
        } else {
            wg_login_user.visibility = View.GONE
            wg_no_user.visibility = View.VISIBLE
            wg_rl_oder_service.visibility = View.GONE
        }
    }
}
