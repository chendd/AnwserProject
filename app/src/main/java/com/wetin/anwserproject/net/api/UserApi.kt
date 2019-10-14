package com.wetin.anwserproject.net.api

import com.wetin.anwserproject.bean.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    /**发送短信*/
    @FormUrlEncoded
    @POST("question/user/sendSms")
    fun sendSms(@Field("phone")phone:String,@Field("type") type:String):Call<ResultBean<String>>

    /**注册*/
    @FormUrlEncoded
    @POST("question/user/reg")
    fun register(@Field("phone")phone: String,
                 @Field("passWord") pass:String,
                 @Field("smsCode")smsCode:String):Call<ResultBean<String>>

    /**密码短信验证码登录*/
    @FormUrlEncoded
    @POST("question/user/login")
    fun login(@Field("phone")phone:String?=null,
              @Field("passWord")passWord:String?=null,
              @Field("smsCode")smsCode: String?=null,
              @Field("loginType") loginType:String):Call<ResultBean<UserBean>>


    /**微信快捷登录*/
    @FormUrlEncoded
    @POST("question/user/wachatLogin")
    fun wetChatLogin(@Field("code")code:String):Call<ResultBean<UserBean>>

    /**修改用户资料*/
    @FormUrlEncoded
    @POST("question/user/update")
    fun update(@Field("userId")userId:String,
               @Field("headImg")headImg:String?=null,
               @Field("nickName")nickName:String?=null,
               @Field("sex")sex:String?=null,
               @Field("testYear")testYear:String?=null,
               @Field("specialtySchool")specialtySchool:String?=null,
               @Field("testSchool")testSchool:String?=null):Call<ResultBean<UserBean>>

    /**忘记密码*/
    @FormUrlEncoded
    @POST("/question/user/password/forget")
    fun forget(@Field("phone")phone:String,
               @Field("passWord")passWord:String,
               @Field("smsCode")smsCode: String):Call<ResultBean<String>>

    /**上传文件*/
    @Multipart
    @POST("/question/upload/file")
    fun uploadFile(@Part file: MultipartBody.Part):Call<ResultBean<String>>

    /**收藏/错题列表*/
    @FormUrlEncoded
    @POST("/question/user/question/errorOrFavorite/query")
    fun CollectOrFaileByUser(@Field("type") type:Int=1)

    /**上传答题*/
    @FormUrlEncoded
    @POST("/question/user/question/add")
    fun uploadAnswer(@Field("answer") answer:String):Call<ResultBean<String>>

    /**上传收藏*/
    @FormUrlEncoded
    @POST("/question/user/favorites/add")
    fun uploadCollect(@Field("type")type:Int,@Field("favorites") favorites:String):Call<ResultBean<String>>

    /**我的评论*/
    @FormUrlEncoded
    @POST("question/comment/list")
    fun myComment(@Field("commentType")commentType:Int,
                  @Field("number") number:Int,
                  @Field("size") size:Int=10
                  ):Call<ResultBean<PagerBean<ArrayList<Comment>>>>
    /**我的消息*/
    @FormUrlEncoded
    @POST("/question/user/messge/list")
    fun getMsgList(@Field("number") number:Int,
                  @Field("size") size:Int=10
                  ):Call<ResultBean<PagerBean<ArrayList<MsgBean>>>>
    /**我的订单*/
    @FormUrlEncoded
    @POST("/question/payment/list")
    fun getOrderList(@Field("number") number:Int,
                  @Field("size") size:Int=10
                  ):Call<ResultBean<PagerBean<ArrayList<OrderBean>>>>

    /**支付宝支付*/
    @FormUrlEncoded
    @POST("/question/payment/orderPay")
    fun createAlipayOrder(@Field("shopId") shopId:Int,
                  @Field("buyTime") buyTime:String?=null,
                  @Field("payType") payType:Int=1,
                  @Field("transAmt") transAmt:String,
                  @Field("timestamp") timestamp:String,
                  @Field("sign") sign:String
                  ):Call<ResultBean<String>>


    /**微信支付*/
    @FormUrlEncoded
    @POST("/question/payment/orderPay")
    fun createWechatOrder(@Field("shopId") shopId:Int,
                          @Field("buyTime") buyTime:String?=null,
                          @Field("payType") payType:Int=2,
                          @Field("transAmt") transAmt:String,
                          @Field("timestamp") timestamp:String,
                          @Field("sign") sign:String
    ):Call<ResultBean<WeChatPayInfo>>

    /**晒单与评价*/
    @FormUrlEncoded
    @POST("/question/shop/comment/save")
    fun commentSave(@Field("orderId") orderId:String,
                          @Field("commentContent") commentContent:String,
                          @Field("imgUrl") imgUrl:String
    ):Call<ResultBean<String>>
}