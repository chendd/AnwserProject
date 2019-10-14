package com.wetin.anwserproject.ui.fragment


import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.webkit.MimeTypeMap
import android.widget.ExpandableListView
import android.widget.VideoView
import com.blankj.utilcode.util.ToastUtils
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.tsy.sdk.social.PlatformType
import com.tsy.sdk.social.SocialApi
import com.tsy.sdk.social.listener.ShareListener
import com.tsy.sdk.social.share_media.ShareWebMedia
import com.wetin.anwserproject.MainActivity
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.ExtendProgressAdapter
import com.wetin.anwserproject.bean.RefreshEvens
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.bean.table.Collect
import com.wetin.anwserproject.bean.table.UserHistoryQuest
import com.wetin.anwserproject.bean.table.XueKeBean
import com.wetin.anwserproject.model.keti.local.CollectLocalModel
import com.wetin.anwserproject.model.keti.local.UserQuestLocalModel
import com.wetin.anwserproject.model.keti.local.XueKeLocalModel
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.net.util.NetWorkConnection
import com.wetin.anwserproject.pay.PayListenerUtils
import com.wetin.anwserproject.pay.PayResultListener
import com.wetin.anwserproject.pay.PayUtils
import com.wetin.anwserproject.ui.fragment.contract.VollFragContranct
import com.wetin.anwserproject.ui.home.*
import com.wetin.anwserproject.ui.me.LoginActivity
import com.wetin.anwserproject.ui.shop.ShopDescActivity
import com.wetin.anwserproject.utils.Config
import com.wetin.anwserproject.widget.ActionPopu
import com.wetin.common.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_home.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Response


/***
 * 首页《题库》fragment
 */
class HomeFragment : BaseFragment<VollFragContranct.Presenter>(),PayResultListener{
    //分享内容
    lateinit var shareWebMedia:ShareWebMedia
    override fun initPresenter(): VollFragContranct.Presenter? = null
    lateinit var extendAdapter: ExtendProgressAdapter
    lateinit var  mSocialApi: SocialApi
    /**setContent 之前处理*/
    override fun initSetContentBefore() {

    }

