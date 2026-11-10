package com.example.coffee_work.Adapter

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.coffee_work.Activity.ItemsListActivity
import com.example.coffee_work.Domain.CategoryModel
import com.example.coffee_work.R
import com.example.coffee_work.databinding.ViewholderCategoryBinding

class CategoryAdapter(
    val items: MutableList<CategoryModel>
) : RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var selectedPosition = -1
    private var lastSelectedPosition = -1

    class ViewHolder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewholderCategoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val context = holder.itemView.context

        holder.binding.titleCat.text = item.title

        holder.binding.root.setOnClickListener {
            val currentPosition = holder.adapterPosition
            if (currentPosition != RecyclerView.NO_POSITION) {
                lastSelectedPosition = selectedPosition
                selectedPosition = currentPosition
                notifyItemChanged(lastSelectedPosition)
                notifyItemChanged(selectedPosition)

                val clickedItem = items[currentPosition] // Use currentPosition here

                Handler(Looper.getMainLooper()).postDelayed({
                    val intent = Intent(context, ItemsListActivity::class.java).apply {
                        putExtra("id", clickedItem.id.toString())
                        putExtra("title", clickedItem.title)
                    }
                    ContextCompat.startActivity(context, intent, null)
                }, 500)
            }
        }

        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.white))
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.white_full_corner_bg)
            holder.binding.titleCat.setTextColor(ContextCompat.getColor(context, R.color.darkBrown))
        }
    }

    override fun getItemCount(): Int = items.size
}