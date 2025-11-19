package com.ciit.tadaimaotakustore.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemRecommendationBinding

class RecommendationAdapter(
    private val items: List<Int>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val imageRes = items[position]
        holder.binding.ivRecommendation.setImageResource(imageRes)
        holder.itemView.setOnClickListener {
            onItemClick(holder.adapterPosition)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class RecommendationViewHolder(val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root)
}