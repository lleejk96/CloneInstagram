package com.gitauto.cloneinstgram.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gitauto.cloneinstgram.R
import com.gitauto.cloneinstgram.data.ContentDTO
import com.gitauto.cloneinstgram.databinding.InstagramContentBinding
import com.gitauto.cloneinstgram.databinding.ItemDetailBinding
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object{
        const val TAG = "DetailViewAdapter"
    }
    private var contentDTO : ArrayList<ContentDTO>? = null
    private var contentUidList : ArrayList<String>? = null
    private lateinit var binding : ItemDetailBinding
    private var listener : OnItemClickListener ?= null
    private var userListener : OnUserClickListener ?= null
    private lateinit var viewHolder: InstagramViewHolder
    private var result = ""
    fun setList(contentDTO: ArrayList<ContentDTO>?, contentUidLIST : ArrayList<String>?){
        this.contentDTO = contentDTO
        this.contentUidList = contentUidLIST
    }
    fun uploadFavoriteStatus(like : String?){
        if (like != null) {
            result = like
        }
    }
    fun setListener(listener : OnItemClickListener, userListener : OnUserClickListener){
        this.listener = listener
        this.userListener = userListener
    }
    inner class InstagramViewHolder(private val binding : ItemDetailBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(content : ContentDTO){
            binding.profileName.text = content.userId
            Glide.with(binding.root).load(content.imageUrl).into(binding.contentPhoto)
            binding.itemExplain.text = content.explain
            binding.favoriteCount.text = "Likes " + content.favoriteCount
            if (content.favorites[content.uid] == true)
                binding.contentFavorite.setImageResource(R.drawable.ic_favorite)
            else
                binding.contentFavorite.setImageResource(R.drawable.ic_favorite_border)

            val pos = adapterPosition
            if (pos != RecyclerView.NO_POSITION){
                binding.contentFavorite.setOnClickListener {
                    listener?.onItemClick(pos)
                    Log.d(TAG, "bind: $result  $pos")
                    if (result == "like")
                        binding.contentFavorite.setImageResource(R.drawable.ic_favorite)
                    else
                        binding.contentFavorite.setImageResource(R.drawable.ic_favorite_border)
                    notifyDataSetChanged()
                }
                binding.profileImage.setOnClickListener {
                    content.uid?.let { itUid -> content.userId?.let { itUserId ->
                        userListener?.onUserClick(itUid, itUserId) } }
                }
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemDetailBinding.inflate(layoutInflater, parent, false)
        return InstagramViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        viewHolder = (holder as InstagramViewHolder)
        contentDTO?.get(holder.adapterPosition)?.let { viewHolder.bind(it) }
    }

    override fun getItemCount(): Int {
        return contentDTO?.size ?: 0
    }

    interface OnItemClickListener{
        fun onItemClick(pos : Int)
    }
    interface OnUserClickListener{
        fun onUserClick(uid : String, userId : String)
    }
}