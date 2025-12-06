package com.ciit.tadaimaotakustore.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.ciit.tadaimaotakustore.R
import com.ciit.tadaimaotakustore.data.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfilePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)
        val userNameTextView = view.findViewById<TextView>(R.id.user_name)

        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return view
        val username = sharedPref.getString("USERNAME", "guest")

        // TODO: Load user profile picture

        CoroutineScope(Dispatchers.IO).launch {
            val userDao = UserDatabase.getDatabase(requireContext()).userDao()
            val user = userDao.getUser(username!!)
            if (user != null) {
                activity?.runOnUiThread {
                    userNameTextView.text = getString(R.string.user_name_format, user.firstName, user.lastName)
                }
            } else {
                // TODO: Handle user not found
            }
        }
        return view
    }
}