package com.ciit.tadaimaotakustore

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

// A simple data class to represent a wishlist item
data class WishlistItem(val id: String, val name: String)

class WishlistViewModel : ViewModel() {

    private val _wishlistItems = MutableLiveData<List<WishlistItem>>(emptyList())
    val wishlistItems: LiveData<List<WishlistItem>> = _wishlistItems

    val wishlistItemCount: LiveData<Int> = MutableLiveData<Int>().apply {
        wishlistItems.observeForever {
            value = it.size
        }
    }

    fun isInWishlist(item: WishlistItem): Boolean {
        return _wishlistItems.value?.contains(item) ?: false
    }

    fun addToWishlist(item: WishlistItem) {
        val currentList = _wishlistItems.value?.toMutableList() ?: mutableListOf()
        if (!currentList.contains(item)) {
            currentList.add(item)
            _wishlistItems.value = currentList
        }
    }

    fun removeFromWishlist(item: WishlistItem) {
        val currentList = _wishlistItems.value?.toMutableList() ?: mutableListOf()
        if (currentList.remove(item)) {
            _wishlistItems.value = currentList
        }
    }
}
