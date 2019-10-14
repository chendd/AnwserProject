package com.wetin.anwserproject.widget

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import com.blankj.utilcode.util.ToastUtils
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import kotlinx.android.synthetic.main.layout_edit_min.*
import retrofit2.Call
import retrofit2.Response

class InputDialog(context: Context?) : Dialog(context, R.style.inputDialog) {
    private var type:Int=0
    private var questionId:String?=null
    private var replayCommentId:String?=null
    init {
         setContentView(R.layout.layout_edit_min)

     }

    override fun onCreate(savedInstanceState: Bundle?) {
        val layoutParams = window!!.attributes
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        window!!.attributes = layoutParams
        super.onCreate(savedInstanceState)
        initEvent()
    }

    private fun initEvent() {
        wg_ed_send.setOnClickListener {
            if (wg_ed_content.text.toString().isNullOrEmpty()){
                ToastUtils.showShort("请输入内容")
                return@setOnClickListener
            }
            wg_ed_send.text="发送中"
            wg_ed_send.isEnabled=false
            postMsg()
        }
    }

    /***
     * 评论类型 1 、题库 2、音频 3、视频
     */
    fun setType(type:Int,questionId:String,replayCommentId:String?=null):InputDialog{
        this.type=type
        this.questionId=questionId
        this.replayCommentId=replayCommentId
        return this
    }


    private fun postMsg(){
        XuekeRemoteModel.getKeTiApi().addComment(type,questionId!!,wg_ed_content.text.toString(),replayCommentId)
            .enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    wg_ed_send.isEnabled=true
                    ToastUtils.showShort("评论成功")
                    this@InputDialog.dismiss()
                }

                override fun onFaile(err: String?) {
                    ToastUtils.showLong(err)
                    wg_ed_send.text="重新发送"
                    wg_ed_send.isEnabled=true
                }
            })
    }
}