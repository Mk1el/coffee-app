package com.example.coffee_work.Activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.coffee_work.Domain.ItemsModel
import com.example.coffee_work.Helper.ManagementCart
import com.example.coffee_work.R
import com.example.coffee_work.databinding.ActivityDetailBinding



class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemsModel
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)
        bundle()
        initSizeList()
    }

    private fun initSizeList() {
        binding.apply {
            smallBtn.setOnClickListener {
                smallBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(0)
            }
            mediumBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
                largeBtn.setBackgroundResource(0)
            }
            largeBtn.setOnClickListener {
                smallBtn.setBackgroundResource(0)
                mediumBtn.setBackgroundResource(0)
                largeBtn.setBackgroundResource(R.drawable.stroke_brown_bg)
            }
        }
    }

    private fun bundle() {
        binding.apply {
            item = intent.getSerializableExtra("object") as ItemsModel

            // Load main coffee picture
            Glide.with(this@DetailActivity)
                .load(item.picUrl[0])
                .into(picMain)

            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "$" + item.price
            ratingTxt.text = item.rating.toString()

            if (item.numberInCart < 1) item.numberInCart = 1
            numberItemTxt.text = item.numberInCart.toString()

            addToCartBtn.setOnClickListener {
                item.numberInCart = Integer.valueOf(numberItemTxt.text.toString())
                managementCart.insertItems(item)
                startActivity(Intent(this@DetailActivity, CartActivity::class.java))
            }

            backBtn.setOnClickListener {
                finish()
            }

            plusCart.setOnClickListener {
                item.numberInCart++
                numberItemTxt.text = item.numberInCart.toString()
            }

            minusBtn.setOnClickListener {
                if (item.numberInCart > 1) {
                    item.numberInCart--
                    numberItemTxt.text = item.numberInCart.toString()
                }
            }
        }
    }
}