package com.wetin.common.utils.loadimg

import android.content.Context

/**
 * 图片加载配置
 *
 * @context:上下文
 * @skipMemoryCache:禁止内存
 * @errorimg：默认错误图
 * @placeImg：占位图
 * @isFade：是否渐变显示
 */
class ImageConfig(var context: Context,
                  var skipMemoryCache:Boolean,
                  var errorimg:Int,
                  var placeImg:Int,
                  var isFade:Boolean)