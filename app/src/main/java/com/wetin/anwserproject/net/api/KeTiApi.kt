package com.wetin.anwserproject.net.api

import com.wetin.anwserproject.bean.*
import com.wetin.anwserproject.bean.table.ContentBean
import com.wetin.anwserproject.bean.table.XueKeBean
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface KeTiApi {

    @POST("/question/user/question/type/query")
    fun getQuestion():Call<ResultBean<List<XueKeBean>>>

    /**content 题目  1 题库 2、收藏 3、错题*/
    @FormUrlEncoded
    @POST("/question/question/count/list")
    fun getContentByChapterId(@Field("type") type:Int, @Field("childId") chapterId:Int):Call<ResultBean<ArrayList<ContentBean>>>

    /**音视频接口  3-音频  4-视频*/
    @FormUrlEncoded
    @POST("/question/user/video/type/query")
    fun queryVideoOrAudio(@Field("type") type:Int):Call<ResultBean<ArrayList<XueKeBean>>>

    /**评论类型 1 、题库 2、音频 3、视频*/
    @FormUrlEncoded
    @POST("/question/comment/list")
    fun getCommentList(@Field("type") type:Int,@Field("questionId") questionId:String,@Field("number") number:Int):Call<ResultBean<PagerBean<ArrayList<Comment>>>>

    /**添加评论*/
    @FormUrlEncoded
    @POST("/question/comment/save")
    fun addComment(@Field("type") type:Int,@Field("questionId") questionId:String,
                   @Field("commentContent") commentContent:String,
                   @Field("replayCommentId") replayCommentId:String?=null):Call<ResultBean<String>>


    /**删除评论*/
    @FormUrlEncoded
    @POST("/question/comment/del")
    fun delComment(@Field("id") id:Int):Call<ResultBean<String>>

    /**商品详情*/
    @FormUrlEncoded
    @POST("/question/shop/find")
    fun getShopDesc(@Field("type") type:Int,@Field("parentId") parentId:String?):Call<ResultBean<ShopDesc>>

    /**分享解锁*/
    @FormUrlEncoded
    //被分享的类型：1 题库 2音视频-音频3音视频-视频
    @POST("/question/user/share/add")
    fun shareOpen(@Field("type") type:Int,@Field("parentId") parentId:String?,
                  @Field("shareId") shareId:String?=null,
                  @Field("shareType") shareType:Int):Call<ResultBean<String>>

    @FormUrlEncoded
    @POST("/question/shop/comment/list")
    fun  shopCommentList(@Field("shopId") shopId:String,@Field("number") number:Int):Call<ResultBean<PagerBean<ArrayList<ShopComment>>>>

    @FormUrlEncoded
    @POST("/question/payment/query/viptime")
    fun  viptime(@Field("type") type:Int,@Field("buyTime") buyTime:Int):Call<ResultBean<String>>

}