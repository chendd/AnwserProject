package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.OrderAdapter
import com.wetin.anwserproject.bean.OrderBean
import com.wetin.anwserproject.bean.PagerBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.widget.EmptyView
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_my_order.*
import retrofit2.Call
import retrofit2.Response

class MyOrderActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var currentPager = 0
    val mAdapter=OrderAdapter(null)
    override fun getContentId(): Int= R.layout.activity_my_order

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("我的订单",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
    }

    override fun initWidget() {
        super.initWidget()
        mAdapter.setEnableLoadMore(true)
        mAdapter.emptyView = EmptyView(this, null).setInfo("还没有订单")
        mAdapter.setOnLoadMoreListener({
            loadMoreData()
        },wg_recycle)
        mAdapter.setPreLoadNumber(4)
        wg_recycle.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        wg_recycle.adapter= mAdapter

        getData()
    }
    /**绑定事件*/
    override fun bindEvent() {
        mAdapter.setOnItemClickListener { _, view, position ->
            OrderDescActivity.launcher(this,mAdapter.data[position] as OrderBean)
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            //评价晒单,已评价
            EditCommentActivity.launcher(this,adapter.data[position] as OrderBean)
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==1&&requestCode==1){
            getData()
        }
    }


    private fun getData() {
        WaitDialog.show(this,"加载中...")
        UserRemoteModel.getUserApi().getOrderList(currentPager)
            .enqueue(object : ResultCall<PagerBean<ArrayList<OrderBean>>> {
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<OrderBean>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<OrderBean>>>>
                ) {
                    mAdapter.setNewData(response?.body()?.result?.content)
                    currentPager=response?.body()?.result?.number!!
                    WaitDialog.dismiss()
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@MyOrderActivity,err)
                }
            })
    }

    private fun loadMoreData() {
        UserRemoteModel.getUserApi().getOrderList(currentPager+1)
            .enqueue(object :ResultCall<PagerBean<ArrayList<OrderBean>>>{
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<OrderBean>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<OrderBean>>>>
                ) {
                    val content = response?.body()?.result?.content
                    if (content==null||content.isEmpty()){
                        mAdapter.loadMoreEnd()
                    }else {
                        mAdapter.addData(content)
                        mAdapter.loadMoreComplete()
                        currentPager = response?.body()?.result?.number!!
                    }
                }
                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    mAdapter.loadMoreFail()
                }

            })
    }

    companion object {
        fun launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,MyOrderActivity::class.java))
        }
    }
}