    /**布局*/
    override fun getContentLayout(): Int = R.layout.fragment_home

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        mSocialApi= SocialApi.get(context)
        shareWebMedia=ShareWebMedia().apply {
            this.description="这里有一份优惠"
            this.title="就等你来呢"
            this.webPageUrl="www.baidu.com"
            this.thumb=BitmapFactory.decodeResource(resources, R.mipmap.ic_logo)
        }
        PayListenerUtils.getInstance(context).addListener(this)
        EventBus.getDefault().register(this)
    }

    /**初始化组件*/
    override fun initWidget() {
        getTiKuData()
    }

    /**绑定事件*/
    override fun bindEvent() {
        wg_rl_search.setOnClickListener { SearchActivity.launcher(activity as Activity, wg_rl_search) }
        /**分享按钮*/
        wg_share.setOnClickListener {
            mSocialApi.doShare(activity,PlatformType.WEIXIN,shareWebMedia,null)

        }
        // 父点击
        wg_ex_listview.setOnGroupClickListener(object : ExpandableListView.OnGroupClickListener {
            override fun onGroupClick(parent: ExpandableListView?, v: View?, groupPosition: Int, id: Long): Boolean {
                wg_app_bar.setExpanded(false)
                return false
            }

        })
        //子view点击
        wg_ex_listview.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
            val child = extendAdapter.getChild(groupPosition, childPosition) as ChapterBean
            val keTiBean = extendAdapter.getGroup(groupPosition) as XueKeBean
            if (keTiBean.type == 1) {  //文章
                //判断是否需要解锁
                if (child.unlockType!=0) {
                    //解锁操作
                    unLockDialog(child)
                }else{ //跳转章节目录
                    TopicNumActivity.launcher(
                        this@HomeFragment.activity as Activity,
                        keTiBean.name,
                        child.name ?: "",
                        child.id.toString(),
                        groupPosition
                    )
                }
            } else {  //视频
                //PlayVideoActivity.launcher( this@HomeFragment.activity as Activity,child)
                //调用系统自带的播放器
//                var uri = Uri.parse(child.videoUrl);
//                var intent =  Intent(Intent.ACTION_VIEW);
//                intent.setDataAndType(uri, "video/mp4");
//                startActivity(intent);

                var extension = MimeTypeMap.getFileExtensionFromUrl(child.videoUrl)
                var mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
                var mediaIntent =   Intent(Intent.ACTION_VIEW)
                mediaIntent.setDataAndType(Uri.parse(child.videoUrl), mimeType)
                startActivity(mediaIntent)

            }
            false
        }

        /**收藏按钮*/
        wg_collect.setOnClickListener {

           CollectActivity.launcher(activity as MainActivity)
           /* if (UserLocalModel.newInstanct().isLogin()) {
                CollectActivity.launcher(activity as MainActivity)
            }else{
                LoginActivity.launcher(activity as MainActivity)
            }*/
        }
        /**错题*/
        wg_faile_keti.setOnClickListener {
            AnswerFaileActivity.launcher(activity as MainActivity)
           /* if (UserLocalModel.newInstanct().isLogin()) {
                AnswerFaileActivity.launcher(activity as MainActivity)
            }else{
                LoginActivity.launcher(activity as MainActivity)
            }*/
        }
        /**评论*/
        wg_comment.setOnClickListener {
            if (UserLocalModel.newInstanct().isLogin()) {
                CommentActivity.launcher(activity as MainActivity)
            }else{
                LoginActivity.launcher(activity as MainActivity)
            }
        }
    }

    /**
     * 获取题库数据
     */
    private fun getTiKuData() {
        val keTiData = XueKeLocalModel.getInstance().allXueKe
        var boo=NetWorkConnection.checkNetworkConnection(this.context)
        if(boo){
//        if (keTiData == null||keTiData.size==0) {//网络请求
            WaitDialog.show(context, "数据加载中..")
            XuekeRemoteModel.getKeTiApi().getQuestion().enqueue(object : ResultCall<List<XueKeBean>> {
                override fun onFaile(err: String?) {
                    WaitDialog.dismiss()
                    TipDialog.show(this@HomeFragment.context, err, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                }

                override fun onSuccess(
                    call: Call<ResultBean<List<XueKeBean>>>,
                    response: Response<ResultBean<List<XueKeBean>>>
                ) {
                    WaitDialog.dismiss()
                    for (bean in response?.body()?.result!!) {
                        XueKeLocalModel.getInstance().insertXueKe(bean)
                    }
                    extendAdapter = ExtendProgressAdapter(context!!, response?.body()?.result as ArrayList<XueKeBean>?)
                    wg_ex_listview.setAdapter(extendAdapter)
                }

            })
        } else {
            extendAdapter = ExtendProgressAdapter(context!!, keTiData as ArrayList<XueKeBean>)
            wg_ex_listview.setAdapter(extendAdapter)
            XuekeRemoteModel.getKeTiApi().getQuestion().enqueue(object : ResultCall<List<XueKeBean>> {
                override fun onFaile(err: String?) {
                }

                override fun onSuccess(
                    call: Call<ResultBean<List<XueKeBean>>>,
                    response: Response<ResultBean<List<XueKeBean>>>
                ) {
                    for (bean in response?.body()?.result!!) {
                        XueKeLocalModel.getInstance().insertXueKe(bean)
                    }
                }

            })

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
        val shareWebMedia = ShareWebMedia().apply {
            this.description="这里有一份优惠"
            this.title="就等你来呢"
            this.webPageUrl="www.baidu.com"
            this.thumb=BitmapFactory.decodeResource(resources, R.mipmap.ic_logo)
        }
        when(child.unlockType){
            Config.LockType.Free.ordinal->{

            }
            Config.LockType.WeChatShare.ordinal->{
                try {
                    mSocialApi.doShare(activity, PlatformType.WEIXIN, shareWebMedia, object : ShareListener {
                        override fun onComplete(platform_type: PlatformType?) {
                            shareLockApi(child)
                        }

                        override fun onCancel(platform_type: PlatformType?) {
                            TipDialog.show(context, "分享取消")
                        }

                        override fun onError(platform_type: PlatformType?, err_msg: String?) {
                            TipDialog.show(context, err_msg)
                        }

                    })
                }catch (e:NoClassDefFoundError){
                    ToastUtils.showShort("请安装微信")
                }
            }
            Config.LockType.QQShare.ordinal->{
                try {
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
                }catch (e:NoClassDefFoundError){
                    ToastUtils.showShort("请安装QQ")
                }
            }
            Config.LockType.Charge.ordinal->{//充值
                ShopDescActivity.launcher(activity as MainActivity,1,child.parentId.toString())
            }

        }
    }

    private fun shareLockApi(chapterBean:ChapterBean){
        XuekeRemoteModel.getKeTiApi().shareOpen(1,chapterBean.id.toString(),shareType = chapterBean.unlockType)
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


    override fun onPaySuccess() {
        getTiKuData()
        WaitDialog.dismiss()
    }

    override fun onPayError() {
        ToastUtils.showShort("支付失败")
        WaitDialog.dismiss()
    }

    override fun onPayCancel() {
        ToastUtils.showShort("支付取消")
        WaitDialog.dismiss()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshUI(index: RefreshEvens){
        extendAdapter.notifyDataSetChanged()
        extendAdapter.onGroupExpanded(index.index)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        PayListenerUtils.getInstance(context).removeListener(this)
    }


    companion object {
        fun newInstance(): HomeFragment {
            val args = Bundle()
            val fragment = HomeFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
