package com.wetin.anwserproject.presnter

import android.os.Bundle
import com.wetin.anwserproject.ui.fragment.contract.HomeFragContranct
import com.wetin.common.base.BasePresenter

class HomeFragPresenter(mView: HomeFragContranct.View?) : BasePresenter<HomeFragContranct.View>(mView) {
    /**
     * 容易被回收掉时保存数据
     */
    override fun onSaveInstanceState(outState: Bundle?) {
    }

    /**
     * 做初始化的操作,需要在V的视图初始化完成之后才能调用
     * presenter进行初始化.
     */
    override fun onCreate() {
    }


    override fun onDestory() {
    }
}