package com.ciit.tadaimaotakustore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemWishlistBinding

class WishlistAdapter(private var items: List<WishlistItem>) : RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val binding = ItemWishlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WishlistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val item = items[position]
        holder.binding.tvItemName.text = item.name
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(newItems: List<WishlistItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    inner class WishlistViewHolder(val binding: ItemWishlistBinding) : RecyclerView.ViewHolder(binding.root)
}