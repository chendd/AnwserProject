package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.app.ActivityManager
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.WindowId
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.ShopDesc
import com.wetin.anwserproject.bean.ShopVipTime
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.me.PayOrderActivity
import com.wetin.anwserproject.ui.shop.ShopDescActivity
import com.wetin.common.base.IBaseContract
import com.wetin.common.base.ViewManager
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.activity_order.*
import retrofit2.Call
import retrofit2.Response

/**
 * 提交订单
 */
class OrderActivity : BaseAnswerActivity<IBaseContract.BasePresenter>()  {
    lateinit var shopVipTime:ShopVipTime
    override fun getContentId(): Int =R.layout.activity_order

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("确认订单",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))

        shopVipTime = intent.getSerializableExtra(INTENT_SHOP_TIME) as ShopVipTime
        if (shopVipTime!=null){
            wg_day.text=shopVipTime.desc
            wg_title.text="${shopVipTime.buyTime}"
            wg_price.text="价格:${shopVipTime.transAmt}"
            wg_pay_privice.text="合计:${shopVipTime.transAmt}"
        }
        ImageLoadUtil.instance.loadImg(this,imageView,intent.getStringExtra(INTENT_IMG))
        if (intent.getIntExtra(INTENT_TYPE,1)!=1){
            wg_time_bar.visibility=View.GONE
        }
        wg_title.text = intent.getStringExtra(INTENT_SHOP)
        getVipTime(shopVipTime)

    }
    /**绑定事件*/
    override fun bindEvent() {
        wg_btn_submit.setOnClickListener {
            PayOrderActivity.launcher(this, intent.getIntExtra("id",0),intent.getStringExtra(INTENT_SHOP),shopVipTime)
            ViewManager.instance.finishActivity(ShopDescActivity::class.java)
            this.finish()
        }
    }



    private fun getVipTime(shopVipTime:ShopVipTime){
        XuekeRemoteModel.getKeTiApi().viptime(intent.getIntExtra(INTENT_TYPE,1),shopVipTime.buyTime)
            .enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    wg_time.text=response?.body()?.result
                }

                override fun onFaile(err: String?) {
                }

            })
    }

    companion object {
        val INTENT_SHOP_TIME="shop_time"
        val INTENT_SHOP="shop_name"
        val INTENT_IMG="shop_img"
        val INTENT_TYPE="type"
        fun launcher(fromActivity: Activity,time:ShopVipTime?=null,shop_name:String,img:String,type:Int,id: Int){
            val intent = Intent(fromActivity, OrderActivity::class.java)
            intent.putExtra(INTENT_SHOP_TIME,time)
            intent.putExtra(INTENT_SHOP,shop_name)
            intent.putExtra(INTENT_IMG,img)
            intent.putExtra(INTENT_TYPE,type)
            intent.putExtra("id",id)
            fromActivity.startActivity(intent)
        }
    }
}
