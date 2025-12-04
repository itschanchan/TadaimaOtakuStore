package com.ciit.tadaimaotakustore.ui.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ciit.tadaimaotakustore.databinding.ItemCartBinding

class CartAdapter(
    private val cartViewModel: CartViewModel
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var items = listOf<CartItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size

    fun setItems(items: List<CartItem>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class CartViewHolder(private val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cartItem: CartItem) {
            binding.checkboxItem.isChecked = cartItem.isChecked
            binding.tvItemName.text = cartItem.name
            binding.tvItemPrice.text = String.format("%.2f", cartItem.price)
            binding.tvQuantity.text = cartItem.quantity.toString()

            binding.checkboxItem.setOnCheckedChangeListener { _, isChecked ->
                cartItem.isChecked = isChecked
            }

            binding.btnPlus.setOnClickListener {
                cartViewModel.increaseQuantity(cartItem)
            }

            binding.btnMinus.setOnClickListener {
                cartViewModel.decreaseQuantity(cartItem)
            }
        }
    }
}