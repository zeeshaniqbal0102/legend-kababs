package com.app.legendkebabs.ui.events

import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.data.model.SendDeviceTokenModel

sealed class OrdersListEvents {
    object StartLoading : OrdersListEvents()
    object StopLoading : OrdersListEvents()
    class OnOrdersData(val ordersListModel: OrderListItemModel?) : OrdersListEvents()
    class OnSendTokenData(val sendTokenMessage: SendDeviceTokenModel?) : OrdersListEvents()
//    class OnOrdersData(val ordersListModel: List<OrderListItemModel>?) : OrdersListEvents()
    class OnTokenExpiredData(val isTokenExpired: String?) : OrdersListEvents()
    class OnNoInternetAvailable(val noInternetAvailable: String?) : OrdersListEvents()
    class Exception(val exception: java.lang.Exception?) : OrdersListEvents()
    class Error(val error: String?) : OrdersListEvents()
}