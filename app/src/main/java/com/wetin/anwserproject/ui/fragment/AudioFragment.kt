package com.wetin.anwserproject.ui.fragment


import android.app.Activity
import android.content.DialogInterface
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.tsy.sdk.social.PlatformType
import com.tsy.sdk.social.SocialApi
import com.tsy.sdk.social.listener.ShareListener
import com.tsy.sdk.social.share_media.ShareWebMedia
import com.wetin.anwserproject.MainActivity
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.ExtendUserAdapter
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.bean.table.XueKeBean
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.model.video.local.AudioLocalModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.home.MusicPlayActivity
import com.wetin.anwserproject.ui.me.LoginActivity
import com.wetin.anwserproject.utils.Config
import com.wetin.common.base.BaseFragment
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.fragment_video.*
import retrofit2.Call
import retrofit2.Response


/**
 * 《音视频》中的音频fragment
 *
 */
class AudioFragment : BaseFragment<IBaseContract.BasePresenter>() {
    lateinit var shareWebMedia:ShareWebMedia
    lateinit var  mSocialApi: SocialApi
    override fun initPresenter(): IBaseContract.BasePresenter? =null
    lateinit var extendAdapter: ExtendUserAdapter
    companion object {
        fun newInstance():AudioFragment{
            val args = Bundle()
            val fragment = AudioFragment()
            fragment.arguments = args
            return fragment
        }
    }
    /**setContent 之前处理*/
    override fun initSetContentBefore() {

    }

    /**布局*/
    override fun getContentLayout(): Int =R.layout.fragment_audio

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        mSocialApi= SocialApi.get(context)
        shareWebMedia= ShareWebMedia().apply {
            this.description="这里有一份优惠"
            this.title="就等你来呢"
            this.webPageUrl="www.baidu.com"
            this.thumb= BitmapFactory.decodeResource(resources, R.mipmap.ic_logo)
        }
    }

    /**初始化组件*/
    override fun initWidget() {
        getData()
    }


    private fun  getData(){
        //判断本地是否有数据
        val videoList = AudioLocalModel.getInstance().audioList
        if (videoList.isNullOrEmpty()){
            WaitDialog.show(context,"加载中...")
            Log.d("chendd","AudioFragment网络请求")
            //网络请求
            XuekeRemoteModel.getKeTiApi().queryVideoOrAudio(3).enqueue(
                object : ResultCall<ArrayList<XueKeBean>> {
                    override fun onSuccess(
                        call: Call<ResultBean<ArrayList<XueKeBean>>>,
                        response: Response<ResultBean<ArrayList<XueKeBean>>>
                    ) {
                        extendAdapter=ExtendUserAdapter(context!!,response?.body()?.result)
                        wg_listView.setAdapter(extendAdapter)
                        AudioLocalModel.getInstance().save(Gson().toJson(response?.body()?.result))
                        WaitDialog.dismiss()
                    }

                    override fun onFaile(err: String?) {
                        WaitDialog.dismiss()
                        TipDialog.show(context,err, TipDialog.TYPE_ERROR)
                    }
                }
            )
        }else{
            //取本地
            Log.d("chendd","AudioFragment取本地"+videoList.size)
            extendAdapter=ExtendUserAdapter(context!!,videoList)
            wg_listView.setAdapter(extendAdapter)
            //同步数据
            XuekeRemoteModel.getKeTiApi().queryVideoOrAudio(3).enqueue(
                object : ResultCall<ArrayList<XueKeBean>> {
                    override fun onSuccess(
                        call: Call<ResultBean<ArrayList<XueKeBean>>>,
                        response: Response<ResultBean<ArrayList<XueKeBean>>>
                    ) {
                        AudioLocalModel.getInstance().save(Gson().toJson(response?.body()?.result))
                    }

                    override fun onFaile(err: String?) {
                    }
                }
            )
        }
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_listView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val child = extendAdapter.getChild(groupPosition, childPosition) as ChapterBean
            val keTiBean = extendAdapter.getGroup(groupPosition) as XueKeBean
            if (keTiBean.type == 1) {  //文章
                //判断是否需要解锁
                if (child.unlockType!=0) {
                    //解锁操作
                    unLockDialog(child)
                }else{ //跳转章节目录
                    MusicPlayActivity.launcher(this@AudioFragment.activity as Activity, child)
                }
            } else { //播放
                MusicPlayActivity.launcher(this@AudioFragment.activity as Activity, child)
            }

            false
        }
    }


    /**
     * 解锁操作确认对话框
     */
    private fun unLockDialog(child:ChapterBean){
        if (UserLocalModel.newInstanct().isLogin()){
            SelectDialog.build(context,"提示","是否进行章节解锁操作?","确定", DialogInterface.OnClickListener { dialog, which ->
                chapterUnLockAction(child)
            },"取消",null).showDialog()
        }else{
            LoginActivity.launcher(activity as MainActivity)
        }
    }

    /**
     * 文章解锁操作
     */
    private fun chapterUnLockAction(child:ChapterBean){

        when(child.unlockType){
            Config.LockType.Free.ordinal->{

            }
            Config.LockType.WeChatShare.ordinal->{
                mSocialApi.doShare(activity, PlatformType.WEIXIN,shareWebMedia,object : ShareListener {
                    override fun onComplete(platform_type: PlatformType?) {
                        shareLockApi(child)
                    }

                    override fun onCancel(platform_type: PlatformType?) {
                        TipDialog.show(context,"分享取消")
                    }

                    override fun onError(platform_type: PlatformType?, err_msg: String?) {
                        TipDialog.show(context,err_msg)
                    }

                })
            }
            Config.LockType.QQShare.ordinal->{
                mSocialApi.doShare(activity,PlatformType.QQ,shareWebMedia,object :ShareListener{
                    override fun onComplete(platform_type: PlatformType?) {
                        shareLockApi(child)
                    }

                    override fun onCancel(platform_type: PlatformType?) {
                        TipDialog.show(context,"分享取消")
                    }

                    override fun onError(platform_type: PlatformType?, err_msg: String?) {
                        TipDialog.show(context,err_msg)
                    }

                })
            }
            Config.LockType.Charge.ordinal->{//充值

            }

        }
    }

    private fun shareLockApi(chapterBean:ChapterBean){
        XuekeRemoteModel.getKeTiApi().shareOpen(2,chapterBean.id.toString(),shareType = chapterBean.unlockType)
            .enqueue(object :ResultCall<String>{
                override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {
                    chapterBean.unlockType=0
                    extendAdapter.notifyDataSetChanged()
                }

                override fun onFaile(err: String?) {
                    TipDialog.show(context,"章节解锁失败")
                }
            })
    }



}
