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
import com.ciit.tadaimaotakustore.databinding.ActivityRegistrationBinding
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

                showSuccessToast()

                showDialogBox(getFirstName, getLastName, getEmail, getUsername, getPassword, getGender, selectedDate)
            }
        }
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

    // Display User Info - UPDATED to include birthdate
    private fun showDialogBox(getFirstName: String, getLastName: String, getEmail: String, getUsername: String, getPassword: String, getGender: String, birthDate: String) {
        // Customize theme for alert box
        val titleView = TextView(this).apply {
            text = "Registration Successful!"
            textSize = 20f
            setTypeface(Typeface.create("serif", Typeface.BOLD))
            setTextColor(0xFF6A0DAD.toInt()) // Purple Text
            gravity = Gravity.CENTER
            setPadding(0, 40, 0, 20)
        }

        // Alert Box - Updated to include birthdate
        val builder = AlertDialog.Builder(this)
            .setCustomTitle(titleView)
            .setMessage("""
                First Name: $getFirstName
                Last Name: $getLastName
                Email: $getEmail
                Username: $getUsername
                Password: $getPassword
                Gender: $getGender
                Birth Date: $birthDate
                Terms & Conditions: Accepted
            """.trimIndent())
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email", getEmail)
                intent.putExtra("pass", getPassword)
                startActivity(intent)
            }

        val alertDialog = builder.create()

        // Set simple background color instead of missing drawable
        alertDialog.window?.setBackgroundDrawableResource(android.R.color.white)

        alertDialog.show()
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
