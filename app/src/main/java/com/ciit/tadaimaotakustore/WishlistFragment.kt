package com.ciit.tadaimaotakustore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciit.tadaimaotakustore.databinding.FragmentWishlistBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class WishlistFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentWishlistBinding? = null
    private val binding get() = _binding!!

    private val wishlistViewModel: WishlistViewModel by activityViewModels()
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        wishlistViewModel.wishlistItems.observe(viewLifecycleOwner) {
            wishlistAdapter.updateItems(it)
        }
    }

    private fun setupRecyclerView() {
        wishlistAdapter = WishlistAdapter(emptyList())
        binding.rvWishlist.apply {
            adapter = wishlistAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}