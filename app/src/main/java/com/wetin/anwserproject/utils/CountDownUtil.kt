package com.wetin.anwserproject.utils

import java.util.*
import java.text.SimpleDateFormat


object CountDownUtil {

    /**
     * 计算两个时间差
     * @return:返回个数数组
     */
    fun getTimeDifference(strTime1: Long, strTime2: Long): ArrayList<Char>?{
        //格式日期格式，在此我用的是"2018-01-24 19:49:50"这种格式
        //可以更改为自己使用的格式，例如：yyyy/MM/dd HH:mm:ss 。。。
        try {
            val l = strTime1 - strTime2      //获取时间差
            val day = l / (24 * 60 * 60 * 1000)
            val hour = l / (60 * 60 * 1000) - day * 24
            val charArray = String.format("%03d", day.toInt()).toCharArray()
            val array = String.format("%02d", hour.toInt()).toCharArray()
            var totle= ArrayList<Char>()
            for (s in charArray){totle.add(s)}
            for (hour in array){totle.add(hour)}
            return totle
        } catch (e: Exception) {
        }
        return null
    }

    fun getSysYear(): Int {

        val date = Calendar.getInstance()

        return date.get(Calendar.YEAR)

    }

    /*
     * 将时间转换为时间戳
     */
    fun dateToStamp(s: String): String {
        val res: String
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date = simpleDateFormat.parse(s)
        val ts = date.time
        res = ts.toString()
        return res
    }

}