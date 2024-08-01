package com.example.mlseriescamera.helpers

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mlseriescamera.R


class ImageAdapter (private val imageUris: List<Uri>): RecyclerView.Adapter<ImageAdapter.ImageViewHolder>(){

    inner class ImageViewHolder(itemView:View):  RecyclerView.ViewHolder(itemView){
        val imageView: ImageView = itemView.findViewById(R.id.rowImageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_row,parent, false)
        return ImageViewHolder(view)
    }

    override fun getItemCount(): Int = imageUris.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = imageUris[position]
        Glide.with(holder.imageView.context)
            .load(uri)
            .into(holder.imageView)
    }

}