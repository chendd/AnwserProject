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
import kotlinx.android.synthetic.main.layout_service.*
import retrofit2.Call
import retrofit2.Response

class ServiceDialog(context: Context?) : Dialog(context, R.style.dialog) {
    private var type:Int=0
    private var questionId:String?=null
    private var replayCommentId:String?=null
    init {
         setContentView(R.layout.layout_service)

     }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wg_close.setOnClickListener { this.dismiss() }
        wg_qq.setOnClickListener {
            this.dismiss()
        }
        wg_weixin.setOnClickListener {
            this.dismiss()
        }
    }



}