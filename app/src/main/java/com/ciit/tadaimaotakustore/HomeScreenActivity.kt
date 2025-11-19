package com.ciit.tadaimaotakustore

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.ciit.tadaimaotakustore.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private var wishlistBadge: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarHomeScreen.toolbar)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_content_home_screen) as NavHostFragment
        navController = navHostFragment.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.appBarHomeScreen.bottomNavView.setupWithNavController(navController)

        wishlistViewModel.wishlistItemCount.observe(this) { count ->
            updateWishlistBadge(count)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        val wishlistItem = menu.findItem(R.id.action_wishlist)
        val actionView = wishlistItem.actionView
        wishlistBadge = actionView?.findViewById(R.id.wishlist_badge)

        actionView?.setOnClickListener {
            onOptionsItemSelected(wishlistItem)
        }

        updateWishlistBadge(wishlistViewModel.wishlistItemCount.value ?: 0)
        return true
    }

    private fun updateWishlistBadge(count: Int) {
        if (count > 0) {
            wishlistBadge?.visibility = View.VISIBLE
            wishlistBadge?.text = count.toString()
        } else {
            wishlistBadge?.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_wishlist) {
            navController.navigate(R.id.nav_wishlist)
            return true
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}