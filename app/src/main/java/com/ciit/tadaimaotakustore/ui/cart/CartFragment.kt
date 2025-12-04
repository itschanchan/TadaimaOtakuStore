package com.ciit.tadaimaotakustore.ui.cart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ciit.tadaimaotakustore.databinding.FragmentCartBinding

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private val cartViewModel: CartViewModel by activityViewModels()
    private lateinit var cartAdapter: CartAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        cartAdapter = CartAdapter(cartViewModel)
        binding.rvCartItems.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = cartAdapter
        }

        cartViewModel.cartItems.observe(viewLifecycleOwner) { items ->
            cartAdapter.setItems(items)
            updateTotalPrice(items)
            binding.cbCheckAll.isChecked = items.all { it.isChecked }
        }

        binding.cbCheckAll.setOnCheckedChangeListener { _, isChecked ->
            cartViewModel.toggleAllItems(isChecked)
        }
    }

    private fun updateTotalPrice(items: List<CartItem>) {
        val totalPrice = items.filter { it.isChecked }.sumOf { it.price.toDouble() * it.quantity }
        binding.tvTotalPrice.text = String.format("Total: ₱%.2f", totalPrice)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}