package com.example.mvvmpracticeproject.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.legendkebabs.ConnectivityException
import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.data.model.SendDeviceTokenModel
import com.app.legendkebabs.data.remote.repo.OrderListRepository
import com.app.legendkebabs.ui.events.BaseEvent
import com.app.legendkebabs.ui.events.OrdersListEvents
import com.app.legendkebabs.data.remote.retrofit.Result
import com.google.gson.Gson
import com.google.gson.JsonParseException
import kotlinx.coroutines.launch
import java.lang.Exception
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import javax.net.ssl.SSLHandshakeException

class OrdersListViewModel @ViewModelInject constructor(
    private val gson: Gson,
    private val repository: OrderListRepository
): ViewModel() {

    private val orderListEvent = MutableLiveData<BaseEvent<OrdersListEvents>>()
    val navEvent : LiveData<BaseEvent<OrdersListEvents>> = orderListEvent

    fun getOrdersList(params: HashMap<String, Any>) {
        viewModelScope.launch {
            orderListEvent.value = BaseEvent(OrdersListEvents.StartLoading)
            try {
                repository.getOrdersList(params).let {
                    when(it) {
                        is Result.Success -> {
                            orderListEvent.value = BaseEvent(
                                OrdersListEvents.StopLoading)
                            try {
//                                val tokenType = object : TypeToken<MutableList<OrderListItemModel>>(){}.type
//                                val data: MutableList<OrderListItemModel> = gson.fromJson(it.response.toString(),tokenType)

                                val data: OrderListItemModel = gson.fromJson(it.response.toString(), OrderListItemModel::class.java)

                                orderListEvent.value = BaseEvent(OrdersListEvents.OnOrdersData(data))
                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                orderListEvent.value = BaseEvent(
                                    OrdersListEvents.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            orderListEvent.value = BaseEvent(OrdersListEvents.StopLoading)
                            orderListEvent.value = BaseEvent(OrdersListEvents.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            orderListEvent.value= BaseEvent(OrdersListEvents.StopLoading)
                        }
                        is Result.Exception -> {
                            orderListEvent.value = BaseEvent(OrdersListEvents.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException ||it.exception is ConnectException ||it.exception is SSLHandshakeException ||it.exception is TimeoutException){
                                orderListEvent.value = BaseEvent(OrdersListEvents.Exception(
                                    ConnectivityException()
                                ))
                            } else{
                                orderListEvent.value = BaseEvent(OrdersListEvents.Exception(it.exception))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                orderListEvent.value= BaseEvent(OrdersListEvents.StopLoading)
                orderListEvent.value= BaseEvent(OrdersListEvents.Error(e.message))
            }

        }
    }

    fun sendToken(params: HashMap<String, Any>) {
        viewModelScope.launch {
//            orderListEvent.value = BaseEvent(OrdersListEvents.StartLoading)
            try {
                repository.sendToken(params).let {
                    when(it) {
                        is Result.Success -> {
//                            orderListEvent.value = BaseEvent(
//                                OrdersListEvents.StopLoading)
                            try {
//                                val tokenType = object : TypeToken<MutableList<OrderListItemModel>>(){}.type
//                                val data: MutableList<OrderListItemModel> = gson.fromJson(it.response.toString(),tokenType)

                                val data: SendDeviceTokenModel = gson.fromJson(it.response.toString(), SendDeviceTokenModel::class.java)

                                orderListEvent.value = BaseEvent(OrdersListEvents.OnSendTokenData(data))
                            } catch (e: JsonParseException) {
                                e.printStackTrace()
                                orderListEvent.value = BaseEvent(
                                    OrdersListEvents.Error(e.message))
                            }
                        }
                        is Result.Error -> {
                            orderListEvent.value = BaseEvent(OrdersListEvents.StopLoading)
                            orderListEvent.value = BaseEvent(OrdersListEvents.Error(it.error))
                        }
                        is Result.RetrofitError -> {
                            orderListEvent.value= BaseEvent(OrdersListEvents.StopLoading)
                        }
                        is Result.Exception -> {
                            orderListEvent.value = BaseEvent(OrdersListEvents.StopLoading)
                            if (it.exception is SocketTimeoutException || it.exception is UnknownHostException ||it.exception is ConnectException ||it.exception is SSLHandshakeException ||it.exception is TimeoutException){
                                orderListEvent.value = BaseEvent(OrdersListEvents.Exception(
                                    ConnectivityException()
                                ))
                            } else{
                                orderListEvent.value = BaseEvent(OrdersListEvents.Exception(it.exception))
                            }
                        }
                    }
                }
            }catch (e: Exception){
                orderListEvent.value= BaseEvent(OrdersListEvents.StopLoading)
                orderListEvent.value= BaseEvent(OrdersListEvents.Error(e.message))
            }

        }
    }
}