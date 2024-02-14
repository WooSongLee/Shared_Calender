package com.wosong.shared_calender

import Model.GroupMakingModel
import android.content.Context
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class MainAdapter(val context : Context, val items : List<GroupMakingModel>) : BaseAdapter() {
    override fun getCount(): Int {
        return items.size
    }

    override fun getItem(position: Int): Any {
        return items[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var convertView = convertView
        if(convertView == null){
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.grouplist_item,parent,false)
        }
        val groupname = convertView!!.findViewById<TextView>(R.id.groupname)
        val groupCount = convertView!!.findViewById<TextView>(R.id.groupCount)

        groupname.text = items[position].groupName
        groupCount.text = items[position].groupCount.toString()
        return convertView!!
    }
}