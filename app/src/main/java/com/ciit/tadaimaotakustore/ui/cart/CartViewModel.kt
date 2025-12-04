package com.ciit.tadaimaotakustore.ui.cart

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class CartItem(
    val id: String,
    val name: String,
    var quantity: Int,
    val price: Float,
    var isChecked: Boolean = true
)

class CartViewModel : ViewModel() {

    private val _cartItems = MutableLiveData<MutableList<CartItem>>(mutableListOf())
    val cartItems: LiveData<MutableList<CartItem>> = _cartItems

    val cartItemCount: LiveData<Int> = MutableLiveData<Int>().apply {
        value = _cartItems.value?.size ?: 0
    }

    val totalCartItemQuantity: LiveData<Int> = MutableLiveData<Int>().apply {
        value = _cartItems.value?.sumOf { it.quantity } ?: 0
    }

    fun toggleAllItems(isChecked: Boolean) {
        val currentItems = _cartItems.value ?: return
        currentItems.forEach { it.isChecked = isChecked }
        _cartItems.value = currentItems
    }

    fun addToCart(item: CartItem) {
        val currentItems = _cartItems.value ?: mutableListOf()
        val existingItem = currentItems.find { it.id == item.id }
        if (existingItem != null) {
            existingItem.quantity++
        } else {
            currentItems.add(item)
        }
        _cartItems.value = currentItems
        (cartItemCount as MutableLiveData).value = currentItems.size
        (totalCartItemQuantity as MutableLiveData).value = currentItems.sumOf { it.quantity }
    }

    fun increaseQuantity(item: CartItem) {
        val currentItems = _cartItems.value ?: return
        val existingItem = currentItems.find { it.id == item.id } ?: return
        existingItem.quantity++
        _cartItems.value = currentItems
        (cartItemCount as MutableLiveData).value = currentItems.size
        (totalCartItemQuantity as MutableLiveData).value = currentItems.sumOf { it.quantity }
    }

    fun decreaseQuantity(item: CartItem) {
        val currentItems = _cartItems.value ?: return
        val existingItem = currentItems.find { it.id == item.id } ?: return
        if (existingItem.quantity > 1) {
            existingItem.quantity--
        } else {
            currentItems.remove(existingItem)
        }
        _cartItems.value = currentItems
        (cartItemCount as MutableLiveData).value = currentItems.size
        (totalCartItemQuantity as MutableLiveData).value = currentItems.sumOf { it.quantity }
    }
}