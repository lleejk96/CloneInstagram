package com.gitauto.cloneinstgram.adapter

import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gitauto.cloneinstgram.data.ContentDTO


class UserViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var contentDTO : ArrayList<ContentDTO>? = null
    private var width : Int = 0
    inner class UserViewHolder(val imageview: ImageView) : RecyclerView.ViewHolder(imageview){

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val imageView = ImageView(parent.context)
        imageView.layoutParams = LinearLayoutCompat.LayoutParams(width,width)
        return UserViewHolder(imageView)
    }

    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var imageview = (p0 as UserViewHolder).imageview
        Glide.with(p0.itemView.context).load(contentDTO?.get(p1)?.imageUrl).apply(RequestOptions().centerCrop()).into(imageview)
    }

    override fun getItemCount(): Int {
        return contentDTO?.size ?: 0
    }
    fun setList(contentDTO: ArrayList<ContentDTO>){
        this.contentDTO = contentDTO
    }
    fun setWidth(width : Int){
        this.width = width
    }
}