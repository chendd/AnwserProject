package com.wetin.anwserproject.adapter

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.table.XueKeBean
import com.wetin.anwserproject.utils.Config
import com.wetin.common.utils.loadimg.ImageLoadUtil

/**
 * 1 ->针对视频的adapter
 */

class ExtendAdapter(var mContext: Context, var data: ArrayList<XueKeBean>?) :
    BaseExpandableListAdapter() {
    /**
     * Gets the data associated with the given group.
     *
     * @param groupPosition the position of the group
     * @return the data child for the specified group
     */
    override fun getGroup(groupPosition: Int): Any {
        return data?.get(groupPosition) ?: ""
    }


    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    /**
     * 初始化父view的绑定
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the group at the specified position
     */
    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup?): View {
        val groupViewHolder: ExtendUserAdapter.GroupViewHolder
        var createView: View
        if (convertView == null) {
            createView = LayoutInflater.from(parent?.context).inflate(R.layout.item_partent_list01, parent, false)
            groupViewHolder = ExtendUserAdapter.GroupViewHolder()
            groupViewHolder.wg_tv_title = createView.findViewById(R.id.wg_part_title)
            groupViewHolder.wg_tv_count = createView.findViewById(R.id.wg_part_count)
            createView?.tag = groupViewHolder
        } else {
            createView = convertView
            groupViewHolder = createView.tag as ExtendUserAdapter.GroupViewHolder
        }
        val xueKeBean = data?.get(groupPosition)
        groupViewHolder.wg_tv_count.text = "${xueKeBean?.questionCount}题"
        groupViewHolder.wg_tv_title.text = xueKeBean?.name
        return createView
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return data?.get(groupPosition)?.child?.size ?: 0
    }


    override fun getChild(groupPosition: Int, childPosition: Int): Any {
        return data?.get(groupPosition)?.child?.get(childPosition) ?: ""
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }


    /**
     *
     * 初始化子view的绑定
     * @param parent the parent that this view will eventually be attached to
     * @return the View corresponding to the child at the specified position
     */
    override fun getChildView(
        groupPosition: Int,
        childPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        var createView: View
        val childViewHolder: ChildVideoHolder
        if (convertView == null) {
            childViewHolder = ChildVideoHolder()
            createView = LayoutInflater.from(parent?.context).inflate(R.layout.item_child_video, parent, false)
            childViewHolder.wg_title = createView.findViewById(R.id.wg_name)
            childViewHolder.wg_img = createView.findViewById(R.id.wg_img)
            childViewHolder.wg_author = createView.findViewById(R.id.wg_author)
            childViewHolder.wg_play_count = createView.findViewById(R.id.wg_play_count)
            childViewHolder.wg_play_time = createView.findViewById(R.id.wg_play_time)
            childViewHolder.wg_play_status = createView.findViewById<CheckBox>(R.id.wg_play_status)
            createView?.tag = childViewHolder
        } else {
            createView = convertView
            childViewHolder = createView.tag as ChildVideoHolder
        }
        //赋值
        val chapterBean = data?.get(groupPosition)?.child!![childPosition]
        childViewHolder.wg_title.text = chapterBean.name
        childViewHolder.wg_author.text = chapterBean.author
        childViewHolder.wg_play_count.text = "${chapterBean.playCount}"
        childViewHolder.wg_play_time.text = "${chapterBean.videoTime}"
        childViewHolder.wg_play_status.isSelected = chapterBean.unlockType != 0
        ImageLoadUtil.instance.loadImg(mContext, childViewHolder.wg_img, chapterBean.imgUrl)
        return createView

    }


    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    /**
     *
     * 父级总数
     * @return the number of groups
     */
    override fun getGroupCount(): Int {
        return data?.size ?: 0
    }

    /**
     * 父View
     */
    class GroupViewHolder {
        lateinit var wg_tv_title: TextView
        lateinit var wg_tv_count: TextView
        lateinit var wg_process: ProgressBar
    }

    /*   */
    /**
     * 子view---文章
     *//*
    class ChildViewHolder {
        lateinit var wg_title: TextView
        lateinit var wg_tv_count: TextView
        lateinit var wg_suo_info: TextView
        lateinit var wg_suo_bar: ConstraintLayout
    }*/

    /**
     * 子view---视频
     */
    class ChildVideoHolder {
        lateinit var wg_title: TextView
        lateinit var wg_img: ImageView
        lateinit var wg_author: TextView
        lateinit var wg_play_count: TextView
        lateinit var wg_play_time: TextView
        lateinit var wg_play_status: CheckBox
    }
}