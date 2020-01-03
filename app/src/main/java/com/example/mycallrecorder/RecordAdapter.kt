package com.example.mycallrecorder

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.record_layout.view.*

class RecordAdapter(val recordList:ArrayList<CallDetails>): RecyclerView.Adapter<RecordAdapter.ViewHolder>() {
    var longClickListener:onLongClicklistener?=null
    var clickListener:onClickListener?=null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = li.inflate(R.layout.record_layout, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount() :Int {

         return recordList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val callitem = recordList[position]
        holder.itemView.cardView.setOnLongClickListener {
               longClickListener!!.onLongClick(callitem)
        }
        holder.itemView.cardView.setOnClickListener {
            clickListener!!.onClick()

        }



        holder.bind(callitem)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(datarecord: CallDetails) {

            with(itemView) {
                contactView.text=datarecord.name
                numberView.text=datarecord.num
                timeView.text=datarecord.time
                dateView.text=datarecord.date

            }

        }

    }


}