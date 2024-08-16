package com.app.legendkebabs.data.remote.repo

import com.app.legendkebabs.constants.WebServiceAppConstants
import com.app.legendkebabs.data.remote.retrofit.Result
import com.app.legendkebabs.di.PrefManager
import javax.inject.Inject

class SingleOrderRepository @Inject constructor(
    private val networkCall: NetworkCall,
    private val pref : PrefManager
) {

    suspend fun updateOrderStatus(params: HashMap<String, Any>): Result =
        networkCall.post<String>(
            WebServiceAppConstants.PATH_UPDATE_ORDER + "/${params[WebServiceAppConstants.Q_PARAM_ORDER_ID]}?order_status=${params[WebServiceAppConstants.Q_PARAM_ORDER_STATUS]}",
            null,
            null,
            null
        )

    suspend fun getOrders(params: HashMap<String, Any>): Result =
        networkCall.get<String>(
            WebServiceAppConstants.PATH_ORDER + "/${params[WebServiceAppConstants.Q_PARAM_ORDER_ID]}",
            null,
            null,
            null
        )

    suspend fun getOrdersDetail(params: HashMap<String, Any>): Result =
        networkCall.get<String>(
            WebServiceAppConstants.PATH_GET_ORDER_DETAIL + "/${params[WebServiceAppConstants.Q_PARAM_ORDER_ID]}",
            null,
            null,
            null
        )
}