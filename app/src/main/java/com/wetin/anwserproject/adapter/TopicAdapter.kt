package com.wetin.anwserproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ruffian.library.widget.RTextView
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.table.ContentBean

class TopicAdapter(datas:ArrayList<ContentBean>?): BaseQuickAdapter<ContentBean, BaseViewHolder>(R.layout.item_topic,datas) {
    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param helper A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    override fun convert(helper: BaseViewHolder?, item: ContentBean?) {
        helper?.apply {
            helper.setText(R.id.wg_top_mun,"${item?.questionNumber}")
            //查询本地进行对错状态显示
            val rTextView = helper.getView<RTextView>(R.id.wg_top_mun)
            if (item?.userHistoryQuest!=null&&item?.userHistoryQuest?.answerStatus==2){
                rTextView.helper.backgroundColorNormal = mContext.resources.getColor(R.color.bg_red)
                rTextView.helper.backgroundColorPressed = mContext.resources.getColor(R.color.bg_red)
                rTextView.setTextColor(mContext.resources.getColor(R.color.white))
            }else if (item?.userHistoryQuest!=null&&item?.userHistoryQuest?.answerStatus==1){
                rTextView.helper.backgroundColorNormal = mContext.resources.getColor(R.color.bg_green)
                rTextView.helper.backgroundColorPressed = mContext.resources.getColor(R.color.bg_green)
                rTextView.setTextColor(mContext.resources.getColor(R.color.white))
            }else{
                rTextView.helper.backgroundColorNormal = mContext.resources.getColor(R.color.line_color)
                rTextView.helper.backgroundColorPressed = mContext.resources.getColor(R.color.line_color)
                rTextView.setTextColor(mContext.resources.getColor(R.color.text_gray))
            }

        }
    }



}