package com.wetin.anwserproject.ui.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import com.blankj.utilcode.util.SizeUtils
import com.wetin.anwserproject.R
import com.wetin.anwserproject.adapter.TopicAdapter
import com.wetin.anwserproject.ui.BaseAnswerActivity
import com.wetin.anwserproject.ui.home.help.SpaceItemDecoration
import com.wetin.anwserproject.widget.TopLayout
import com.wetin.common.base.IBaseContract
import kotlinx.android.synthetic.main.activity_topic_num.*
import com.kongzue.dialog.v2.SelectDialog
import com.kongzue.dialog.v2.TipDialog
import com.kongzue.dialog.v2.WaitDialog
import com.wetin.anwserproject.ProjectApplication
import com.wetin.anwserproject.bean.RefreshEvens
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.bean.table.UserHistoryQuest
import com.wetin.anwserproject.model.keti.local.ContentLocalModel
import com.wetin.anwserproject.model.keti.local.UserQuestLocalModel
import com.wetin.anwserproject.model.keti.remote.XuekeRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response

/**
 * 章节总题数页面
 */
class TopicNumActivity : BaseAnswerActivity<IBaseContract.BasePresenter>() {
    var topicAdapter=TopicAdapter(null)
    companion object {
        const val INTENT_TITLE="title"
        const val INTENT_CHAPTER="chapter"
        const val INTENT_CHAPTER_ID="chapterID"
        const val INTENT_QUESTSTYLE="questStyle"
        const val INTENT_GROUD="group"
        fun launcher(fromActivity:Activity,title:String,chapter:String,chapterId: String,questStyle:Int=0,group:Int=0){
            val intent = Intent(fromActivity, TopicNumActivity::class.java)
            intent.putExtra(INTENT_TITLE,title)
            intent.putExtra(INTENT_CHAPTER,chapter)
            intent.putExtra(INTENT_CHAPTER_ID,chapterId)
            intent.putExtra(INTENT_QUESTSTYLE,questStyle)
            intent.putExtra(INTENT_GROUD,group)
            fromActivity.startActivity(intent)
        }
    }
    override fun getContentId(): Int = R.layout.activity_topic_num

    override fun initPresenter(): IBaseContract.BasePresenter?=null

    /**初始化数据*/
    override fun initDatas(savedInstanceState: Bundle?) {
        setTopLayoutInfo(intent.getStringExtra(INTENT_TITLE)?:"")
    }

    override fun initWidget() {
        super.initWidget()
        wg_tv_chapter.text = intent.getStringExtra(INTENT_CHAPTER)?:""
        wg_topic_recycle.layoutManager=GridLayoutManager(this,5)
        wg_topic_recycle.addItemDecoration(SpaceItemDecoration(SizeUtils.dp2px(9f)))
        wg_topic_recycle.adapter=topicAdapter
    }
    /**绑定事件*/
    override fun bindEvent() {
        topicAdapter.setOnItemClickListener { adapter, view, position ->
            QuestionActivity.launcher(this,intent.getStringExtra(INTENT_CHAPTER_ID),intent.getStringExtra(INTENT_CHAPTER)?:"",intent.getStringExtra(INTENT_TITLE)?:"",position)
        }
    }


    /**获取章节下所有题目*/
    private fun getContentData() {
        val chapterId = intent.getStringExtra(INTENT_CHAPTER_ID)
        val questStyle = intent.getIntExtra(INTENT_QUESTSTYLE,0)

        when(questStyle){
            //题库来源
            0->{ initTiKuData(chapterId)}
            //错误来源
            1->{

                initFaileQuest(chapterId)
            }
            //收藏来源
            2->{
                initCollectQuest(chapterId)
            }
        }



    }

    override fun onResume() {
        super.onResume()
        getContentData()
    }


    /**设置顶部信息*/
    fun setTopLayoutInfo(title: String){
        val questStyle = intent.getIntExtra(INTENT_QUESTSTYLE,0)
        if(questStyle==1){
            getTopLayout().setTitle(title).setImgShow(isLeft = true, isRight = true).setRightImg(righString = "清空")
                .setClickListener(object : TopLayout.TopClickListener {
                    override fun click(id: Int) {
                        if (id == R.id.wg_top_right) {
                            //重做点击
                            SelectDialog.show(this@TopicNumActivity, "提示", "确定清除此章节下的错题？") { dialog, _ ->
                                UserQuestLocalModel.getInstance().clear(intent.getStringExtra(INTENT_CHAPTER_ID))
                                this@TopicNumActivity.finish()
                            }
                        } else {
                            this@TopicNumActivity.finish()
                        }
                    }

                })
        }else {
            getTopLayout().setTitle(title).setImgShow(isLeft = true, isRight = true).setRightImg(righString = "重做")
                .setClickListener(object : TopLayout.TopClickListener {
                    override fun click(id: Int) {
                        if (id == R.id.wg_top_right) {
                            //重做点击
                            SelectDialog.show(this@TopicNumActivity, "提示", "确定清除所有记录？") { dialog, _ ->
                                UserQuestLocalModel.getInstance().clear(intent.getStringExtra(INTENT_CHAPTER_ID))
                                getContentData()
                            }
                        } else {
                            this@TopicNumActivity.finish()
                        }
                    }

                })
        }
    }


