package com.wetin.anwserproject.ui.home.fragment


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.blankj.utilcode.util.ToastUtils
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.TiKuCommentAdapter
import com.wetin.anwserproject.bean.Comment
import com.wetin.anwserproject.bean.PagerBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.home.PlayVideoActivity
import com.wetin.anwserproject.ui.me.LoginActivity
import com.wetin.anwserproject.widget.ActionPopu
import com.wetin.anwserproject.widget.EmptyView
import com.wetin.anwserproject.widget.InputDialog
import com.wetin.common.base.BaseFragment
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.fragment_comment.*
import retrofit2.Call
import retrofit2.Response


/**
 * 评论fragment<针对课程>
 */
class CommentFragment : BaseFragment<IBaseContract.BasePresenter>() {
    var currentPage = 0

    var mAdapter = TiKuCommentAdapter(null)
    override fun initPresenter(): IBaseContract.BasePresenter? = null
    /**setContent 之前处理*/
    override fun initSetContentBefore() {
    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_comment

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


    private fun loadMoreData() {
        XuekeRemoteModel.getKeTiApi().getCommentList(3, this.arguments!!.getString(KITU_ID), currentPage + 1)
            .enqueue(object : ResultCall<PagerBean<ArrayList<Comment>>> {
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<Comment>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<Comment>>>>
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

                override fun onFaile(err: String?) {
                    mAdapter.loadMoreFail()
                }

            })
    }


    /**绑定事件*/
    override fun bindEvent() {
        wg_edit_comment.setOnClickListener {
            if (UserLocalModel.newInstanct().isLogin()) {
                InputDialog(context).setType(this.arguments!!.getInt(TYPE), arguments?.getString(KITU_ID)!!, null)
                    .apply {
                        this.setOnDismissListener {
                            getCommentDataApi()
                        }
                    }.show()
            } else {
                LoginActivity.launcher(activity as PlayVideoActivity)
            }
        }
        mAdapter.setOnItemClickListener { adapter, view, position ->
            val actionPopu = ActionPopu(context)
            actionPopu.setlistener(View.OnClickListener {
                val comment = adapter.data[position] as Comment

                when (it.id) {
                    com.wetin.anwserproject.R.id.wg_btn_del -> {
                        deleteComment(comment.id, position)
                    }
                    com.wetin.anwserproject.R.id.wg_btn_fuzhi -> {
                        //获取剪贴板管理器：
                        val cm = context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val mClipData = ClipData.newPlainText("Label", comment.commentContent)
                        cm!!.primaryClip = mClipData
                        ToastUtils.showShort("成功复制")
                    }
                    R.id.wg_btn_replay -> {
                        if (UserLocalModel.newInstanct().isLogin()) {
                            InputDialog(context).setType(this.arguments!!.getInt(TYPE), arguments?.getString(KITU_ID)!!, comment.id.toString())
                                .apply {
                                    this.setOnDismissListener {
                                        getCommentDataApi()
                                    }
                                }.show()
                        } else {
                            LoginActivity.launcher(activity as PlayVideoActivity)
                        }
                    }
                }
                actionPopu.dismiss()
            })
            if ((adapter.data[position] as Comment).userId == UserLocalModel.newInstanct()?.getUserData()?.userId) {
                actionPopu.isShowDel(true).showAsDropDown(view, view.width / 3, -(view.height / 1.5).toInt())
            } else {
                actionPopu.isShowDel(false).showAsDropDown(view, view.width / 3, -(view.height / 1.5).toInt())
            }

        }
    }

    /**获取数据*/
    private fun getCommentDataApi() {
        XuekeRemoteModel.getKeTiApi()
            .getCommentList(this.arguments!!.getInt(TYPE), this.arguments!!.getString(KITU_ID), currentPage)
            .enqueue(object : ResultCall<PagerBean<ArrayList<Comment>>> {
                override fun onSuccess(
                    call: Call<ResultBean<PagerBean<ArrayList<Comment>>>>,
                    response: Response<ResultBean<PagerBean<ArrayList<Comment>>>>
                ) {
                    mAdapter.setNewData(response?.body()?.result?.content)
                    wg_load?.visibility = View.GONE
                }

                override fun onFaile(err: String?) {
                    wg_load?.text = "err"
                }
            })
    }


    private fun deleteComment(id: Int, position: Int) {
        XuekeRemoteModel.getKeTiApi().delComment(id)
            .enqueue(object : ResultCall<String> {
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    mAdapter.remove(position)
                }

                override fun onFaile(err: String?) {
                    ToastUtils.showShort(err)
                }
            })
    }

    companion object {
        const val KITU_ID = "tikuId"
        const val TYPE = "type"
        fun newIntance(id: String, type: Int): CommentFragment {
            val commentFragment = CommentFragment()
            val bundle = Bundle()
            bundle.putString(KITU_ID, id)
            bundle.putInt(TYPE, type)
            commentFragment.arguments = bundle
            return commentFragment
        }
    }
}
