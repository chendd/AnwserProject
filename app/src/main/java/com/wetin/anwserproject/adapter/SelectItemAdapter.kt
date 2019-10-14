package com.wetin.anwserproject.adapter

import android.support.constraint.ConstraintLayout
import android.view.View
import android.widget.TextView
import com.blankj.utilcode.util.TimeUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.google.gson.Gson
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.ResultBean
import com.wetin.anwserproject.bean.UploadAnswer
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.bean.table.UserHistoryQuest
import com.wetin.anwserproject.model.keti.local.UserQuestLocalModel
import com.wetin.anwserproject.model.user.local.UserLocalModel
import com.wetin.anwserproject.model.user.remote.UserRemoteModel
import com.wetin.anwserproject.net.help.ResultCall
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Response
import java.lang.StringBuilder

class SelectItemAdapter(data: MutableList<String>?, var content: ContentBean) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_select_quest, data) {
    //测试
    val questStyle = content.questionType
    //选择结果集
    val selectArray = arrayListOf<String>()
    private var isShowResult = false

    //序列集合A，B，C
    val sortArray = arrayListOf("A", "B", "C", "D", "E", "F", "G", "H")

    override fun convert(helper: BaseViewHolder?, item: String?) {
        val indexOf = data.indexOf(item)
        //判断做题还是仅仅显示
        helper?.apply {
            this.setText(R.id.wg_tv_answer, "$item")
            if (isShowResult || (content.userHistoryQuest != null && content.userHistoryQuest.userAnswer != null)) {//显示答案
                if ((content.userHistoryQuest != null && content.userHistoryQuest.userAnswer != null)) {
                    val toCharArray = content.userHistoryQuest.userAnswer.toCharArray()
                    for (bean in toCharArray) {
                        selectArray.add(bean.toString())
                    }
                }
                this.getView<ConstraintLayout>(R.id.wg_item).isEnabled = false
                if (content.answer.contains(sortArray[indexOf])) {
                    /**答案中的一个*/
                    if (isSelected(sortArray[indexOf])) { //选对了
                        this.setBackgroundRes(R.id.wg_iv_status, R.drawable.ic_smll_gou)
                        this.setTextColor(R.id.wg_tv_answer, mContext.resources.getColor(R.color.bg_green))
                    } else { //对的答案，没选->红色打钩
                        this.setBackgroundRes(R.id.wg_iv_status, R.drawable.ic_small_gou)
                        this.setTextColor(R.id.wg_tv_answer, mContext.resources.getColor(R.color.bg_red))
                    }
                } else if (isSelected(sortArray[indexOf])) {//选错了
                    this.setBackgroundRes(R.id.wg_iv_status, R.drawable.ic_smll_faile)
                    this.setTextColor(R.id.wg_tv_answer, mContext.resources.getColor(R.color.bg_red))
                } else { //不是答案中的->正常显示
                    this.setBackgroundRes(R.id.wg_iv_status, R.drawable.ic_smll_circle)
                    this.setTextColor(R.id.wg_tv_answer, mContext.resources.getColor(R.color.text_gray))
                }
            } else {
                this.getView<ConstraintLayout>(R.id.wg_item).setOnClickListener { v: View? ->
                    selectEvent(v!!, sortArray[indexOf], helper.getView<TextView>(R.id.wg_iv_status))
                }
            }
        }

    }


    fun selectEvent(itemView: View, selectIndex: String, imageView: TextView) {
        itemView.isSelected = !itemView.isSelected
        if (itemView.isSelected) {
            selectArray.add(selectIndex)
            imageView.setBackgroundResource(R.drawable.ic_small_cir_shi)
        } else {
            selectArray.remove(selectIndex)
            imageView.setBackgroundResource(R.drawable.ic_smll_circle)
        }
        if (questStyle == "单选") {//单选
            showSelectResult()
        }
    }

    fun showSelectResult() {
        isShowResult = true
        notifyDataSetChanged()
        EventBus.getDefault().post("analysis")
        //保存记录
        val stringBuilder = StringBuilder()
        for (answ in selectArray) {
            stringBuilder.append(answ)
        }
        val userHistoryQuest = UserHistoryQuest(
            content.id,
            UserLocalModel.newInstanct().getUserData()?.userId ?: "-1",
            content.parentId.toLong(),
            content.childId.toLong(),
            content.id.toLong(),
            stringBuilder.toString(),
            isAnswerTrue(stringBuilder.toString(), content.answer),
            false
        )
        //保存本地
        saveUserQuest(userHistoryQuest)
        //上传
        if (UserLocalModel.newInstanct().isLogin()) {
            uploadAnswer(userHistoryQuest)
        }
    }

    /**勾选*/
    private fun isSelected(cuIndex: String): Boolean {
        for (index in selectArray) {
            if (cuIndex == index) {
                return true
            }
        }
        return false
    }


    private fun isAnswerTrue(answer: String, trueAnswer: String): Int {
        //1 ->对 2->错
        if (answer.length != trueAnswer.length) {
            return 2
        } else {//长度一致
            for (char in answer.toCharArray()) {
                if (!trueAnswer.contains(char)) {
                    return 2
                }
            }
            return 1
        }
    }

    /**保存用户做题记录->上传*/
    private fun saveUserQuest(userHistoryQuest: UserHistoryQuest) {
        UserQuestLocalModel.getInstance().insert(userHistoryQuest)
    }


    private fun uploadAnswer(userHistoryQuest: UserHistoryQuest) {
        val uploadAnswer =
            UploadAnswer(TimeUtils.getNowString(), userHistoryQuest.userAnswer, userHistoryQuest.contentId.toInt())
        val toJson = Gson().toJson(uploadAnswer)
        UserRemoteModel.getUserApi().uploadAnswer("[$toJson]").enqueue(object : ResultCall<String> {
            override fun onFaile(err: String?) {

            }

            override fun onSuccess(call: Call<ResultBean<String>>, response: Response<ResultBean<String>>) {

            }
        })
    }
}