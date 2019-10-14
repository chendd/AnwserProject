package com.wetin.anwserproject.ui.shop.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.ShopCommentAdapter
import com.wetin.anwserproject.bean.PagerBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.ShopComment
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.Response
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.widget.EmptyView
import com.wetin.common.base.BaseFragment
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.fragment_shop_comment.*
import retrofit2.Call


class ShopCommentFragment : BaseFragment<IBaseContract.BasePresenter>() {
    var currentPage = 0
    val mAdapter=ShopCommentAdapter(null)
    override fun initPresenter(): IBaseContract.BasePresenter?=null

    /**setContent 之前处理*/
    override fun initSetContentBefore() {
    }

    /**布局*/
    override fun getContentLayout(): Int=R.layout.fragment_shop_comment

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getCommentDataApi()
    }

    /**初始化组件*/
    override fun initWidget() {
        wg_recycle.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        wg_recycle.adapter = mAdapter
        mAdapter.setEnableLoadMore(true)
        mAdapter.emptyView = EmptyView(context!!, null).setInfo("暂时还没有评论")
        mAdapter.setOnLoadMoreListener({
            loadMoreData()
        }, wg_recycle)
        mAdapter.setPreLoadNumber(4)
    }

    /**绑定事件*/
    override fun bindEvent() {
    }




    private fun loadMoreData() {
        XuekeRemoteModel.getKeTiApi().shopCommentList(arguments?.getString(INTENT_SHOP_ID)!!, currentPage + 1)
            .enqueue(object : ResultCall<PagerBean<ArrayList<ShopComment>>> {
                override fun onFaile(err: String?) {
                    mAdapter.loadMoreFail()
                }

                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<ShopComment>>>>,
                    response: retrofit2.Response<ResultBean<PagerBean<ArrayList<ShopComment>>>>
                ) {
                    val content = response?.body()?.result?.content
                    if (content == null || content.isEmpty()) {
                        mAdapter.loadMoreEnd()
                    } else {
                        mAdapter.addData(content)
                        mAdapter.loadMoreComplete()
                        currentPage = response?.body()?.result?.number!!
                    }
                }
            })
    }


    /**获取数据*/
    private fun getCommentDataApi() {
        XuekeRemoteModel.getKeTiApi().shopCommentList(arguments?.getString(INTENT_SHOP_ID)!!, currentPage)
            .enqueue(object : ResultCall<PagerBean<ArrayList<ShopComment>>> {
                override fun onFaile(err: String?) {
                    wg_load?.text = "err"
                }

                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<ShopComment>>>>,
                    response: retrofit2.Response<ResultBean<PagerBean<ArrayList<ShopComment>>>>
                ) {
                    mAdapter.setNewData(response?.body()?.result?.content)
                    wg_load?.visibility = View.GONE
                }
    })
    }
    companion object {
         val INTENT_SHOP_ID="shopId"
        fun newInstance(shopId:String):ShopCommentFragment{
            val args = Bundle()
            args.putString(INTENT_SHOP_ID,shopId)
            val fragment = ShopCommentFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
