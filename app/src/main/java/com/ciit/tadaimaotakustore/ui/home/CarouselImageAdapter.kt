package com.ciit.tadaimaotakustore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemCarouselImageBinding

class CarouselImageAdapter(private val images: List<Int>) :
    RecyclerView.Adapter<CarouselImageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCarouselImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(private val binding: ItemCarouselImageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageRes: Int) {
            binding.ivCarouselImage.setImageResource(imageRes)
        }
    }
}