    /**
     *  题库数据初始化
     *
     * */
    fun initTiKuData(chapterId:String){
        val contentLocal = ContentLocalModel.getInstance().query(chapterId)
        if (contentLocal == null || contentLocal.size == 0) {//无数据走网络
            WaitDialog.show(this, "数据加载中..")
            XuekeRemoteModel.getKeTiApi().getContentByChapterId(1, chapterId.toInt())
                .enqueue(object : ResultCall<ArrayList<ContentBean>> {
                    override fun onSuccess(
                        call: Call<ResultBean<ArrayList<ContentBean>>>,
                        response: Response<ResultBean<ArrayList<ContentBean>>>
                    ) {

                        for (bean in response?.body()?.result!!) {
                            ContentLocalModel.getInstance().insert(bean)
                            try {
                                bean.userHistoryQuest= UserQuestLocalModel.getInstance().getUserDoQuest(bean?.id.toString())[0]
                            }catch (e:Exception){
                            }
                        }
                        ProjectApplication.chapterContent=response?.body()?.result!!
                        topicAdapter.setNewData(response?.body()?.result!!)
                        WaitDialog.dismiss()
                    }

                    override fun onFaile(err: String?) {
                        WaitDialog.dismiss()
                        TipDialog.show(this@TopicNumActivity, err, TipDialog.SHOW_TIME_SHORT, TipDialog.TYPE_ERROR)
                    }

                })
        } else { //取本地
            for (bean in contentLocal) {
                bean.userHistoryQuest=null
                try {
                    bean.userHistoryQuest= UserQuestLocalModel.getInstance().getUserDoQuest(bean?.id.toString())[0]
                }catch (e:Exception){
                }
            }
            ProjectApplication.chapterContent=contentLocal
            topicAdapter.setNewData(contentLocal)

            XuekeRemoteModel.getKeTiApi().getContentByChapterId(1, chapterId.toInt())
                .enqueue(object : ResultCall<ArrayList<ContentBean>> {
                    override fun onSuccess(
                        call: Call<ResultBean<ArrayList<ContentBean>>>,
                        response: Response<ResultBean<ArrayList<ContentBean>>>
                    ) {

                        for (bean in response?.body()?.result!!) {
                            ContentLocalModel.getInstance().insert(bean)
                        }
                    }

                    override fun onFaile(err: String?) {
                    }

                })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().post(RefreshEvens(intent.getIntExtra(INTENT_GROUD,0)))
    }
    /**
     * 错题数据初始化
     */
    fun initFaileQuest(chapterId:String){
        var datas=ArrayList<ContentBean>()
        val contentLocal = ContentLocalModel.getInstance().query(chapterId)
        for (bean in contentLocal) {
            try {
                bean.userHistoryQuest= UserQuestLocalModel.getInstance().getUserDoQuestByFaile(bean?.id.toString())[0]
                if (bean.userHistoryQuest!=null){
                    datas.add(bean)
                }
            }catch (e:Exception){
            }
        }
        ProjectApplication.chapterContent=datas
        topicAdapter.setNewData(datas)
    }


    /**
     * 收藏数据初始化
     */
    fun initCollectQuest(chapterId:String){
        var datas=ArrayList<ContentBean>()
        val contentLocal = ContentLocalModel.getInstance().query(chapterId)
        for (bean in contentLocal) {
            try {
                val collect = UserQuestLocalModel.getInstance().getUserDoQuestByCollect(bean?.id.toString())[0]
                bean.userHistoryQuest= UserHistoryQuest(collect.id,collect.userId,collect.xueKeId.toLong(),collect.chapterId.toLong(),collect.contentId.toLong(),null,0,false)
                if (bean.userHistoryQuest!=null){
                    datas.add(bean)
                }
            }catch (e:Exception){
            }
        }
        ProjectApplication.chapterContent=datas
        topicAdapter.setNewData(datas)
    }
}
