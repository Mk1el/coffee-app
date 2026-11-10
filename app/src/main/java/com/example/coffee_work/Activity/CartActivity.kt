
package com.example.coffee_work.Activity

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_work.Adapter.CartAdapter
import com.example.coffee_work.Helper.ChangeNumberItemsListener
import com.example.coffee_work.Helper.ManagementCart
import com.example.coffee_work.R


class CartActivity : AppCompatActivity() {

    private lateinit var managementCart: ManagementCart
    private var tax: Double = 0.0

    private lateinit var totalFeeTxt: TextView
    private lateinit var taxTxt: TextView
    private lateinit var deliveryTxt: TextView
    private lateinit var totalTxt: TextView
    private lateinit var listView: RecyclerView
    private lateinit var backBtn: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)

        totalFeeTxt = findViewById(R.id.totalFeeTxt)
        taxTxt = findViewById(R.id.taxTxt)
        deliveryTxt = findViewById(R.id.deliveryTxt)
        totalTxt = findViewById(R.id.totalTxt)
        listView = findViewById(R.id.listView)
        backBtn = findViewById(R.id.backBtn)

        managementCart = ManagementCart(this)

        backBtn.setOnClickListener {
            finish()
        }

        initCartList()
        calculateCart()
    }

    private fun initCartList() {
        listView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )

        listView.adapter = CartAdapter(
            managementCart.getListCart(),
            this,
            object : ChangeNumberItemsListener {
                override fun onChanged() {
                    calculateCart()
                }
            }
        )
    }

    private fun calculateCart() {
        val percentTax = 0.02
        val delivery = 15.0

        val itemTotal = managementCart.getTotalFee()

        tax = Math.round(itemTotal * percentTax * 100.0) / 100.0

        val total = Math.round(
            (itemTotal + tax + delivery) * 100.0
        ) / 100.0

        totalFeeTxt.text = "$${
            Math.round(itemTotal * 100.0) / 100.0
        }"

        taxTxt.text = "$$tax"
        deliveryTxt.text = "$$delivery"
        totalTxt.text = "$$total"
    }
}
