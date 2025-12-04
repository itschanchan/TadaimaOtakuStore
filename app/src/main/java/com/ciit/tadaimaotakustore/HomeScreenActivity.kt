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
import com.ciit.tadaimaotakustore.ui.cart.CartViewModel
import com.ciit.tadaimaotakustore.databinding.ActivityHomeScreenBinding

class HomeScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeScreenBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val wishlistViewModel: WishlistViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private var wishlistBadge: TextView? = null
    private var cartBadge: TextView? = null
    private var lastBackPressTime: Long = 0

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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.nav_item_view || destination.id == R.id.nav_cart) {
                binding.appBarHomeScreen.bottomNavView.visibility = View.GONE
            } else {
                binding.appBarHomeScreen.bottomNavView.visibility = View.VISIBLE
            }
        }

        wishlistViewModel.wishlistItemCount.observe(this) { count ->
            updateWishlistBadge(count)
        }

        cartViewModel.totalCartItemQuantity.observe(this) { count ->
            updateCartBadge(count)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.home_screen_menu, menu)
        val wishlistItem = menu.findItem(R.id.action_wishlist)
        val cartItem = menu.findItem(R.id.nav_cart)
        val wishlistActionView = wishlistItem.actionView
        val cartActionView = cartItem.actionView
        wishlistBadge = wishlistActionView?.findViewById(R.id.wishlist_badge)
        cartBadge = cartActionView?.findViewById(R.id.cart_badge)

        wishlistActionView?.setOnClickListener {
            onOptionsItemSelected(wishlistItem)
        }

        cartActionView?.setOnClickListener {
            onOptionsItemSelected(cartItem)
        }

        updateWishlistBadge(wishlistViewModel.wishlistItemCount.value ?: 0)
        updateCartBadge(cartViewModel.totalCartItemQuantity.value ?: 0)
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

    private fun updateCartBadge(count: Int) {
        if (count > 0) {
            cartBadge?.visibility = View.VISIBLE
            cartBadge?.text = count.toString()
        } else {
            cartBadge?.visibility = View.GONE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_wishlist) {
            if (navController.currentDestination?.id != R.id.nav_wishlist) {
                navController.navigate(R.id.nav_wishlist)
            }
            return true
        }
        if (item.itemId == R.id.nav_cart) {
            if (navController.currentDestination?.id != R.id.nav_cart) {
                navController.navigate(R.id.nav_cart)
            }
            return true
        }
        return item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home_screen)
        val currentTime = System.currentTimeMillis()
        if (navController.currentDestination?.id == R.id.nav_cart) {
            if (currentTime - lastBackPressTime < 1000) { // 1 second debounce
                return true // consume event
            }
            lastBackPressTime = currentTime
        }
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        val currentTime = System.currentTimeMillis()
        if (navController.currentDestination?.id == R.id.nav_cart) {
            if (currentTime - lastBackPressTime < 1000) { // 1 second debounce
                return // consume event
            }
            lastBackPressTime = currentTime
        }

        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}