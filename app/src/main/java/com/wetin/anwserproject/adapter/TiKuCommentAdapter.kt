package com.wetin.anwserproject.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.Comment
import com.wetin.common.utils.loadimg.ImageLoadUtil

/**
 * 关于题目的评论适配器
 */
class TiKuCommentAdapter(data: MutableList<Comment>?) : BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.item_comment,data) {

    override fun convert(helper: BaseViewHolder?, item: Comment?) {
        helper?.apply {
            if(item?.replayCommentContent!=null){
                this.setGone(R.id.wg_replay_bar,true)
                this.setText(R.id.wg_replay_name,item?.replayNickName)
                this.setText(R.id.wg_repaly_time,"${item?.replaySpecialtySchool}  ${item?.replayCreateTime}")
                this.setText(R.id.wg_replay_content,item?.replayCommentContent)
            }else{
                this.setGone(R.id.wg_replay_bar,false)
            }
            this.setText(R.id.wg_name,item?.nickName)
            this.setText(R.id.wg_from,"${item?.specialtySchool}  ${item?.createTime}")
            this.setText(R.id.wg_prase_count,"${item?.countDigg}")
            this.setText(R.id.wg_context,item?.commentContent)
            ImageLoadUtil.instance.loadCircleImg(mContext,helper?.getView(R.id.wg_iv_head)!!,item?.headImg)
        }
    }
}