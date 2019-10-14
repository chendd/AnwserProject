package com.wetin.anwserproject.widget

import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.table.ChapterBean
import com.wetin.anwserproject.service.AudioPlayer
import kotlinx.android.synthetic.main.layout_music_dialog.*

class MusicListDialog(context: Context) : BaseUpDownDialog(context) {
    val mAdapter=Adapter(null)
    override fun getContentView(): Int {
        return R.layout.layout_music_dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wg_recycle.layoutManager=LinearLayoutManager(context)
        wg_recycle.adapter=mAdapter
        mAdapter.setOnItemClickListener { adapter, view, position ->
            AudioPlayer.get().addAndPlay((adapter.data as ArrayList<ChapterBean>)[position])
        }
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            AudioPlayer.get().delete(position)
            mAdapter.notifyDataSetChanged()
            true
        }
    }

    fun setData(data:ArrayList<ChapterBean>){
        mAdapter.setNewData(data)
    }



    class Adapter(data: MutableList<ChapterBean>?) : BaseQuickAdapter<ChapterBean, BaseViewHolder>(R.layout.item_music,data) {
        override fun convert(helper: BaseViewHolder?, item: ChapterBean?) {
            helper?.setText(R.id.wg_name,item?.name)
            helper?.addOnClickListener(R.id.wg_delete)
        }

    }
}