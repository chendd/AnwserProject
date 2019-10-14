package com.wetin.anwserproject.ui.me

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.widget.TextView
import cn.addapp.pickers.picker.SinglePicker
import com.blankj.utilcode.util.SizeUtils
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener
import com.kongzue.dialog.v2.*
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.utils.CountDownUtil
import com.wetin.anwserproject.widget.TopLayout
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_user_info.*
import retrofit2.Call
import retrofit2.Response
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.wetin.anwserproject.bean.UserBean
import com.wetin.common.utils.loadimg.ImageLoadUtil
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Part
import java.io.File


class UserInfoActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var userBean:UserBean?=null
    var userImgUrl:String?=null
    companion object {
        fun launcher(fromActivity:Context){
            fromActivity.startActivity(Intent(fromActivity,UserInfoActivity::class.java))
        }
    }
    override fun getContentId(): Int =R.layout.activity_user_info

    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        userBean= UserLocalModel.newInstanct().getUserData()
    }

    override fun initWidget() {
        super.initWidget()
        getTopLayout().setTitle("我的资料",R.color.white)
            .setImgShow(true,false)
            .setClickListener(object :TopLayout.TopClickListener{
                override fun click(id: Int) {
                    backDialog()
                }
            })
            .setBackgroundColor(ContextCompat.getColor(this,R.color.bg_red))


        /**ui赋值*/
        wg_name.text=userBean?.nickName
        wg_sex.text=if(userBean?.sex=="1")"男" else "女"
        wg_year.text=userBean?.testYear
        wg_tv_zhuan.text=userBean?.specialtySchool
        wg_tv_bao.text=userBean?.testSchool
        ImageLoadUtil.instance.loadCircleImg(this, wg_img,userBean?.headImg)
    }
    /**绑定事件*/
    override fun bindEvent() {
        wg_item_img.setOnClickListener { selectImg() }
        //名称
        wg_item_name.setOnClickListener { showInputDialog(wg_name,"设置名称","设置一个好听的名字吧") }
        //性别
        wg_item_sex.setOnClickListener {
            showSexSelectDialog(wg_sex,arrayListOf("男","女"))
        }
        //年份
        wg_item_year.setOnClickListener {
            val cuyear = CountDownUtil.getSysYear()
            showSexSelectDialog(wg_year,arrayListOf("${cuyear}","${cuyear+1}","${cuyear+2}"))
        }
        wg_item_zhuang.setOnClickListener {
            showPick(wg_tv_zhuan,this.resources.getStringArray(R.array.array_zhuangke))

        }
        wg_item_bao.setOnClickListener {
            showPick(wg_tv_bao,this.resources.getStringArray(R.array.array_benke))
        }
    }


    /**
     * 输入对话框
     */
    private fun showInputDialog(tView:TextView,title:String,msg:String){
        InputDialog.show(this, title, msg, "确定",
            InputDialogOkButtonClickListener { dialog, inputText ->
                tView.text = inputText
                dialog?.dismiss()
            }, "取消", DialogInterface.OnClickListener { dialog, which -> dialog?.dismiss() })
    }

    /**
     * 性别选择对话框
     */
    private fun showSexSelectDialog(tView:TextView,list:ArrayList<String>){

        BottomMenu.show(this, list,
            { text, index -> tView.text = text }, false)
    }


    /**
     * 选择器
     */
    private fun showPick(textView: TextView,array: Array<String>) {
        val singlePicker = SinglePicker(this, array)
        singlePicker.setTextSize(18)
        singlePicker.setItemWidth(SizeUtils.dp2px(200f))
        singlePicker.setCanLoop(false)
        singlePicker.setSelectedTextColor(resources.getColor(com.wetin.anwserproject.R.color.bg_red))
        singlePicker.setOnItemPickListener { _, s ->
            textView.text = s
        }
        singlePicker.show()
    }

    /**
     * 上传修改信息
     */
    private fun updateApi(){
        WaitDialog.show(this,"修改中..")
        val sex=if(wg_sex.text.toString()=="男") "1" else "2"
        UserRemoteModel.getUserApi().update(userBean?.userId?:"",userImgUrl,wg_name.text.toString(),
            sex,
            wg_year.text.toString(),
            wg_tv_zhuan.text.toString(),
            wg_tv_bao.text.toString()).enqueue(object :ResultCall<UserBean>{
            override fun onSuccess(call: Call<ResultBean<UserBean>>, response: Response<ResultBean<UserBean>>) {
                WaitDialog.dismiss()
                val result = response?.body()?.result
                result?.token=userBean?.token?:""
                UserLocalModel.newInstanct().saveUser(response?.body()?.result!!)
                this@UserInfoActivity.finish()
            }

            override fun onFaile(err: String?) {
                WaitDialog.dismiss()
                showInfo(err?:"未知错误")
            }

        })
    }


    /**
     * 返回对话框
     */
    private fun backDialog(){
        SelectDialog.show(
            this, "保存提示", "是否保存修改数据?","保存",
            DialogInterface.OnClickListener { dialog, which -> updateApi() },"取消", DialogInterface.OnClickListener { dialog, which ->
                dialog?.dismiss()
                this@UserInfoActivity.finish()
            }
        )
    }

    /**选择图片*/
    private fun selectImg(){
        PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
            .selectionMode(PictureConfig.SINGLE)
            .isCamera(true)
            .enableCrop(true)
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
            WaitDialog.show(this,"处理中..")
            val selectList = PictureSelector.obtainMultipleResult(data)
            ImageLoadUtil.instance.loadCircleImg(this, wg_img, selectList[0].compressPath)
            uploadImg(selectList[0].compressPath)
        }

    }

    /**
     * 上传文件Api
     * @url:文件路径
     */
    private fun uploadImg(url:String){
        val requestBody = RequestBody.create(MediaType.parse("image/png"), File(url))
        val part = MultipartBody.Part.createFormData("file", "${System.currentTimeMillis()}.png", requestBody)
        UserRemoteModel.getUserApi().uploadFile(part).enqueue(object :ResultCall<String>{
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                userImgUrl=response?.body()?.result
                WaitDialog.dismiss()
            }

            override fun onFaile(err: String?) {
                WaitDialog.dismiss()
                TipDialog.show(this@UserInfoActivity,err)
            }

        })
    }
    override fun onBackPressed() {
        backDialog()
    }
}
