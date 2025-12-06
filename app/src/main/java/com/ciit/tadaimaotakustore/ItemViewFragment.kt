package com.ciit.tadaimaotakustore

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.ciit.tadaimaotakustore.ui.cart.CartItem
import com.ciit.tadaimaotakustore.ui.cart.CartViewModel
import com.ciit.tadaimaotakustore.databinding.FragmentItemViewBinding
import com.google.android.material.snackbar.Snackbar

class ItemViewFragment : Fragment() {

    private var _binding: FragmentItemViewBinding? = null
    private val binding get() = _binding!!

    private val wishlistViewModel: WishlistViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()
    private val args: ItemViewFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvItemName.text = args.itemName
        binding.tvPrice.text = "₱" + String.format("%.2f", args.itemPrice)
        binding.btnTag.text = args.itemTag

        val images = listOf(args.imageResId) // For now, only show the first image

        val adapter = ImagePagerAdapter(images) { imageResId ->
            val action = ItemViewFragmentDirections.actionNavItemViewToNavFullScreenImage(imageResId)
            findNavController().navigate(action)
        }
        binding.itemViewPager.adapter = adapter

        binding.btnTag.setOnClickListener {
            val action = ItemViewFragmentDirections.actionNavItemViewToNavCategoryItemsView(args.itemTag)
            findNavController().navigate(action)
        }

        binding.itemViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvImageCounter.text = "${position + 1} / ${images.size}"
            }
        })

        binding.btnPrevious.setOnClickListener {
            binding.itemViewPager.currentItem = binding.itemViewPager.currentItem - 1
        }

        binding.btnNext.setOnClickListener {
            binding.itemViewPager.currentItem = binding.itemViewPager.currentItem + 1
        }

        val currentItem = WishlistItem(args.imageResId.toString(), args.itemName)
        val cartItem = CartItem(args.imageResId.toString(), args.itemName, 1, args.itemPrice)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_chat -> {
                    Snackbar.make(view, "Chat feature coming soon!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_add_to_cart -> {
                    cartViewModel.addToCart(cartItem)
                    Snackbar.make(view, "Added to cart!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_buy_now -> {
                    Snackbar.make(view, "Buy Now feature coming soon!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        cartViewModel.totalCartItemQuantity.observe(viewLifecycleOwner) {
            updateCartBadge(it)
        }

        binding.btnShare.setOnClickListener {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, "Check out this awesome item: ${currentItem.name}!")
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }

        // Wishlist functionality
        updateWishlistButton(wishlistViewModel.isInWishlist(currentItem))

        binding.btnWishlist.setOnClickListener {
            if (wishlistViewModel.isInWishlist(currentItem)) {
                wishlistViewModel.removeFromWishlist(currentItem)
                Snackbar.make(view, "Removed from wishlist!", Snackbar.LENGTH_SHORT).show()
            } else {
                wishlistViewModel.addToWishlist(currentItem)
                Snackbar.make(view, "Added to wishlist!", Snackbar.LENGTH_SHORT).show()
            }
            updateWishlistButton(wishlistViewModel.isInWishlist(currentItem))
        }

        // Set initial counter text
        binding.tvImageCounter.text = "1 / ${images.size}"

        val startingPosition = images.indexOf(args.imageResId)
        if (startingPosition != -1) {
            binding.itemViewPager.currentItem = startingPosition
        }
    }

    private fun updateWishlistButton(inWishlist: Boolean) {
        if (inWishlist) {
            binding.btnWishlist.setImageResource(R.drawable.ic_heart_filled)
        } else {
            binding.btnWishlist.setImageResource(R.drawable.ic_heart_outline)
        }
    }

    private fun updateCartBadge(count: Int) {
        val badge = requireActivity().findViewById<TextView>(R.id.cart_badge)
        if (badge != null) {
            if (count > 0) {
                badge.text = count.toString()
                badge.visibility = View.VISIBLE
            } else {
                badge.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
