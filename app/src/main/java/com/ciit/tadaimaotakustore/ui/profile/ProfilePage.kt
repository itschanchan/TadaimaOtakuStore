package com.ciit.tadaimaotakustore.ui.profile

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ciit.tadaimaotakustore.R
import com.ciit.tadaimaotakustore.data.UserDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class ProfilePage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_profile_page, container, false)
        val userNameTextView = view.findViewById<TextView>(R.id.user_name)
        val accountAgeTextView = view.findViewById<TextView>(R.id.account_age)

        val safeContext = context ?: return view

        val sharedPref = PreferenceManager.getDefaultSharedPreferences(safeContext)
        val username = sharedPref.getString("USERNAME", null)

        if (username != null) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val userDao = UserDatabase.getDatabase(safeContext).userDao()
                val user = userDao.getUser(username)
                if (user != null) {
                    val accountAgeInMillis = System.currentTimeMillis() - user.createdAt
                    val years = TimeUnit.MILLISECONDS.toDays(accountAgeInMillis) / 365
                    withContext(Dispatchers.Main) {
                        userNameTextView.text = getString(R.string.user_name_format, user.firstName, user.lastName)
                        accountAgeTextView.text = "$years years"
                    }
                } else {
                    // TODO: Handle user not found
                }
            }
        }

        return view
    }
}