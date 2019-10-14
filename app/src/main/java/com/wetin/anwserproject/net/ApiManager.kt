package com.wetin.anwserproject.net

import android.content.Context
import android.util.Log
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * api访问
 * Created by huangzhibo on 2018-03-01.
 * mail:1043202454@qq.com
 * 请在Application初始化ApiManager 并配置参数----如果不这样会造成拦截器添加多个
 * 因为retrofit是单利模式
 * 依赖:
 *  //Retrofit
 *   implementation 'com.squareup.retrofit2:retrofit:2.3.0'
 *   implementation 'com.squareup.retrofit2:converter-gson:2.3.0'
 *   implementation 'com.squareup.retrofit2:adapter-rxjava:2.3.0'
 *   implementation 'com.squareup.okhttp3:logging-interceptor:3.8.0'
 *   //String 解析
 *   implementation 'com.squareup.retrofit2:converter-scalars:2.3.0'
 */
class ApiManager private constructor() {
    private var factory: Converter.Factory = GsonConverterFactory.create()  //默认Gson解析

    private lateinit var retrofit: Retrofit
    private lateinit var builder: OkHttpClient.Builder
    private var rxJava:RxJavaCallAdapterFactory?=null

    fun with(context: Context,deBug:Boolean=false):ApiManager {
        initOKhttp(context,deBug)
        return this
    }

    /**
     * 初始化
     */
    private fun initOKhttp(context: Context,deBug:Boolean) {

        builder = OkHttpClient.Builder()
        /**打印日志*/
        if(deBug) {
            val loggingInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                Log.i("inter","retrofit---$message")
            })
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(loggingInterceptor)
        }

        //OkhttpClient设置
        val cache = Cache(File(context.cacheDir, "file_okhttp"), 14 * 1024 * 100)
        builder.cache(cache)
        builder.connectTimeout(15, TimeUnit.SECONDS)
        builder.readTimeout(15, TimeUnit.SECONDS)
        builder.writeTimeout(15, TimeUnit.SECONDS)
    }


    companion object {
        fun getInstance(): ApiManager {
            return Inner.apiManager
        }
    }

    private object Inner {
        val apiManager = ApiManager()
    }

    /**
     *设置数据转化器
     * @factory
     */
    fun setConverterFactory(factory: Converter.Factory): ApiManager {
        this.factory = factory
        return this
    }

    fun setCallAdapterFactory(rxJava:RxJavaCallAdapterFactory ):ApiManager{
         this.rxJava=rxJava
         return this
    }
    /**
     * api接口类导入
     */
    fun <T> createRequestClass(reqService: Class<T>): T {
        return retrofit.create(reqService)
    }

    /**
     * 添加拦截 *注意不要多次调用
     * 放在初始化中
     */
    fun addIntercepter(intercept: Interceptor): ApiManager {
        builder.addInterceptor(intercept)
        return this
    }



    fun build(Service_host: String): ApiManager {
        val client = Retrofit.Builder().client(builder.build())
        retrofit = if (rxJava==null){
            client.addConverterFactory(factory)
                .baseUrl(Service_host)
                .build()
        }else {
            client.addConverterFactory(factory)
                .addCallAdapterFactory(rxJava)
                .baseUrl(Service_host)
                .build()
        }
        return this
    }




}