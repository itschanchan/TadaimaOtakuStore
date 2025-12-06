package com.ciit.tadaimaotakustore

import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ciit.tadaimaotakustore.data.User
import com.ciit.tadaimaotakustore.data.UserDatabase
import com.ciit.tadaimaotakustore.databinding.ActivityRegistrationBinding
import kotlinx.coroutines.launch
import java.security.MessageDigest
import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val calendar = Calendar.getInstance()
    private var isDateSelected = false // Track if date has been selected

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.datePicker.setOnClickListener { showDatePicker() }

        binding.showPassCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.editPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.editRePassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
            } else {
                binding.editPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.editRePassword.transformationMethod = PasswordTransformationMethod.getInstance()
            }
            // Move cursor to end for better UX
            binding.editPassword.setSelection(binding.editPassword.text?.length ?: 0)
            binding.editRePassword.setSelection(binding.editRePassword.text?.length ?: 0)
        }

        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.maleRadio -> showGenderToast("Male")
                R.id.femaleRadio -> showGenderToast("Female")
                R.id.othersRadio -> showGenderToast("Others")
            }
        }

        binding.regisButton.setOnClickListener { validateAndRegister() }
    }

    private fun validateAndRegister() {
        val getFirstName = binding.editFirstName.text.toString().trim()
        val getLastName = binding.editLastName.text.toString().trim()
        val getEmail = binding.editEmail.text.toString().trim()
        val getUsername = binding.editUsername.text.toString().trim()
        val getPassword = binding.editPassword.text.toString()
        val getRePassword = binding.editRePassword.text.toString()
        val isTcChecked = binding.tcCheckbox.isChecked

        val selectedRadioButtonId = binding.genderRadioGroup.checkedRadioButtonId

        when {
            getFirstName.isEmpty() || getLastName.isEmpty() -> {
                showErrorToast("Please enter your full name!")
            }
            getEmail.isEmpty() -> {
                showErrorToast("Please enter your email!")
            }
            getUsername.isEmpty() -> {
                showErrorToast("Please choose a username!")
            }
            getPassword.isEmpty() -> {
                showErrorToast("Please create a password!")
            }
            getRePassword.isEmpty() -> {
                showErrorToast("Please confirm your password!")
            }
            selectedRadioButtonId == -1 -> {
                showErrorToast("Please select your gender!")
            }
            !isTcChecked -> {
                showErrorToast("Please accept the Terms & Conditions!")
            }
            getPassword != getRePassword -> {
                showErrorToast("Passwords do not match!")
            }
            getPassword.length < 6 -> {
                showErrorToast("Password must be at least 6 characters!")
            }
            !isDateSelected -> { // NEW VALIDATION: Check if date is selected
                showErrorToast("Please select your date of birth!")
            }
            else -> {
                // Safe gender retrieval
                val getGender = if (selectedRadioButtonId != -1) {
                    binding.genderRadioGroup.findViewById<RadioButton>(selectedRadioButtonId)?.text?.toString() ?: "Not specified"
                } else {
                    "Not specified"
                }

                // Get the selected date
                val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                val selectedDate = dateFormat.format(calendar.time)

                val passwordHash = hashPassword(getPassword)
                val user = User(
                    firstName = getFirstName,
                    lastName = getLastName,
                    birthDate = selectedDate,
                    gender = getGender,
                    username = getUsername,
                    email = getEmail,
                    passwordHash = passwordHash
                )
                lifecycleScope.launch {
                    val userDao = UserDatabase.getDatabase(applicationContext).userDao()
                    userDao.addUser(user)
                    showSuccessToast()
                    val intent = Intent(this@Registration, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
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

    private fun showSuccessToast() {
        val toast = Toast.makeText(this, "Registration successful! ⸜(｡˃ ᵕ ˂ )⸝♡", Toast.LENGTH_LONG)

        val view = toast.view
        view?.setBackgroundColor(0xFFE6E6FA.toInt()) // Light lavender purple

        val text = view?.findViewById<TextView>(android.R.id.message)
        text?.setTextColor(0xFF4B0082.toInt()) // Indigo text color
        text?.textSize = 16f
        text?.setTypeface(Typeface.create("serif", Typeface.BOLD)) // Georgia-like bold font
        text?.gravity = Gravity.CENTER

        toast.show()
    }

    private fun showGenderToast(gender: String) {
        val toast = Toast.makeText(this, "$gender selected", Toast.LENGTH_SHORT)

        val view = toast.view
        view?.setBackgroundColor(0xFFF0E6FA.toInt()) // Very light purple

        val text = view?.findViewById<TextView>(android.R.id.message)
        text?.setTextColor(0xFF6A0DAD.toInt()) // Purple text
        text?.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))

        toast.show()
    }

    private fun showErrorToast(message: String) {
        val toast = Toast.makeText(this, message, Toast.LENGTH_SHORT)

        val view = toast.view
        view?.setBackgroundColor(0xFFFFE6E6.toInt()) // Light red/pink

        val text = view?.findViewById<TextView>(android.R.id.message)
        text?.setTextColor(0xFFD32F2F.toInt()) // Dark red text
        text?.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL))

        toast.show()
    }

    private fun showDatePicker() {
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year: Int, monthOfYear: Int, dayOfMonth: Int ->
            calendar.set(year, monthOfYear, dayOfMonth)
            updateDateButton()
            isDateSelected = true // Mark date as selected
        }

        DatePickerDialog(
            this,
            dateSetListener,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).apply {
            // Set maximum date to today
            datePicker.maxDate = System.currentTimeMillis()
            show()
        }
    }

    private fun updateDateButton() {
        val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        binding.datePicker.text = formattedDate

        // Update the label to show selected date
        binding.tvBirthdate.text = "Birthdate: $formattedDate"
    }
}
