package com.wetin.anwserproject.bean

import java.io.Serializable

/**
 * 统一回调实体类
 */
data class ResultBean<T>(
    val resp_code: String,
    val resp_message: String,
    val result: T
)
/**
* 用户数据实体
*/
data class UserBean(
    val headImg: String,
    val id: String,
    val nickName: String,
    val roleId: String,
    val sex: String,
    val specialtySchool: String,
    val testSchool: String,
    val testYear: String,
    var token: String,
    val userId: String
)

data class UploadAnswer(
    val operationTime: String,
    val choiceAnswer:String,
    val questionId: Int
)


data class RefreshEvens(var index:Int)
//分页
data class PagerBean<T>(
    val content: T,
    val first: Boolean,
    val last: Boolean,
    val number: Int,
    val numberOfElements: Int,
    val size: Int,
    val sort: Any,
    val totalElements: Int,
    val totalPages: Int
)

data class Comment(
    val commentContent: String,
    val countDigg: Int,
    val createTime: String,
    val headImg: String,
    val id: Int,
    val nickName: String,
    val questionId: Int,
    val replayCommentContent: String,
    val replayCommentId: String,
    val replayNickName: String,
    val replaySpecialtySchool: String,
    val replayCreateTime: String,
    val specialtySchool: String,
    val type: Int,
    val updateTime: String,
    val userId: String
)


data class ShopComment(
    val commentContent: String,
    val createTime: String,
    val headImg: String,
    val id: Int,
    val imgUrl: String,
    val nickName: String,
    val replayCommentContent: String,
    val shopId: Int,
    val updateTime: String,
    val userId: String
)

data class MsgBean(
    val createTime: String,
    val id: Int,
    val isRead: Int,
    val messageContent: String,
    val messageTitle: String,
    val questionId: Any,
    val updateTime: String,
    val userId: Any
)
data class ShopDesc(
    val commentCount: Int,
    val createTime: String,
    val id: Int,
    val oriPrice: Int,
    val saleCount: Int,
    val parentId: String,
    val priceMax: Double,
    val priceMin: Double,
    val remarks: String,
    val shopBannerImg: ArrayList<ShopBannerImg>?,
    val shopVipTime: ArrayList<ShopVipTime>,
    val shopInfoImg: ArrayList<ShopInfoImg>,
    val title: String,
    val type: Int,
    val updateTime: String
)

data class WeChatPayInfo(
    val noncestr: String,
    val `package`: String,
    val prepayid: String,
    val sign: String,
    val timestamp: String
)

data class OrderBean(
    val buyTime: Int,
    val createTime: String,
    val desc: String,
    val id: Int,
    val isComment: Int,
    val orderId: String,
    val orderStatus: Int,
    val outOrderId: String,
    val payType: Int,
    val remark: String,
    val remarks: String,
    val responseCode: String,
    val responseMsg: String,
    val shopId: Int,
    val smallImg: String,
    val title: String,
    val transAmt: Double,
    val transTimeout: String,
    val updateTime: String,
    val userId: String,
    val vipTimeout: String
):Serializable

data class ShopVipTime(
    val buyTime: Int,
    val desc: String,
    val id: Int,
    val transAmt: Double
):Serializable

data class ShopInfoImg(
    val commentId: String,
    val createTime: String,
    val gotoUrl: String,
    val id: Int,
    val imgUrl: String,
    val shopId: Int,
    val type: Int,
    val updateTime: String
):Serializable

data class ShopBannerImg(
    val commentId: String,
    val createTime: String,
    val gotoUrl: String,
    val id: Int,
    val imgUrl: String,
    val shopId: Int,
    val type: Int,
    val updateTime: String
):Serializable