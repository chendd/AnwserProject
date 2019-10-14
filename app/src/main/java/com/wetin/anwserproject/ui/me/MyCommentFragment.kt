package com.wetin.anwserproject.ui.me


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog

import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.Comment
import com.wetin.anwserproject.bean.PagerBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.fragment.contract.VollFragContranct
import com.wetin.anwserproject.widget.EmptyView
import com.wetin.common.base.BaseFragment
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_my_comment.*
import retrofit2.Call
import retrofit2.Response

/**
 * 我的评论
 */
class MyCommentFragment : BaseFragment<VollFragContranct.Presenter>() {
    var currentPager = 0

    var mAdapter=MyCommentAdapter(null)

    override fun initPresenter(): VollFragContranct.Presenter? = null

    /**setContent 之前处理*/
    override fun initSetContentBefore() {
    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_my_comment

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    /**初始化组件*/
    override fun initWidget() {
        mAdapter.setEnableLoadMore(true)
        mAdapter.emptyView = EmptyView(context!!, null).setInfo("暂时还没有评论")
        mAdapter.setOnLoadMoreListener({
            loadMoreData()
        },wg_recycle)
        mAdapter.setPreLoadNumber(4)
        wg_recycle.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        wg_recycle.adapter= mAdapter

        getData()
    }

    /**绑定事件*/
    override fun bindEvent() {
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            if (view.id==R.id.wg_check_ti){
                //跳转题目
            }
        }
    }


    private fun getData() {
        WaitDialog.show(context,"加载中...")
        UserRemoteModel.getUserApi().myComment(arguments!!.getInt(INTENT_INDEX),currentPager)
            .enqueue(object :ResultCall<PagerBean<ArrayList<Comment>>>{
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<Comment>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<Comment>>>>
                ) {
                    mAdapter.setNewData(response?.body()?.result?.content)
                    currentPager=response?.body()?.result?.number!!
                    WaitDialog.dismiss()
                }

                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(context,err)
                }
            })
    }

    private fun loadMoreData() {
        UserRemoteModel.getUserApi().myComment(arguments!!.getInt(INTENT_INDEX),currentPager+1)
            .enqueue(object :ResultCall<PagerBean<ArrayList<Comment>>>{
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<Comment>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<Comment>>>>
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
        const val INTENT_INDEX="index"
        fun newIntance(id:Int):MyCommentFragment{
            val commentFragment = MyCommentFragment()
            val bundle = Bundle()
            bundle.putInt(INTENT_INDEX,id)
            commentFragment.arguments=bundle
            return commentFragment
        }
    }

    class MyCommentAdapter(data: MutableList<Comment>?) :
        BaseQuickAdapter<Comment, BaseViewHolder>(R.layout.item_my_comment, data) {
        override fun convert(helper: BaseViewHolder?, item: Comment?) {
            helper?.apply {
                this.setText(R.id.wg_name,item?.nickName)
                this.setText(R.id.wg_from,"${item?.specialtySchool}  ${item?.createTime}")
                this.setText(R.id.wg_context,item?.commentContent)
                ImageLoadUtil.instance.loadCircleImg(mContext,helper?.getView(R.id.wg_iv_head)!!,item?.headImg)
            }
        }

    }
}
