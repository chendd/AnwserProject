package com.wetin.anwserproject.ui.fragment


import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cn.jzvd.JZDataSource
import cn.jzvd.Jzvd
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.wetin.anwserproject.ProjectApplication
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.SelectItemAdapter
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.UploadAnswer
import com.wetin.anwserproject.bean.table.Collect
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.model.keti.local.CollectLocalModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import com.wetin.anwserproject.ui.home.QuestionActivity
import com.wetin.anwserproject.ui.home.TiKuCommentActivity
import com.wetin.anwserproject.ui.me.LoginActivity
import com.wetin.anwserproject.widget.InputDialog
import com.wetin.anwserproject.widget.VideoLayout
import com.wetin.common.base.BaseFragment
import com.wetin.common.base.IBaseContract
import com.wetin.common.utils.loadimg.ImageLoadUtil
import kotlinx.android.synthetic.main.fragment_work_quest.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import retrofit2.Call
import retrofit2.Response


/**
 * 用户做题的fragment
 *
 */
class WorkQuestFragment : BaseFragment<IBaseContract.BasePresenter>() {
    lateinit var mAdapter:SelectItemAdapter
    var collect:Collect?=null
    var parenView:View?=null
    var isFirstLoad=false
    lateinit var contentBean:ContentBean
    companion object {
        //需要传递题目bean
        fun newInstance(index:Int): WorkQuestFragment {
            val args = Bundle()
            args.putInt("index",index)
            val fragment = WorkQuestFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun initPresenter(): IBaseContract.BasePresenter? =null

    /**setContent 之前处理*/
    override fun initSetContentBefore() {

    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (parenView==null){
            parenView=inflater.inflate(getContentLayout(), container, false)
            isFirstLoad=true
        }else{
            isFirstLoad=false
        }
        return parenView
    }

    /**布局*/
    override fun getContentLayout(): Int =R.layout.fragment_work_quest

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        contentBean=ProjectApplication.chapterContent[this.arguments!!.getInt("index")]
        mAdapter=SelectItemAdapter(null,contentBean)
    }



    /**初始化组件*/
    override fun initWidget() {
        if (!isFirstLoad){
            return
        }
        //全部显示，不支持滑动
        wg_quest_recycle.isNestedScrollingEnabled=false
        //选项
        wg_quest_recycle.layoutManager=LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        wg_quest_recycle.adapter=mAdapter
        wg_quest_title.text = Html.fromHtml("<font color='red'>[${contentBean.questionType}]</font>  ${contentBean.questionContent}")
        wg_check_answer.setOnClickListener {
            if(mAdapter.selectArray.isEmpty()){
                ToastUtils.showShort("请选择您的答案")
                return@setOnClickListener
            }
            mAdapter.showSelectResult()

            wg_check_answer.visibility=View.GONE
            wg_rl_analysis.visibility=View.VISIBLE
        }
        val choiceQuestion = contentBean.choiceQuestion
        mAdapter.setNewData(choiceQuestion.split("*|*"))
        //用户是否做过,做过直接显示分析和答案
        if (contentBean.userHistoryQuest!=null&&contentBean.userHistoryQuest.userAnswer!=null){
            wg_rl_analysis.visibility=View.VISIBLE
            wg_check_answer.visibility=View.GONE
        }else {
            //用户没做过
            wg_check_answer.visibility = if (contentBean.questionType == "单选") View.GONE else View.VISIBLE
            wg_rl_analysis.visibility=View.GONE
        }
        //数据绑定
        var baifen=if (contentBean.answerCount!=0){
            contentBean.answerRightCount/contentBean.answerCount
        }else{ 0 }
        wg_tongji.text=Html.fromHtml("本题23人收藏，全部考生答<font color='red'>${contentBean.answerCount}</font>次，对<font color='red'>${contentBean.answerRightCount}</font>次，正确率<font color='red'>$baifen</font>%，")
        initVideo()
        textView17.compoundPaddingTop
        wg_rigth.text=contentBean.answer
        wg_pointReduction.text= contentBean.pointReduction
        wg_anals.text=contentBean.answerAnalysis
        //底部按钮
        wg_comment.setOnClickListener { TiKuCommentActivity.launcher(activity as QuestionActivity,contentBean.id.toString()) }
        //是否收藏过
        collect = CollectLocalModel.getInstance()
            .isCollect(contentBean.id.toString(), UserLocalModel.newInstanct().getUserData()?.userId ?: "-1")
        wg_collect.isSelected= collect!=null
    }

    /**绑定事件*/
    override fun bindEvent() {
        if (!isFirstLoad){
            return
        }
        initVideo()
        //评论
        wg_edit_comment.setOnClickListener {
            if (UserLocalModel.newInstanct().isLogin()) {
                InputDialog(context).setType(1,contentBean.id.toString(),null).show()
            }else{
                LoginActivity.launcher(activity as QuestionActivity)
            }
        }
        //收藏
        wg_collect.setOnClickListener {
            if (wg_collect.isSelected){//取消收藏
                CollectStatusApi(2)
            }else{ //收藏
                CollectStatusApi(1)
            }
            wg_collect.isSelected=!wg_collect.isSelected
        }
    }

    override fun onPause() {
        super.onPause()
        Jzvd.resetAllVideos()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser){
            if (!EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().register(this)
            }
        }else{
            EventBus.getDefault().unregister(this)
        }
        if (!isVisibleToUser){
            Jzvd.resetAllVideos()

        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun refreshUi(info:String){
        if (info=="analysis"){
            wg_check_answer.visibility=View.GONE
            wg_rl_analysis.visibility=View.VISIBLE
        }
    }

    /**
     * 用户收藏操作
     */
    private fun CollectStatusApi(type:Int){
        if (type==1) {
            collect= Collect()?.apply{
                this.id=contentBean.id
                this.userId = UserLocalModel.newInstanct().getUserData()?.userId?:"-1"
                this.xueKeId = contentBean.parentId
                this.chapterId =contentBean.childId
                this.contentId=contentBean.id.toInt()
            }
            CollectLocalModel.getInstance().CollectContent(collect)
        }else{
            CollectLocalModel.getInstance().cancelCollectContent(collect)
        }
        val uploadAnswer = UploadAnswer(TimeUtils.getNowString(),"" ,contentBean.id.toInt())
        val toJson = Gson().toJson(uploadAnswer)
        UserRemoteModel.getUserApi().uploadCollect(type,"[$toJson]").enqueue(object :ResultCall<String>{
            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {

            }

            override fun onFaile(err: String?) {
            }
        })
    }




    /**视频初始化*/
    private fun initVideo(){
        if (contentBean.video.isNullOrBlank()){
            ll.visibility=View.GONE
            wg_play.visibility=View.GONE
            return
        }
        val arrayMap = LinkedHashMap<String, String>()
        val proxyUrl = ProjectApplication.getProxy().getProxyUrl(contentBean.video)
        arrayMap["高清"] = proxyUrl
        val jzDataSource = JZDataSource(arrayMap, "")
        jzDataSource.looping = false
        jzDataSource.currentUrlIndex = 0
        jzDataSource.headerMap["key"] = "value"
        wg_play.setUp(
            jzDataSource,
            Jzvd.SCREEN_NORMAL
        )
        ImageLoadUtil.instance.loadImg(context!!,wg_play.thumbImageView,contentBean.videoImg)
        wg_play.setListener(object :VideoLayout.ProgressListener{
            override fun progress(progress: Int, position: Long, duration: Long) {
                //视频播放进度回调
               /* LogUtils.a("$progress--$position")
                if(progress==6){
                    Jzvd.resetAllVideos()
                    wg_play.startButton.isEnabled=false
                }*/
            }

        })
       // wg_play.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640")
    }
}
