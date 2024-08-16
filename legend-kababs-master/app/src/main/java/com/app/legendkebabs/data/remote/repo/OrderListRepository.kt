package com.app.legendkebabs.data.remote.repo

import com.app.legendkebabs.constants.WebServiceAppConstants
import com.app.legendkebabs.data.remote.retrofit.Result
import com.app.legendkebabs.di.PrefManager
import javax.inject.Inject

class OrderListRepository @Inject constructor(
    private val networkCall: NetworkCall,
    private val pref : PrefManager
) {

    suspend fun getOrdersList(params: HashMap<String, Any>): Result =
        networkCall.get<String>(
            WebServiceAppConstants.PATH_ORDERS + "/${params[WebServiceAppConstants.Q_PARAM_DEVICE_ID]}?page=${params[WebServiceAppConstants.Q_PARAM_PAGE]}",
            null,
            null,
            null
        )

    suspend fun sendToken(params: HashMap<String, Any>): Result =
        networkCall.post<String>(
            WebServiceAppConstants.PATH_GET_DEVICE_ID + "/${params[WebServiceAppConstants.Q_PARAM_FCM_TOKEN]}" + "/${params[WebServiceAppConstants.Q_PARAM_DEVICE_ID]}",
            null,
            null,
            null
        )
}