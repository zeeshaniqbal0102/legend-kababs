//package com.app.legendkebabs.data.model
//
//import java.io.Serializable
//
//data class OrderListItemModel(
//    val current_page: Int,
//    val `data`: List<Data>,
//    val first_page_url: String,
//    val from: Int,
//    val last_page: Int,
//    val last_page_url: String,
//    val next_page_url: String,
//    val path: String,
//    val per_page: Int,
//    val prev_page_url: Any,
//    val to: Int,
//    val total: Int
//) {
//    data class Data(
//        val Id: String,
//        val addon_id: String,
//        val addon_name: String,
//        val addon_price: String,
//        val addon_quantity: String,
//        val address: String,
//        val address_2: String,
//        val cart: String,
//        val category_id: String,
//        val child_category_id: String,
//        val country: String,
//        val created_at: String,
//        val email: String,
//        val full_name: String,
//        val id: Int,
//        val order_id: String,
//        val order_selected: String,
//        val order_status: String,
//        val price: String,
//        val product_id: String,
//        val product_name: String,
//        val product_price: String,
//        val product_quantity: String,
//        val shipping_same: String,
//        val state: String,
//        val status: String,
//        val sub_category_id: String,
//        val subtotal: String,
//        val total_amount: String,
//        val total_price: String,
//        val total_quantity: String,
//        val updated_at: String,
//        val user_id: String,
//        val zip_code: String
//    ) : Serializable
//}