package com.ciit.tadaimaotakustore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class CartItem(val id: String, val name: String, var quantity: Int)

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    private val _cartItemCount = MutableLiveData<Int>(0)
    val cartItemCount: LiveData<Int> = _cartItemCount

    fun addToCart(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.id == item.id }

        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(item)
        }

        _cartItems.value = currentItems
        updateCartItemCount()
    }

    fun removeFromCart(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        currentItems.remove(item)
        _cartItems.value = currentItems
        updateCartItemCount()
    }

    private fun updateCartItemCount() {
        _cartItemCount.value = _cartItems.value?.sumOf { it.quantity } ?: 0
    }
}
