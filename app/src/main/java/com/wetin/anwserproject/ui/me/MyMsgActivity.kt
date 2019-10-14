package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.MsgAdapter
import com.wetin.anwserproject.bean.MsgBean
import com.wetin.anwserproject.bean.PagerBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.widget.EmptyView
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_my_msg.*
import retrofit2.Call
import retrofit2.Response

class MyMsgActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var currentPager = 0

    val mAdapter= MsgAdapter(null)
    override fun getContentId(): Int = R.layout.activity_my_msg

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        getTopLayout().setTitle("我的消息",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
    }

    override fun initWidget() {
        super.initWidget()
        mAdapter.setEnableLoadMore(true)
        mAdapter.emptyView = EmptyView(this, null).setInfo("暂时还没有评论")
        mAdapter.setOnLoadMoreListener({
            loadMoreData()
        },wg_recycle)
        wg_recycle.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        wg_recycle.adapter=mAdapter
        mAdapter.setOnItemClickListener { _, view, position ->
            val msgBean = mAdapter.data[position] as MsgBean
            MsgDescActivity.launcher(this,msgBean.messageTitle,msgBean.messageContent)
        }

        getData()
    }
    /**绑定事件*/
    override fun bindEvent() {
    }
    companion object {
        fun launcher(fromActivity: Activity){
            fromActivity.startActivity(Intent(fromActivity,MyMsgActivity::class.java))
        }
    }



    private fun getData() {
        WaitDialog.show(this,"加载中...")
        UserRemoteModel.getUserApi().getMsgList(number = currentPager)
            .enqueue(object : ResultCall<PagerBean<ArrayList<MsgBean>>> {
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<MsgBean>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<MsgBean>>>>
                ) {
                    mAdapter.setNewData(response?.body()?.result?.content)
                    currentPager=response?.body()?.result?.number!!
                    WaitDialog.dismiss()
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@MyMsgActivity,err)
                }
            })
    }

    private fun loadMoreData() {

        UserRemoteModel.getUserApi().getMsgList(number = currentPager + 1)
            .enqueue(object : ResultCall<PagerBean<ArrayList<MsgBean>>> {
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<MsgBean>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<MsgBean>>>>
                ) {
                    val content = response?.body()?.result?.content
                    if (content == null || content.isEmpty()) {
                        mAdapter.loadMoreEnd()
                    } else {
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


}
