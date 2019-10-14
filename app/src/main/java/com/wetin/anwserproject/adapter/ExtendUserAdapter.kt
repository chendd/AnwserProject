package com.wetin.anwserproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.wetin.anwserproject.R
import com.wetin.anwserproject.bean.table.XueKeBean

/**
 * 针对用户的题库Adapter 1->错题 2->收藏
 */

class ExtendUserAdapter(var mContext: Context, var data: ArrayList<XueKeBean>?) :
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
        val groupViewHolder: GroupViewHolder
        var createView: View
        if (convertView == null) {
            createView = LayoutInflater.from(parent?.context).inflate(R.layout.item_partent_list01, parent, false)
            groupViewHolder = GroupViewHolder()
            groupViewHolder.wg_tv_title = createView.findViewById(R.id.wg_part_title)
            groupViewHolder.wg_tv_count = createView.findViewById(R.id.wg_part_count)
            createView?.tag = groupViewHolder
        } else {
            createView = convertView
            groupViewHolder = createView.tag as GroupViewHolder
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
        val childViewHolder: ChildViewHolder
        var createView: View
        val chapterBean = data?.get(groupPosition)?.child!![childPosition]
        if (convertView == null) {
            childViewHolder = ChildViewHolder()
            createView = LayoutInflater.from(parent?.context).inflate(R.layout.item_child_list, parent, false)
            childViewHolder.wg_title = createView.findViewById(R.id.wg_child_title)
            childViewHolder.wg_tv_count = createView.findViewById(R.id.wg_count)
            createView?.tag = childViewHolder
        } else {
            createView = convertView
            childViewHolder = createView.tag as ChildViewHolder
        }
        //赋值
        childViewHolder.wg_title.text = chapterBean.name
        childViewHolder.wg_tv_count.text = "${chapterBean.questionCount}题"
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
    }

    /**
     * 子view---文章
     */
    class ChildViewHolder {
        lateinit var wg_title: TextView
        lateinit var wg_tv_count: TextView
    }


}