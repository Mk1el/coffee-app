package com.example.coffee_work.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffee_work.Activity.DetailActivity
import com.example.coffee_work.Domain.ItemsModel
import com.example.coffee_work.R

class ItemsListCategoryAdapter(val items: MutableList<ItemsModel>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_ITEM1 = 0
        const val TYPE_ITEM2 = 1
    }

    private lateinit var context: Context

    override fun getItemViewType(position: Int): Int {
        return if (position % 2 == 0) TYPE_ITEM1 else TYPE_ITEM2
    }

    class ViewholderItem1(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleTxt)
        val priceTxt: TextView = itemView.findViewById(R.id.priceTxt)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val picMain: ImageView = itemView.findViewById(R.id.picMain)
    }

    class ViewholderItem2(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleTxt)
        val priceTxt: TextView = itemView.findViewById(R.id.priceTxt)
        val ratingBar: RatingBar = itemView.findViewById(R.id.ratingBar)
        val picMain: ImageView = itemView.findViewById(R.id.picMain)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return when (viewType) {
            TYPE_ITEM1 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.viewholder_item_pic_right, parent, false)
                ViewholderItem1(view)
            }
            TYPE_ITEM2 -> {
                val view = LayoutInflater.from(context)
                    .inflate(R.layout.viewholder_item_pic_left, parent, false)
                ViewholderItem2(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]

        when (holder) {
            is ViewholderItem1 -> {
                holder.titleTxt.text = item.title
                holder.priceTxt.text = "$${item.price}"
                holder.ratingBar.rating = item.rating.toFloat()

                Glide.with(context)
                    .load(item.picUrl[0])
                    .into(holder.picMain)

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("object", item)
                    context.startActivity(intent)
                }
            }
            is ViewholderItem2 -> {
                holder.titleTxt.text = item.title
                holder.priceTxt.text = "$${item.price}"
                holder.ratingBar.rating = item.rating.toFloat()

                Glide.with(context)
                    .load(item.picUrl[0])
                    .into(holder.picMain)

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra("object", item)
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size
}