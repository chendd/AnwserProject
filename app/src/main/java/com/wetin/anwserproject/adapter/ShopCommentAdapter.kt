package com.wetin.anwserproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ShopComment
import com.wetin.common.utils.loadimg.ImageLoadUtil

class ShopCommentAdapter(data: MutableList<ShopComment>?) : BaseQuickAdapter<ShopComment, BaseViewHolder>(R.layout.item_shop_comment,data)  {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder?, item: ShopComment?) {
        helper?.apply {
            this.setText(R.id.wg_name,item?.nickName)
            this.setText(R.id.wg_time,item?.createTime)
            this.setText(R.id.wg_buy_time,item?.updateTime)
            this.setText(R.id.wg_content,item?.commentContent)
            if (item?.replayCommentContent?.isEmpty() != false){
                this.setGone(R.id.wg_repaly_content,false)
            }else{
                this.setText(R.id.wg_repaly_content,item?.replayCommentContent)
                this.setGone(R.id.wg_repaly_content,true)
            }
            ImageLoadUtil.instance.loadImg(mContext,this.getView(R.id.wg_img),item?.headImg)
        }

    }
}