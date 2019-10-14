package com.wetin.anwserproject.ui.shop.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ShopBannerImg
import com.wetin.anwserproject.bean.ShopInfoImg
import com.wetin.common.base.BaseFragment
import com.wetin.common.base.IBaseContract
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_shop_img.*

class ShopImgFragment :  BaseFragment<IBaseContract.BasePresenter>() {
    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**setContent 之前处理*/
    override fun initSetContentBefore() {
    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_shop_img

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    /**初始化组件*/
    override fun initWidget() {
        wg_recycle.layoutManager=LinearLayoutManager(context!!,LinearLayoutManager.VERTICAL,false)
        wg_recycle.adapter=ImgAdapter(this.arguments!!.getSerializable(INTENT_DATA) as ArrayList<ShopInfoImg>)
    }

    /**绑定事件*/
    override fun bindEvent() {
    }


    companion object {
        const val INTENT_DATA="data"
        fun newInstance(data:ArrayList<ShopInfoImg>):ShopImgFragment{
            val args = Bundle()
            args.putSerializable(INTENT_DATA,data)
            val fragment = ShopImgFragment()
            fragment.arguments = args
            return fragment
        }
    }

    class ImgAdapter(data: MutableList<ShopInfoImg>?) : BaseQuickAdapter<ShopInfoImg, BaseViewHolder>(R.layout.item_img,data) {
        override fun convert(helper: BaseViewHolder?, item: ShopInfoImg?) {
           // Glide.with(mContext).load(item?.imgUrl).into(helper?.getView(R.id.wg_img)!!)
           ImageLoadUtil.instance.loadImg(mContext,helper?.getView(R.id.wg_img)!!,item?.imgUrl)
        }

    }
}
