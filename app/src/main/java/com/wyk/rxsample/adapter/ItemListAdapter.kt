package com.wyk.rxsample.adapter

import android.content.ClipData
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wyk.rxsample.R
import com.wyk.rxsample.model.Item

class ItemListAdapter: RecyclerView.Adapter<ItemListAdapter.DebounceViewHolder>(){

    var items: List<Item>? = null
    private set

    override fun onCreateViewHolder(parent: ViewGroup, position: Int) =
            DebounceViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.grid_item, parent, false))

    override fun getItemCount() = if(items == null) 0 else items!!.size

    override fun onBindViewHolder(viewHolder: DebounceViewHolder, position: Int) {
        viewHolder.mDescription.text = items?.get(position)?.description
        Glide.with(viewHolder.itemView.context).load(items?.get(position)?.imageUrl).into(viewHolder.mImg)
    }

    fun setData(data: List<Item>){
       items = data
        notifyDataSetChanged()
    }

    inner class DebounceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var mImg:ImageView
        var mDescription: TextView
        init {
            mImg = itemView.findViewById(R.id.imageIv)
            mDescription = itemView.findViewById(R.id.descriptionTv)
        }
    }
}