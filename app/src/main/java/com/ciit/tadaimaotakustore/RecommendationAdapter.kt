package com.ciit.tadaimaotakustore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemRecommendationBinding

class RecommendationAdapter(private val items: List<RecommendationItem>) :
    RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("itemName", item.name)
                putFloat("itemPrice", item.price.toFloat())
                putInt("imageResId", item.imageResource)
            }
            it.findNavController().navigate(R.id.action_nav_home_to_nav_item_view, bundle)
        }
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendationItem) {
            binding.ivRecommendation.setImageResource(item.imageResource)
            binding.tvItemName.text = item.name
            binding.tvItemPrice.text = "₱" + String.format("%.2f", item.price)
        }
    }
}