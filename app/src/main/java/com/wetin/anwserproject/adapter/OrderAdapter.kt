package com.wetin.anwserproject.adapter

import android.graphics.Color
import android.support.v4.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruffian.library.widget.RTextView
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.OrderBean
import com.wetin.common.utils.loadimg.ImageLoadUtil

class OrderAdapter(data: MutableList<OrderBean>?) : BaseQuickAdapter<OrderBean, BaseViewHolder>(R.layout.item_order,data) {

    override fun convert(helper: BaseViewHolder?, item: OrderBean?) {
        helper?.apply {
            this.setText(R.id.wg_order_num,"订单编号:${item?.orderId}")
            this.setText(R.id.wg_time,"${item?.createTime}")
            this.setText(R.id.wg_title,"${item?.title}")
            this.setText(R.id.wg_info,"${item?.desc}")
            this.setText(R.id.wg_time_youxaio,"有效期:${item?.vipTimeout}")
            this.setText(R.id.wg_beizhu,"共1件商品，已付款￥${item?.transAmt}")
            ImageLoadUtil.instance.loadImg(mContext,this.getView(R.id.wg_img),item?.smallImg)
            if (item?.isComment==1){
                helper.setTextColor(R.id.wg_sure,Color.parseColor("#999999"))
                helper?.getView<RTextView>(R.id.wg_sure).helper.setBackgroundColorNormal(Color.parseColor("#ffffff")).backgroundColorPressed =
                    Color.parseColor("#ffffff")
                helper?.setText(R.id.wg_sure,"已评价")
                helper?.setTextColor(R.id.wg_sure,ContextCompat.getColor(mContext,R.color.bg_red))
            }else{
                helper.setTextColor(R.id.wg_sure,ContextCompat.getColor(mContext,R.color.bg_red))
                helper?.getView<RTextView>(R.id.wg_sure).helper.setBackgroundColorNormal(ContextCompat.getColor(mContext,R.color.bg_red)).backgroundColorPressed =
                    ContextCompat.getColor(mContext,R.color.bg_red)
                helper?.setText(R.id.wg_sure,"评价晒单")
                helper?.setTextColor(R.id.wg_sure,ContextCompat.getColor(mContext,R.color.white))
                this.addOnClickListener(R.id.wg_sure)
            }
        }
    }
}