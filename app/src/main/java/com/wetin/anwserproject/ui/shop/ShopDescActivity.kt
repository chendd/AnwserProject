package com.wetin.anwserproject.ui.shop

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.ShopDesc
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.home.OrderActivity
import com.wetin.anwserproject.ui.shop.fragment.ShopCommentFragment
import com.wetin.anwserproject.ui.shop.fragment.ShopImgFragment
import com.wetin.anwserproject.utils.GlideImageLoader
import com.wetin.common.base.IBaseContract
import com.youth.banner.listener.OnBannerListener
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import com.zhy.view.flowlayout.TagFlowLayout
import kotlinx.android.synthetic.main.activity_shop_desc.*
import retrofit2.Call
import retrofit2.Response

/**
 * 商品详情页
 */
class ShopDescActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var selectPo = 0
    var shopDesc:ShopDesc?=null
    override fun getContentId(): Int = R.layout.activity_shop_desc

    override fun initPresenter(): IBaseContract.BasePresenter? = null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {

    }


    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("商品", R.color.white)
            .setImgShow(true, false)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.bg_red))
        getShopApi()
        //banner
        wg_banner.apply {
            this.setIndicatorGravity(Gravity.BOTTOM)
            this.setImageLoader(GlideImageLoader())
            this.setDelayTime(5000)
        }


    }

    /**绑定事件*/
    override fun bindEvent() {
        //购买
        wg_btn_buy.setOnClickListener {
            //跳转确认订单
            OrderActivity.launcher(
                this,
                shopDesc?.shopVipTime?.get(selectPo),
                shopDesc?.title?:"",
                shopDesc?.shopBannerImg?.get(0)?.imgUrl ?: "",
                shopDesc?.type!!,
                shopDesc?.id?:0
            )
        }
        wg_tag_flow.setOnSelectListener { selectPosSet -> this@ShopDescActivity.selectPo = selectPosSet?.first()!! }
    }


    fun getShopApi() {
        WaitDialog.show(this, "加载中..")
        XuekeRemoteModel.getKeTiApi().getShopDesc(
            this.intent.getIntExtra(INTENT_TYPE, 0), this.intent.getStringExtra(
                INTENT_PARENDID
            )
        )
            .enqueue(object : ResultCall<ShopDesc> {
                override fun onSuccess(call: Call<ResultBean<ShopDesc>>, response: Response<ResultBean<ShopDesc>>) {
                    WaitDialog.dismiss()
                    showDataView(response?.body()?.result)
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@ShopDescActivity, err)
                }
            })
    }


    private fun showDataView(shopDesc: ShopDesc?) {
        if (shopDesc == null) {
            return
        }
        this.shopDesc=shopDesc
        wg_shop_name.text = shopDesc.title
        wg_shop_price.text = "￥${shopDesc.priceMin}-${shopDesc.priceMax}"
        wg_shop_yprice.text = shopDesc.oriPrice.toString()
        wg_shop_count.text = "已销售${shopDesc.saleCount}"
        var times = ArrayList<String>()
        for (time in shopDesc.shopVipTime) {
            times.add(time.desc)
        }
        if (times.size > 0) {
            wg_tag_flow.adapter = object : TagAdapter<String>(times) {
                override fun getView(parent: FlowLayout?, position: Int, t: String?): View {
                    val textView = this@ShopDescActivity.layoutInflater.inflate(R.layout.item_tag, null) as TextView
                    textView.text = t
                    return textView
                }
            }
        }
        var images = ArrayList<String>()
        shopDesc.shopBannerImg?.forEach { shopBanner ->
            images.add(shopBanner.imgUrl)
        }
        wg_banner.setImages(images)
        wg_banner.setImageLoader(GlideImageLoader())
        wg_banner.start()
        wg_banner.setOnBannerListener {
            //跳转h5
        }



        wg_tablayout.setupWithViewPager(wg_viewpager)
        wg_viewpager.adapter = FragAdapter(
            supportFragmentManager, arrayOf("商品详情", "学员评价(${shopDesc.commentCount})"),
            arrayListOf(
                ShopImgFragment.newInstance(shopDesc.shopInfoImg),
                ShopCommentFragment.newInstance(shopDesc.id.toString())
            )
        )
    }


    class FragAdapter(fm: FragmentManager?, var title: Array<String>, var fragDasta: ArrayList<Fragment>) :
        FragmentPagerAdapter(fm) {
        override fun getItem(p0: Int): Fragment {
            return fragDasta[p0]
        }

        override fun getCount(): Int {
            return title.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return title[position]
        }

    }

    companion object {
        const val INTENT_TYPE = "type"
        const val INTENT_PARENDID = "parendId"
        fun launcher(fromActivity: Activity, type: Int, parendId: String?) {
            val intent = Intent(fromActivity, ShopDescActivity::class.java)
            intent.putExtra(INTENT_TYPE, type)
            intent.putExtra(INTENT_PARENDID, parendId)
            fromActivity.startActivity(intent)
        }
    }
}
