package com.example.medicalsupplyapplication.customer


import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.medicalsupplyapplication.databinding.ActivityProfileBinding
import com.example.medicalsupplyapplication.roomDatabase.Synchronization
import com.example.medicalsupplyapplication.viewModel.CustomerViewModel
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var custViewModel: CustomerViewModel
    private val coroutineScope: CoroutineScope = lifecycleScope
    private val synchronization = Synchronization()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val custID = intent.getStringExtra("getID")

        custViewModel = ViewModelProvider(this).get(CustomerViewModel::class.java)

        if(custViewModel.customerList.value == null) {
            custViewModel.initOnlineCustomer(custID!!)
        }

        custViewModel.customer.observe(this, Observer { newCust ->
            binding.showCustName.text = newCust.username
            binding.showCustEmail.text = newCust.email
            binding.showCustPhone.text = newCust.phone
            binding.showAddress.text = newCust.address
        })


        val storageRef = FirebaseStorage.getInstance().reference.child("Customer/$custID.jpg")
        val localFile = File.createTempFile("tempImage", "jpeg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            binding.profileImage.setImageBitmap(bitmap)
        }

        binding.upProfile.setTextColor(Color.BLUE)

        binding.updateCustBtn.setOnClickListener {
            val intent = Intent(this,UpdateProfileActivity::class.java)
            intent.putExtra("getID",custID)
            startActivity(intent)
        }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            intent.putExtra("getID",custID)
            startActivity(intent)
        }
    }
}