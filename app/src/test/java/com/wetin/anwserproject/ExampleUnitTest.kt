package com.wetin.anwserproject

import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {

    }

    private fun getTimeDifference(strTime1: Long, strTime2: Long):ArrayList<Char>?{
        //格式日期格式，在此我用的是"2018-01-24 19:49:50"这种格式
        //可以更改为自己使用的格式，例如：yyyy/MM/dd HH:mm:ss 。。。
        try {
            val l = strTime1 - strTime2      //获取时间差
            val day = l / (24 * 60 * 60 * 1000)
            val hour = l / (60 * 60 * 1000) - day * 24
            val charArray = String.format("%03d", day.toInt()).toCharArray()
            val array = String.format("%02d", hour.toInt()).toCharArray()
            var totle=ArrayList<Char>()
            for (s in charArray){totle.add(s)}
            for (hour in array){totle.add(hour)}
            return totle
        } catch (e: Exception) {
        }
        return null
    }
}
