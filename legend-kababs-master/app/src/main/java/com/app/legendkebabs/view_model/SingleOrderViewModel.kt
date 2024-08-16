package com.app.legendkebabs.view_model

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.legendkebabs.ConnectivityException
import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.data.model.SingleOrderModel
import com.app.legendkebabs.data.remote.repo.SingleOrderRepository
import com.app.legendkebabs.data.remote.retrofit.Result
import com.app.legendkebabs.ui.events.BaseEvent
import com.app.legendkebabs.ui.events.OrdersListEvents
import com.app.legendkebabs.ui.events.SingleOrderEvents
import com.google.gson.Gson
import com.google.gson.JsonParseException
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

class SingleOrderViewModel @ViewModelInject constructor(
    private val gson: Gson,
    private val repository: SingleOrderRepository
): ViewModel() {

    private val singleOrderEvent = MutableLiveData<BaseEvent<SingleOrderEvents>>()
    val navEvent : LiveData<BaseEvent<SingleOrderEvents>> = singleOrderEvent

    fun updateOrder(params: HashMap<String, Any>) {
        viewModelScope.launch {
            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StartLoading)
            try {
                repository.updateOrderStatus(params).let {
                    when(it) {
                        is Result.Success -> {
                            singleOrderEvent.value = BaseEvent(
                                SingleOrderEvents.StopLoading)
                            try {
//                                val tokenType = object : TypeToken<MutableList<OrderListItemModel>>(){}.type
//                                val data: MutableList<OrderListItemModel> = gson.fromJson(it.response.toString(),tokenType)

                                val data: String = gson.fromJson(it.response.toString(), String::class.java)

                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.OnUpdateOrderData(data))
                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                        }
                        is Result.Exception -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException ||it.exception is ConnectException ||it.exception is SSLHandshakeException ||it.exception is TimeoutException){
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Exception(
                                    ConnectivityException()
                                ))
                            } else{
                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.Exception(it.exception))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.Error(e.message))
            }

        }
    }

    fun getOrder(params: HashMap<String, Any>) {
        viewModelScope.launch {
            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StartLoading)
            try {
                repository.getOrders(params).let {
                    when(it) {
                        is Result.Success -> {
                            singleOrderEvent.value = BaseEvent(
                                SingleOrderEvents.StopLoading)
                            try {
                                val tokenType = object : TypeToken<MutableList<SingleOrderModel>>(){}.type
                                val data: MutableList<SingleOrderModel> = gson.fromJson(it.response.toString(),tokenType)

//                                val data: String = gson.fromJson(it.response.toString(), String::class.java)

                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.OnOrderData(data[0]))
                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                        }
                        is Result.Exception -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException ||it.exception is ConnectException ||it.exception is SSLHandshakeException ||it.exception is TimeoutException){
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Exception(
                                    ConnectivityException()
                                ))
                            } else{
                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.Exception(it.exception))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.Error(e.message))
            }

        }
    }

    fun getOrderDetail(params: HashMap<String, Any>) {
        viewModelScope.launch {
            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StartLoading)
            try {
                repository.getOrdersDetail(params).let {
                    when(it) {
                        is Result.Success -> {
                            singleOrderEvent.value = BaseEvent(
                                SingleOrderEvents.StopLoading)
                            try {
//                                val tokenType = object : TypeToken<MutableList<OrderListItemModel>>(){}.type
//                                val data: MutableList<OrderListItemModel> = gson.fromJson(it.response.toString(),tokenType)

                                val data: String = gson.fromJson(it.response.toString(), String::class.java)

                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.OnOrderDetailData(data))
                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                        }
                        is Result.Exception -> {
                            singleOrderEvent.value = BaseEvent(SingleOrderEvents.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException ||it.exception is ConnectException ||it.exception is SSLHandshakeException ||it.exception is TimeoutException){
                                singleOrderEvent.value = BaseEvent(
                                    SingleOrderEvents.Exception(
                                    ConnectivityException()
                                ))
                            } else{
                                singleOrderEvent.value = BaseEvent(SingleOrderEvents.Exception(it.exception))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.StopLoading)
                singleOrderEvent.value= BaseEvent(SingleOrderEvents.Error(e.message))
            }

        }
    }
}