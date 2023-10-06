package com.example.medicalsupplyapplication.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Patterns
import android.widget.Toast
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityAddAdminBinding

class AddAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAdminBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adminCount: Int = Database.admins.size
        val newID: Int = adminCount + 1
        val adminID = "AD" + String.format("%04d",newID)
        binding.addAdminID.text = adminID

        binding.showpass.setOnClickListener {
            if(binding.showpass.contentDescription.toString().equals("show")){
                binding.addAdminPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else{
                binding.addAdminPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.showConPass.setOnClickListener {
            if(binding.showpass.contentDescription.toString().equals("show")){
                binding.addAdminConfPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showpass.contentDescription = "Hide"
            } else{
                binding.addAdminConfPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showpass.contentDescription = "show"
            }
        }

        binding.backToSArrow.setOnClickListener {
            val getID = intent.getStringExtra("getID")
            val intent = Intent(this,AdminListActivity::class.java)
            intent.putExtra("getID",getID)
            startActivity(intent)
        }

        binding.addAdminBtn.setOnClickListener {
            val adminName = binding.addAdminName.text.toString()
            val adminEmail = binding.addAdminEmail.text.toString()
            val adminPhone = binding.addAdminPhone.text.toString()
            val adminPass = binding.addAdminPass.text.toString()
            val adminConfPass = binding.addAdminConfPass.text.toString()
            val phoneRegex = Regex("^0\\d{2}-\\d{7,8}$")

            if(adminName == "") {
                Toast.makeText(this, "Please Enter Admin Name.", Toast.LENGTH_SHORT).show()
                binding.addAdminName.requestFocus()
            }else if(adminEmail == "" || !Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches()) {
                Toast.makeText(this, "Please Enter Valid Admin Email.", Toast.LENGTH_SHORT).show()
                binding.addAdminEmail.requestFocus()
            }else if(adminPhone == "" || !phoneRegex.matches(adminPhone)) {
                Toast.makeText(this, "Please Enter Valid Admin Phone Number.", Toast.LENGTH_SHORT).show()
                binding.addAdminPhone.requestFocus()
            }else if(adminPass == "" || adminPass.length < 8) {
                Toast.makeText(this, "Please Enter Password with at least 8 character.", Toast.LENGTH_SHORT).show()
                binding.addAdminPass.requestFocus()
            }else if(adminConfPass == "") {
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show()
                binding.addAdminConfPass.requestFocus()
            }else if(adminConfPass != adminPass) {
                Toast.makeText(this, "Please enter confirm password same as password.", Toast.LENGTH_SHORT).show()
                binding.addAdminConfPass.requestFocus()
            }else{
                Database.addAdmin(adminID,adminName,adminEmail,adminPhone,adminPass,adminConfPass)
                Toast.makeText(this,"Add New Admin Successful!", Toast.LENGTH_LONG).show()
                val getID = intent.getStringExtra("getID")
                val intent = Intent(this,AdminListActivity::class.java)
                intent.putExtra("getID",getID)
                startActivity(intent)
            }
        }
    }
}