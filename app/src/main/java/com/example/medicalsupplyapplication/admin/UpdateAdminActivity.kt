package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityUpdateAdminBinding

class UpdateAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUpdateAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val getAdminPosition = intent.getIntExtra("getPosition", 0)

        binding.showAdminID.text = Database.admins[getAdminPosition].getID()
        binding.editAdminName.setText(Database.admins[getAdminPosition].getName())
        binding.editAdminEmail.setText(Database.admins[getAdminPosition].getEmail())
        binding.editAdminPhone.setText(Database.admins[getAdminPosition].getPhone())
        binding.editAdminPass.setText(Database.admins[getAdminPosition].getPassword())
        binding.editAdminConfPass.setText(Database.admins[getAdminPosition].getConfirmPass())

        binding.showpass.setOnClickListener {
            if(binding.showpass.contentDescription.toString().equals("show")){
                binding.editAdminPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else{
                binding.editAdminPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.showConPass.setOnClickListener {
            if(binding.showConPass.contentDescription.toString().equals("show")){
                binding.editAdminConfPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showConPass.contentDescription = "Hide"
            } else{
                binding.editAdminConfPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showConPass.contentDescription = "show"
            }
        }

        binding.editAdminBtn.setOnClickListener {
            val adminID = binding.showAdminID.text.toString()
            val adminName = binding.editAdminName.text.toString()
            val adminEmail = binding.editAdminEmail.text.toString()
            val adminPhone = binding.editAdminPhone.text.toString()
            val adminPass = binding.editAdminPass.text.toString()
            val adminConfPass = binding.editAdminConfPass.text.toString()

            if (adminName == "") {
                Toast.makeText(this, "Please Enter Admin Name.", Toast.LENGTH_SHORT).show()
                binding.editAdminName.requestFocus()
            } else if (adminEmail == "" || !Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches()) {
                Toast.makeText(this, "Please Enter Admin Email.", Toast.LENGTH_SHORT).show()
                binding.editAdminEmail.requestFocus()
            } else if (adminPhone == "") {
                Toast.makeText(this, "Please Enter Admin Phone Number.", Toast.LENGTH_SHORT).show()
                binding.editAdminPhone.requestFocus()
            } else if (adminPass == "" || adminPass.length < 8) {
                Toast.makeText(this, "Please Enter Password.", Toast.LENGTH_SHORT).show()
                binding.editAdminPass.requestFocus()
            } else if (adminConfPass == "") {
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show()
                binding.editAdminConfPass.requestFocus()
            } else if (adminConfPass != adminPass) {
                Toast.makeText(
                    this,
                    "Please enter confirm password same as password.",
                    Toast.LENGTH_SHORT
                ).show()
                binding.editAdminConfPass.requestFocus()
            } else {
                Database.updateAdmin(
                    adminID,
                    adminName,
                    adminEmail,
                    adminPhone,
                    adminPass,
                    adminConfPass,
                    getAdminPosition
                )
                Toast.makeText(this, "Edit Admin Successful!", Toast.LENGTH_LONG).show()
                val intent = Intent(this, AdminDetailsActivity::class.java)
                intent.putExtra("getIndex", getAdminPosition)
                startActivity(intent)
            }
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, AdminDetailsActivity::class.java)
            intent.putExtra("getIndex", getAdminPosition)
            startActivity(intent)
        }
    }
}