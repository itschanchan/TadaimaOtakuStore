package com.ciit.tadaimaotakustore

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.ciit.tadaimaotakustore.databinding.FragmentListingBinding
import com.google.android.material.tabs.TabLayoutMediator

class ListingFragment : Fragment() {

    private var _binding: FragmentListingBinding? = null
    private val binding get() = _binding!!
    private val imageUris = mutableListOf<Uri>()
    private lateinit var imageCarouselAdapter: ImageCarouselAdapter

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.clipData?.let {
                for (i in 0 until it.itemCount) {
                    imageUris.add(it.getItemAt(i).uri)
                }
            } ?: result.data?.data?.let {
                imageUris.add(it)
            }
            imageCarouselAdapter.notifyDataSetChanged()
            updateCarouselVisibility()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageCarouselAdapter = ImageCarouselAdapter(imageUris)
        binding.imageCarousel.adapter = imageCarouselAdapter

        binding.uploadImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            imagePickerLauncher.launch(intent)
        }

        TabLayoutMediator(binding.carouselIndicator, binding.imageCarousel) { _, _ -> }.attach()

        binding.nextButton.setOnClickListener {
            binding.imageCarousel.currentItem = (binding.imageCarousel.currentItem + 1) % imageUris.size
        }

        binding.prevButton.setOnClickListener {
            binding.imageCarousel.currentItem = (binding.imageCarousel.currentItem - 1 + imageUris.size) % imageUris.size
        }
        
        updateCarouselVisibility()

        // Adapter for Category Spinner
        val categoryAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.categories_array)
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(resources.getColor(android.R.color.darker_gray))
                }
                return view
            }
        }
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = categoryAdapter

        // Adapter for Tags Spinner
        val tagsAdapter = object : ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            resources.getStringArray(R.array.tags_array)
        ) {
            override fun isEnabled(position: Int): Boolean {
                return position != 0
            }

            override fun getDropDownView(
                position: Int, convertView: View?, parent: ViewGroup
            ): View {
                val view = super.getDropDownView(position, convertView, parent) as TextView
                if (position == 0) {
                    view.setTextColor(resources.getColor(android.R.color.darker_gray))
                }
                return view
            }
        }
        tagsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.tagsSpinner.adapter = tagsAdapter
    }

    private fun updateCarouselVisibility() {
        if (imageUris.isEmpty()) {
            binding.uploadImageButton.visibility = View.VISIBLE
            binding.carouselContainer.visibility = View.GONE
            binding.carouselIndicator.visibility = View.GONE
        } else {
            binding.uploadImageButton.visibility = View.GONE
            binding.carouselContainer.visibility = View.VISIBLE
            binding.carouselIndicator.visibility = View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}