package com.wyk.rxsample.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.wyk.rxsample.R
import com.wyk.rxsample.model.ZhuangbiImage

class ZhuangbiListAdapter: RecyclerView.Adapter<ZhuangbiListAdapter.DebounceViewHolder>(){

    var images: List<ZhuangbiImage>? = null
    private set

    fun setData(data: List<ZhuangbiImage>?){
        images = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, p1: Int) =
            DebounceViewHolder(LayoutInflater.from(viewGroup.context).inflate(R.layout.grid_item, viewGroup,false))

    override fun getItemCount() = if(images == null)0 else images!!.size

    override fun onBindViewHolder(viewHolder: DebounceViewHolder, index: Int) {
        Glide.with(viewHolder.itemView.context).load(images?.get(index)?.image_url).into(viewHolder.mImg)
        viewHolder.mDescription.text = images?.get(index)?.description
    }


    inner class DebounceViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var mImg:ImageView
        var mDescription: TextView
        init {
            mImg = itemView.findViewById<ImageView>(R.id.imageIv)
            mDescription = itemView.findViewById<TextView>(R.id.descriptionTv)
        }
    }
}