package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.OrderBean
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_order_desc.*

class OrderDescActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    override fun getContentId(): Int = R.layout.activity_order_desc

    override fun initPresenter(): IBaseContract.BasePresenter? =null
    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("订单详情",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
    }

    /**绑定事件*/
    override fun bindEvent() {
    }

    override fun initWidget() {
        super.initWidget()
        val orderBean = intent.getSerializableExtra("data") as OrderBean
        wg_shop_name.text=orderBean.title
        wg_shop_num.text="1"
        wg_shop_id.text=orderBean.orderId
        wg_shop_time.text=orderBean.vipTimeout
        wg_shop_pay_time.text=orderBean.createTime
        wg_shop_pay.text="￥${orderBean.transAmt}"
    }
    companion object {
        fun launcher(fromActivity: Activity,data: OrderBean){
            val intent = Intent(fromActivity, OrderDescActivity::class.java)
            intent.putExtra("data",data)
            fromActivity.startActivity(intent)
        }
    }
}
