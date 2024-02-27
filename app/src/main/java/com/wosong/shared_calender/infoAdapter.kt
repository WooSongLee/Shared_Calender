package com.wosong.shared_calender

import Model.SavingModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class infoAdapter(val context : Context, val items : List<SavingModel>) : BaseAdapter(){
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
            convertView = LayoutInflater.from(parent?.context).inflate(R.layout.lisview_dialog_item,parent,false)
        }
        val nickname = convertView!!.findViewById<TextView>(R.id.infoNickname)
        val hour = convertView!!.findViewById<TextView>(R.id.inforHour)
        val minute = convertView!!.findViewById<TextView>(R.id.ifoMinute)
        val information = convertView!!.findViewById<TextView>(R.id.infoInformation)

        nickname.text = items[position].nickname
        hour.text = items[position].hour
        minute.text = items[position].minute
        information.text = items[position].info

        return convertView!!
    }
}