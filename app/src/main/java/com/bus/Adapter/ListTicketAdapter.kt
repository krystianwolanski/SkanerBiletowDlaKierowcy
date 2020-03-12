package com.bus.Adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.bus.Model.Ticket
import com.bus.R
import kotlinx.android.synthetic.main.row_layout.view.*

class ListTicketAdapter(internal var activity: Activity,
                        internal var lstTicket: List<Ticket>
                        ): BaseAdapter() {



    internal var inflater:LayoutInflater

    init{
        inflater=activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val rowView:View
        rowView = inflater.inflate(R.layout.row_layout,null)

        rowView.row_id.text = lstTicket[position].qrCode
        if(lstTicket[position].scanned!!){
            rowView.row_status.text = "Zeskanowany"
        }else{
            rowView.row_status.text = "Nieskanowany"
        }
        rowView.row_departure.text = lstTicket[position].busConnection!!.departure.toString()
        rowView.row_arrival.text = lstTicket[position].busConnection!!.arrival.toString()

        return rowView
    }

    override fun getItem(position: Int): Any {
        return lstTicket[position]
    }

    override fun getItemId(position: Int): Long {
        return lstTicket[position].idTicket!!.toLong()
    }

    override fun getCount(): Int {
        return lstTicket.size
    }
}