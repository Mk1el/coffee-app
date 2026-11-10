package com.example.coffee_work.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.coffee_work.Domain.ItemsModel
import com.example.coffee_work.Helper.ChangeNumberItemsListener
import com.example.coffee_work.Helper.ManagementCart
import com.example.coffee_work.R

class CartAdapter(
    private val listItemSelected: ArrayList<ItemsModel>,
    context: Context,
    private val changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    private val managementCart = ManagementCart(context)

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTxt: TextView = itemView.findViewById(R.id.titleTxt)
        val feeEachItem: TextView = itemView.findViewById(R.id.feeEachItem)
        val totalEachItem: TextView = itemView.findViewById(R.id.totalEachItem)
        val numberItemTxt: TextView = itemView.findViewById(R.id.numberItemTxt)
        val picCart: ImageView = itemView.findViewById(R.id.picCart)
        val plusEachItem: TextView = itemView.findViewById(R.id.plusEachItem)
        val minusEachItem: TextView = itemView.findViewById(R.id.minusEachItem)
        val removeItemBtn: ImageView = itemView.findViewById(R.id.removeItemBtn)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.viewholder_cart, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]

        holder.titleTxt.text = item.title
        holder.feeEachItem.text = "$${item.price}"

        val totalItemPrice =
            Math.round(item.numberInCart * item.price * 100.0) / 100.0

        holder.totalEachItem.text = "$$totalItemPrice"
        holder.numberItemTxt.text = item.numberInCart.toString()

        Glide.with(holder.itemView.context)
            .load(item.picUrl.firstOrNull())
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.picCart)

        holder.plusEachItem.setOnClickListener {
            val index = holder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                managementCart.plusItem(
                    listItemSelected,
                    index,
                    createListener()
                )
            }
        }

        holder.minusEachItem.setOnClickListener {
            val index = holder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                managementCart.minusItem(
                    listItemSelected,
                    index,
                    createListener()
                )
            }
        }

        holder.removeItemBtn.setOnClickListener {
            val index = holder.adapterPosition
            if (index != RecyclerView.NO_POSITION) {
                managementCart.romveItem(
                    listItemSelected,
                    index,
                    createListener()
                )
            }
        }
    }

    private fun createListener(): ChangeNumberItemsListener {
        return object : ChangeNumberItemsListener {
            override fun onChanged() {
                notifyDataSetChanged()
                changeNumberItemsListener?.onChanged()
            }
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}