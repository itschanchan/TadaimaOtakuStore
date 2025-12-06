package com.ciit.tadaimaotakustore

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ciit.tadaimaotakustore.data.UserDatabase
import com.ciit.tadaimaotakustore.databinding.ActivityMainBinding
import kotlinx.coroutines.launch
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.loginButton.setOnClickListener {
            handleLogin()
        }

        binding.signUpButton.setOnClickListener {
            val intent = Intent(this@MainActivity, Registration::class.java)
            startActivity(intent)
        }

        binding.showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Move cursor to end for better UX
            binding.editPassword.setSelection(binding.editPassword.text?.length ?: 0)
        }
    }

    private fun handleLogin() {
        val email = binding.editEmail.text.toString().trim()
        val password = binding.editPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            val userDao = UserDatabase.getDatabase(applicationContext).userDao()
            val user = userDao.getUserByEmail(email)

            if (user != null && user.passwordHash == hashPassword(password)) {
                // Save username to SharedPreferences
                val sharedPref = PreferenceManager.getDefaultSharedPreferences(this@MainActivity)
                with(sharedPref.edit()) {
                    putString("USERNAME", user.username)
                    apply()
                }

                val intent = Intent(this@MainActivity, HomeScreenActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@MainActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray(Charsets.UTF_8))
        return bytesToHex(hashedBytes)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789abcdef"
        val result = StringBuilder(bytes.size * 2)
        bytes.forEach { byte ->
            val value = byte.toInt()
            val hex1 = hexChars[value ushr 4 and 0x0F]
            val hex2 = hexChars[value and 0x0F]
            result.append(hex1)
            result.append(hex2)
        }
        return result.toString()
    }
}
