package com.wetin.anwserproject.ui.me

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.OrderBean
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.common.base.IBaseContract
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.activity_edit_comment.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class EditCommentActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var compressPath:String=""
    override fun getContentId(): Int =R.layout.activity_edit_comment

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("晒单与评价",R.color.white)
            .setImgShow(true,false)
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))
        wg_order_id.text = "订单编号:${(intent.getSerializableExtra("data") as OrderBean).orderId}"
        wg_time.text = "${(intent.getSerializableExtra("data") as OrderBean).createTime}"
    }

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_img.setOnClickListener {
            selectImg()
        }

        wg_submit.setOnClickListener { uploadImg(compressPath) }
    }


    /**
     * 上传文件Api
     * @url:文件路径
     */
    private fun uploadImg(url:String){
        WaitDialog.show(this,"处理中..")
        val content = wg_ed_content.text.toString()
        val requestBody = RequestBody.create(MediaType.parse("image/png"), File(url))
        val part = MultipartBody.Part.createFormData("file", "${System.currentTimeMillis()}.png", requestBody)
        UserRemoteModel.getUserApi().uploadFile(part).enqueue(object : ResultCall<String> {
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                submintContent((intent.getSerializableExtra("data") as OrderBean).orderId,response?.body()?.result!!,content)
            }

            override fun onFaile(err: String?) {
                WaitDialog.dismiss()
                TipDialog.show(this@EditCommentActivity,err)
            }

        })
    }


    private fun submintContent(order:String,img:String,content:String){
        UserRemoteModel.getUserApi().commentSave(order,content,img).enqueue(object : ResultCall<String> {
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                WaitDialog.dismiss()
                setResult(1)
                this@EditCommentActivity.finish()
            }

            override fun onFaile(err: String?) {
                WaitDialog.dismiss()
                TipDialog.show(this@EditCommentActivity,err)
            }

        })
    }


    /**选择图片*/
    private fun selectImg(){
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
            .selectionMode(PictureConfig.SINGLE)
            .isCamera(true)
            .compress(true)
            .cropCompressQuality(10)
            .sizeMultiplier(0.5f)
            .minimumCompressSize(100)
            .withAspectRatio(1,1)
            .forResult(PictureConfig.CHOOSE_REQUEST)
    }

    /**
     * 回调处理
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //图片
        if (resultCode == RESULT_OK&&requestCode==PictureConfig.CHOOSE_REQUEST) {
            val selectList = PictureSelector.obtainMultipleResult(data)
            compressPath = selectList[0].compressPath
            ImageLoadUtil.instance.loadImg(this, wg_img, selectList[0].compressPath)
        }

    }

    companion object {
        fun launcher(fromActivity: Activity,data:OrderBean){
            val intent = Intent(fromActivity, EditCommentActivity::class.java)
            intent.putExtra("data",data)
            fromActivity.startActivityForResult(intent,1)
        }
    }

}
