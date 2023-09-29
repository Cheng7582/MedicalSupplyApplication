package com.example.medicalsupplyapplication.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.widget.Toast
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.databinding.ActivityResetPasswordBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val reset = Firebase.auth.currentUser
        val custID = intent.getStringExtra("getID")

        var boolean: Boolean = false

        binding.showCurrentPass.setOnClickListener {
            if(binding.showCurrentPass.contentDescription.toString().equals("show")){
                binding.editCurrentPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showCurrentPass.contentDescription = "Hide"
            } else{
                binding.editCurrentPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showCurrentPass.contentDescription = "show"
            }
        }

        binding.showNewPass.setOnClickListener {
            if(binding.showNewPass.contentDescription.toString().equals("show")){
                binding.enterNewPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showNewPass.contentDescription = "Hide"
            } else{
                binding.enterNewPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showNewPass.contentDescription = "show"
            }
        }

        binding.showConfPass.setOnClickListener {
            if(binding.showConfPass.contentDescription.toString().equals("show")){
                binding.enterConfPass.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.showConfPass.contentDescription = "Hide"
            } else{
                binding.enterConfPass.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.showConfPass.contentDescription = "show"
            }
        }

        binding.confirmBtn.setOnClickListener{
            val currentPass = binding.editCurrentPassword.text.toString()
            val newPass = binding.enterNewPass.text.toString()
            val newConfPass = binding.enterConfPass.text.toString()

            if(currentPass == ""){
                Toast.makeText(this, "Please Enter Current Password.", Toast.LENGTH_SHORT).show()
                binding.editCurrentPassword.requestFocus()
            }else if(newPass == ""){
                Toast.makeText(this, "Please Enter New Password.", Toast.LENGTH_SHORT).show()
                binding.enterNewPass.requestFocus()
            }else if(newConfPass == ""){
                Toast.makeText(this, "Please Enter Confirm Password.", Toast.LENGTH_SHORT).show()
                binding.enterConfPass.requestFocus()
            }else if(newPass != newConfPass){
                Toast.makeText(this, "New Password and Confirm Password must be same.", Toast.LENGTH_SHORT).show()
                binding.enterConfPass.requestFocus()
            }else{
                for(it in Database.customers){
                    if(custID == it.getID() && currentPass == it.getPassword()){
                        reset!!.updatePassword(newPass).addOnCompleteListener{ task ->
                            if(task.isSuccessful){
                                Database.db.collection("Customer").document(custID)
                                    .update("Password",newPass,
                                        "ConfirmPass",newConfPass)
                                Toast.makeText(this,"Updated Successful!", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, SettingActivity::class.java)
                                intent.putExtra("getID",custID)
                                startActivity(intent)
                                boolean = true
                            }
                        }
                    }
                }
                if(boolean){
                    Toast.makeText(this, "Please Enter Correct Current Password.", Toast.LENGTH_SHORT).show()
                    binding.editCurrentPassword.requestFocus()
                }
            }
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this, SettingActivity::class.java)
            intent.putExtra("getID",custID)
            startActivity(intent)
        }
    }
}