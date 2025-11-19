package com.ciit.tadaimaotakustore.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ciit.tadaimaotakustore.ui.profile.ProfilePageViewModel
import com.ciit.tadaimaotakustore.R

class ProfilePage : Fragment() {

    companion object {
        fun newInstance() = ProfilePage()
    }

    private val viewModel: ProfilePageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Use the ViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_profile_page, container, false)
    }
}