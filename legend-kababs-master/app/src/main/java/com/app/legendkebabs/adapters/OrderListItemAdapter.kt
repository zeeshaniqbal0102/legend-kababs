package com.app.legendkebabs.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.app.legendkebabs.R
import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.databinding.ItemOrdersListBinding
import javax.inject.Inject

class OrderListItemAdapter @Inject constructor() : RecyclerView.Adapter<OrderListItemAdapter.ItemViewHolder>() {
    private val orderList: ArrayList<OrderListItemModel.Data> = ArrayList()
    private var listener: OnOrderClickListener? = null

    fun setItemList(orderList: List<OrderListItemModel.Data>) {
//        this.orderList.clear()
        this.orderList.addAll(orderList)
        notifyDataSetChanged()
    }

    fun clearList() {
        this.orderList.clear()
        notifyDataSetChanged()
    }

    fun setOrderClickListener(listener: OnOrderClickListener){
        this.listener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(position, orderList[position])
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    inner class ItemViewHolder(
        private val parent: ViewGroup,
        private val binding: ItemOrdersListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_orders_list,
            parent,
            false
        )
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int,
            model: OrderListItemModel.Data
        ) {
            binding.model = model
            binding.sNo = (position + 1).toString()
            binding.btnView.setOnClickListener {
                listener!!.onOrderClicked((position + 1).toString(), model)
            }
        }
    }

    interface OnOrderClickListener{
        fun onOrderClicked(sNo:String, model: OrderListItemModel.Data)
    }
}