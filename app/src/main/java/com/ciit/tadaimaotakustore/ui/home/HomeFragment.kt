package com.ciit.tadaimaotakustore.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.ciit.tadaimaotakustore.R
import com.ciit.tadaimaotakustore.RecommendationAdapter
import com.ciit.tadaimaotakustore.RecommendationItem
import com.ciit.tadaimaotakustore.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Carousel Images
        val carouselImages = listOf(
            R.drawable.featured_gundam_witch_from_mercury,
            R.drawable.carousel_image_2,
            R.drawable.carousel_image_3
        )
        val carouselAdapter = CarouselImageAdapter(carouselImages)
        binding.imageCarousel.adapter = carouselAdapter
        TabLayoutMediator(binding.tabLayout, binding.imageCarousel) { _, _ -> }.attach()

        // Recommendations
        val recommendationItems = listOf(
            RecommendationItem(R.drawable.product_suletta_plushie, "Suletta Plushie", 800.00, "Plushies"),
            RecommendationItem(R.drawable.product_miorine_plushie, "Miorine Plushie", 800.00, "Plushies"),
            RecommendationItem(R.drawable.product_sd_gundam_aerial, "SD Gundam Aerial", 1200.00, "Figurines")
        )
        val recommendationAdapter = RecommendationAdapter(recommendationItems)
        binding.rvRecommendations.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rvRecommendations.adapter = recommendationAdapter

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
