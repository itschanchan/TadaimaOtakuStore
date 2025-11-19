package com.ciit.tadaimaotakustore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ciit.tadaimaotakustore.databinding.FragmentFullScreenImageBinding

class FullScreenImageFragment : Fragment() {

    private var _binding: FragmentFullScreenImageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullScreenImageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageResId = arguments?.getInt("imageResId") ?: R.drawable.carousel_image_1
        binding.photoView.setImageResource(imageResId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}