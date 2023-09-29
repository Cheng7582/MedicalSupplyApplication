package com.example.medicalsupplyapplication.customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.medicalsupplyapplication.Database
import com.example.medicalsupplyapplication.R
import com.example.medicalsupplyapplication.databinding.ActivityDeliveryStatusBinding

class DeliveryStatusActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDeliveryStatusBinding
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<DeliveryAdapter.ViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeliveryStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginID = intent.getStringExtra("getID")
        val listdelivery: MutableList<listDelivery> = mutableListOf()

        Database.db.collection("Delivery").whereEqualTo("CustID",loginID)
            .get()
            .addOnSuccessListener{
                for(doc in it){
                    val dID = doc.get("DeliveryID").toString()
                    val status = doc.get("Status").toString()

                    var newDeliveryStatus = listDelivery(dID,status)
                    listdelivery.add(newDeliveryStatus)
                }
                layoutManager = LinearLayoutManager(this)

                binding.recycleViewDelivery.layoutManager = layoutManager

                DeliveryAdapter.setFragment(loginID.toString(),this)

                adapter = DeliveryAdapter(this, listdelivery)

                binding.recycleViewDelivery.adapter = adapter
            }

        binding.backToSArrow.setOnClickListener {
            val intent = Intent(this,SettingActivity::class.java)
            intent.putExtra("getID",loginID)
            startActivity(intent)
        }
    }

    fun toDetail(getDID: String){
        val loginID = intent.getStringExtra("getID")
        val intent = Intent(this, ShowDeliveryStatusActivity::class.java)
        intent.putExtra("getDeliveryID",getDID)
        intent.putExtra("getID",loginID)
        startActivity(intent)
    }
}

class DeliveryAdapter(val context: DeliveryStatusActivity, val items: List<listDelivery>): RecyclerView.Adapter<DeliveryAdapter.ViewHolder>() {
    companion object{
        private var getLoginID: String = ""
        private lateinit var activityActi : DeliveryStatusActivity

        fun setFragment(getID: String, activityActi: DeliveryStatusActivity){
            getLoginID = getID
            Companion.activityActi = activityActi
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.delivery_list_layout, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.get(position)

        holder.index.text = (position + 1).toString()
        holder.odrID.text = item.getDID()
        holder.status.text = item.getStatus()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var index: TextView
        var odrID: TextView
        var status: TextView

        init {
            index = itemView.findViewById(R.id.indexPay)
            odrID = itemView.findViewById(R.id.payID)
            status = itemView.findViewById(R.id.totalPrice)

            itemView.setOnClickListener{
                val position: Int = absoluteAdapterPosition
                val item = items.get(position)
                toDetail(activityActi, item.getDID())
            }
        }
    }

    fun toDetail(ActivityAct: DeliveryStatusActivity, getDeliveryID:String){
        ActivityAct.toDetail(getDeliveryID)
    }
}

class listDelivery{
    private var dId:String
    private var status:String

    constructor(){
        dId = ""
        status = ""
    }

    constructor(DId:String,Status:String){
        dId = DId
        status = Status
    }

    fun getDID():String{
        return dId
    }

    fun getStatus():String{
        return status
    }
}