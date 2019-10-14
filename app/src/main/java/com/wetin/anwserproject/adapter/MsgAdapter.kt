package com.wetin.anwserproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.MsgBean
import com.wetin.anwserproject.bean.OrderBean

class MsgAdapter(data: MutableList<MsgBean>?) : BaseQuickAdapter<MsgBean, BaseViewHolder>(R.layout.item_msg,data) {

    override fun convert(helper: BaseViewHolder?, item: MsgBean?) {
        helper?.apply {
            this.setText(R.id.wg_time,item?.updateTime)
            this.setText(R.id.wg_msg_title,item?.messageTitle)
            this.setText(R.id.wg_msg_content,item?.messageContent)
        }
    }
}