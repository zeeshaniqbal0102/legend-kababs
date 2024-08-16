package com.app.legendkebabs.ui.events

import com.app.legendkebabs.data.model.SingleOrderModel

sealed class SingleOrderEvents {
    object StartLoading : SingleOrderEvents()
    object StopLoading : SingleOrderEvents()
    class OnUpdateOrderData(val updateOrderMessage: String?) : SingleOrderEvents()
    class OnOrderData(val order: SingleOrderModel?) : SingleOrderEvents()
    class OnOrderDetailData(val orderDetail: String?) : SingleOrderEvents()
    class OnTokenExpiredData(val isTokenExpired: String?) : SingleOrderEvents()
    class OnNoInternetAvailable(val noInternetAvailable: String?) : SingleOrderEvents()
    class Exception(val exception: java.lang.Exception?) : SingleOrderEvents()
    class Error(val error: String?) : SingleOrderEvents()
}