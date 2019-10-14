package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.ShopVipTime
import com.wetin.anwserproject.bean.WeChatPayInfo
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.ApiManager
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.pay.PayUtils
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.utils.MD5Utils
import com.wetin.anwserproject.utils.SystemUtils
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_pay_order.*
import retrofit2.Call
import retrofit2.Response

class PayOrderActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    lateinit var shopVip:ShopVipTime
    override fun getContentId(): Int=R.layout.activity_pay_order

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("订单支付",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
    }

    override fun initWidget() {
        super.initWidget()
        shopVip = intent.getSerializableExtra("shopVipTime") as ShopVipTime
        wg_title.setText(intent.getStringExtra("title"))
        wg_time.setText(shopVip.desc)
        wg_price.setText("价格:${shopVip.transAmt}")
    }
    /**绑定事件*/
    override fun bindEvent() {
        wg_alipay_pay.setOnClickListener {
            WaitDialog.show(this,"稍等...")
            val times = System.currentTimeMillis().toString()
            val random = (Math.random() * 90000 + 10000).toInt()
            val timestamp=times+random.toString()
            var key="buyTime=${shopVip.buyTime}&payType=1&shopId=${intent.getIntExtra("id",0)}&timestamp=${timestamp}&transAmt=${shopVip.transAmt}&key=elv1qpdaklsdfjiwejlknxvjsfjeksnad.a*i/sdwvufbwdgo9dc2w8qi9b6o902gaq"
            LogUtils.a(key)
            UserRemoteModel.getUserApi().createAlipayOrder(intent.getIntExtra("id",0),
                shopVip.buyTime.toString(),transAmt = shopVip.transAmt.toString(),timestamp =timestamp,
                sign = MD5Utils.MD5Encode(key,"utf-8")
                ).enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    PayUtils.getInstance(this@PayOrderActivity).toAliPay(this@PayOrderActivity,response?.body()?.result)
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@PayOrderActivity,err)
                }

            })
        }
        wg_wechat_pay.setOnClickListener {
            WaitDialog.show(this,"稍等...")
            val times = System.currentTimeMillis().toString()
            val random = (Math.random() * 90000 + 10000).toInt()
            val timestamp=times+random.toString()
            var key="buyTime=${shopVip.buyTime}&payType=2&shopId=${intent.getIntExtra("id",0)}&timestamp=${timestamp}&transAmt=${shopVip.transAmt}&key=elv1qpdaklsdfjiwejlknxvjsfjeksnad.a*i/sdwvufbwdgo9dc2w8qi9b6o902gaq"
            UserRemoteModel.getUserApi().createWechatOrder(intent.getIntExtra("id",0),
                shopVip.buyTime.toString(),transAmt = shopVip.transAmt.toString(),timestamp =timestamp,
                sign = MD5Utils.MD5Encode(key,"utf-8")
            ).enqueue(object :ResultCall<WeChatPayInfo>{
                override fun onSuccess(call: Call<ResultBean<WeChatPayInfo>>, response: Response<ResultBean<WeChatPayInfo>>) {
                    PayUtils.getInstance(this@PayOrderActivity).toWXPay(response?.body()?.result)
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@PayOrderActivity,err)
                }

            })
        }
    }

    companion object {
        fun launcher(fromActivity: Activity,id:Int,title:String,shopVipTime: ShopVipTime){
            val intent = Intent(fromActivity, PayOrderActivity::class.java)
            intent.putExtra("title",title)
            intent.putExtra("id",id)
            intent.putExtra("shopVipTime",shopVipTime)
            fromActivity.startActivity(intent)
        }
    }
}
