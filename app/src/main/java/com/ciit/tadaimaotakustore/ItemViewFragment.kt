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
import androidx.viewpager2.widget.ViewPager2
import com.ciit.tadaimaotakustore.databinding.FragmentItemViewBinding
import com.google.android.material.snackbar.Snackbar

class ItemViewFragment : Fragment() {

    private var _binding: FragmentItemViewBinding? = null
    private val binding get() = _binding!!

    private val wishlistViewModel: WishlistViewModel by activityViewModels()
    private val cartViewModel: CartViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentItemViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemName = arguments?.getString("itemName")
        val itemPrice = arguments?.getFloat("itemPrice")
        val imageResId = arguments?.getInt("imageResId") ?: R.drawable.carousel_image_1

        binding.tvItemName.text = itemName
        binding.tvPrice.text = "PHP " + String.format("%.2f", itemPrice)

        val images = listOf(imageResId) // For now, only show the first image

        val adapter = ImagePagerAdapter(images) { imageResId ->
            val bundle = Bundle().apply {
                putInt("imageResId", imageResId)
            }
            findNavController().navigate(R.id.action_nav_item_view_to_nav_full_screen_image, bundle)
        }
        binding.itemViewPager.adapter = adapter

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

        val currentItem = WishlistItem(imageResId.toString(), itemName ?: "")
        val cartItem = CartItem(imageResId.toString(), itemName ?: "", 1)

        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_add_to_cart -> {
                    cartViewModel.addToCart(cartItem)
                    Snackbar.make(view, "Added to cart!", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        cartViewModel.cartItemCount.observe(viewLifecycleOwner) {
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

        val startingPosition = images.indexOf(imageResId)
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
