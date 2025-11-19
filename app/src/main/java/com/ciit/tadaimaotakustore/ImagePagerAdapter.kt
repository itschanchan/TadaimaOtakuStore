package com.ciit.tadaimaotakustore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemImagePagerBinding

class ImagePagerAdapter(private val images: List<Int>, private val onItemClick: (Int) -> Unit) :
    RecyclerView.Adapter<ImagePagerAdapter.ImagePagerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagePagerViewHolder {
        val binding = ItemImagePagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImagePagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImagePagerViewHolder, position: Int) {
        val imageResId = images[position]
        holder.binding.imageView.setImageResource(imageResId)
        holder.itemView.setOnClickListener {
            onItemClick(imageResId)
        }
    }

    override fun getItemCount(): Int = images.size

    inner class ImagePagerViewHolder(val binding: ItemImagePagerBinding) : RecyclerView.ViewHolder(binding.root)
}