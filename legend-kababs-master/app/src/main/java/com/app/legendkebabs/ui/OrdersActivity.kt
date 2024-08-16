package com.app.legendkebabs.ui

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.legendkebabs.R
import com.app.legendkebabs.adapters.OrderListItemAdapter
import com.app.legendkebabs.constants.WebServiceAppConstants
import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.databinding.ActivityOrdersBinding
import com.app.legendkebabs.di.PrefManager
import com.app.legendkebabs.di.PrefModule
import com.app.legendkebabs.ui.base.BaseActivity
import com.app.legendkebabs.ui.events.BaseEvent
import com.app.legendkebabs.ui.events.OrdersListEvents
import com.app.legendkebabs.ui.events.TokenRefreshEvent
import com.app.legendkebabs.utils.CustomProgressDialog
import com.app.legendkebabs.utils.getDeviceId
import com.example.mvvmpracticeproject.viewmodel.OrdersListViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.toast
import javax.inject.Inject


@AndroidEntryPoint
class OrdersActivity : BaseActivity() {

    @Inject
    lateinit var orderListItemAdapter: OrderListItemAdapter

    private val binding : ActivityOrdersBinding by binding(R.layout.activity_orders)

    lateinit var viewModel: OrdersListViewModel

    val params: HashMap<String, Any> = HashMap()

    var currentPage = 1
    var totalPages = 1
    var isPaginationLoading = false
    var isRefreshLoading = false

    @Inject
    lateinit var progressDialog : CustomProgressDialog

    lateinit var layoutManager: LinearLayoutManager

    var pastVisiblesItems: Int = 0
    var visibleItemCount: Int = 0
    var totalItemCount: Int = 0

    private val mMessageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
//            awaitingDriverRequesting.dismiss()
//            awaitingDriverPhone.setText(intent.extras!!.getString("phone")) //setting values to the TextViews
//            awaitingDriverLat.setText(intent.extras!!.getDouble("lat"))
//            awaitingDriverLng.setText(intent.extras!!.getDouble("lng"))

            params[WebServiceAppConstants.Q_PARAM_FCM_TOKEN] = intent.getStringExtra("token").toString()
            params[WebServiceAppConstants.Q_PARAM_DEVICE_ID] = getDeviceId(this@OrdersActivity)
            viewModel.sendToken(params)

            println("fcm refresh token sent to server ${intent.getStringExtra("token")}")
            println("getDeviceId ${params[WebServiceAppConstants.Q_PARAM_DEVICE_ID]}")
        }
    }

    override fun onStart() {
//        if (!EventBus.getDefault().isRegistered(this)) {
//            EventBus.getDefault().register(this)
//        }
        LocalBroadcastManager.getInstance(this).registerReceiver(
            (mMessageReceiver),
            IntentFilter ("MyData")
        );
        super.onStart()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        orderListItemAdapter = OrderListItemAdapter()


        binding.adapter = orderListItemAdapter
        layoutManager = LinearLayoutManager(this)
        binding.orderList.layoutManager = layoutManager

        binding.pullToRefresh.setOnRefreshListener {
            currentPage = 1
            orderListItemAdapter.clearList()
            isRefreshLoading = true

            params[WebServiceAppConstants.Q_PARAM_PAGE] = currentPage
            params[WebServiceAppConstants.Q_PARAM_DEVICE_ID] = getDeviceId(this)
            viewModel.getOrdersList(params)
        }

        orderListItemAdapter.setOrderClickListener(object :
            OrderListItemAdapter.OnOrderClickListener {
            override fun onOrderClicked(sNo: String, model: OrderListItemModel.Data) {
                val intent = Intent(this@OrdersActivity, SingleOrderActivity::class.java)
                intent.putExtra("model", model)
                intent.putExtra("sNo", sNo)
                startActivity(intent)
            }
        })

        viewModel = ViewModelProvider(this).get(OrdersListViewModel::class.java)

        viewModel.navEvent.observe(this, eventObserver)

        getFcmToken()

        params[WebServiceAppConstants.Q_PARAM_PAGE] = currentPage
        params[WebServiceAppConstants.Q_PARAM_DEVICE_ID] = getDeviceId(this)
        viewModel.getOrdersList(params)

        binding.orderList.setOnScrollChangeListener(object : View.OnScrollChangeListener {
            override fun onScrollChange(p0: View?, p1: Int, p2: Int, dx: Int, dy: Int) {
                println("dy $dy")

                visibleItemCount = layoutManager.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                println("visibleItemCount $visibleItemCount")
                println("totalItemCount $totalItemCount")
                println("pastVisiblesItems $pastVisiblesItems")

                if (!isPaginationLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {

                        Log.v("...", "Last Item Wow !");
                        // Do pagination.. i.e. fetch new data
                        println("currentPage $currentPage")
                        println("totalPages $totalPages")
                        if (currentPage < totalPages) {
                            isPaginationLoading = true
                            binding.loaderPagination.visibility = View.VISIBLE

                            currentPage++
                            params[WebServiceAppConstants.Q_PARAM_PAGE] = currentPage
                            viewModel.getOrdersList(params)
                        }
                    }
                }
                if (dy > 0) { //check for scroll down

                }
            }
        })
    }

//    override fun onResume() {
//        super.onResume()
//        if(intent.hasExtra("is_notification")  && intent.getBooleanExtra("is_notification", false) != null && intent.getBooleanExtra("is_notification", false)) {
//            println("is_notification ${intent.getBooleanExtra("is_notification", false)}")
//            toast("is_notification ${intent.getBooleanExtra("is_notification", false)}")
//        }
//
//        if(intent.hasExtra("body")  && intent.getStringExtra("body") != null && intent.getStringExtra("body")!!.isNotEmpty()) {
//            println("body ${intent.getStringExtra("body")}")
//            toast("body ${intent.getStringExtra("body")}")
//        }
//    }

    override fun onStop() {
//        EventBus.getDefault().unregister(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onStop()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val eventObserver = Observer<BaseEvent<OrdersListEvents>> {
        when (val event = it.getEventIfNotHandled()) {
            is OrdersListEvents.StartLoading -> {
                if (!isPaginationLoading && !isRefreshLoading) {
                    progressDialog.show(this, "Loading...")
                }
            }
            is OrdersListEvents.StopLoading -> {
                println("stop loading")
                isPaginationLoading = false
                isRefreshLoading = false
                binding.loaderPagination.visibility = View.GONE
                progressDialog.dialog.cancel()
                binding.pullToRefresh.setRefreshing(false)
            }
            is OrdersListEvents.OnTokenExpiredData -> {

            }
            is OrdersListEvents.OnOrdersData -> {
                totalPages = event.ordersListModel!!.last_page
                if (event.ordersListModel.data.size > 0) {
//                    print("event.ordersListModel.toString() ${event.ordersListModel.toString()}")
                    orderListItemAdapter.setItemList(event.ordersListModel.data)
                }
            }

            is OrdersListEvents.OnSendTokenData -> {
                println("fcm token sent ${event.sendTokenMessage!!.devId}")
//                toast(event.sendTokenMessage!!.devId)
            }

            is OrdersListEvents.OnNoInternetAvailable -> {
                toast(event.noInternetAvailable.toString())
            }

            is OrdersListEvents.Error -> {
                Log.d("TAG", "${event.error}")
                toast(event.error.toString())
            }

            is OrdersListEvents.Exception -> {
                Log.d("TAG", "${event.exception?.message.toString()}")
                toast(event.exception?.message.toString())
            }
        }
    }

    fun getFcmToken() {

//        FirebaseMessaging.getInstance().deleteToken()

        val pref = PrefManager(PrefModule.provideSharedPreferences(), Gson())

        println("fcm token 1 ${pref.getToken().isEmpty()}")
        println("fcm token 1 ${pref.getToken()}")

        if(pref.getToken().isEmpty()) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result

                println("fcm token received $token")

                pref.putToken(token!!)

                params[WebServiceAppConstants.Q_PARAM_FCM_TOKEN] = token
                params[WebServiceAppConstants.Q_PARAM_DEVICE_ID] = getDeviceId(this)
                viewModel.sendToken(params)
                println("getDeviceId ${params[WebServiceAppConstants.Q_PARAM_DEVICE_ID]}")
                println("fcm token $token")
                println("fcm token sending to server for first time $token")
            })
        }
        else {
            params[WebServiceAppConstants.Q_PARAM_FCM_TOKEN] = pref.getToken()
            params[WebServiceAppConstants.Q_PARAM_DEVICE_ID] = getDeviceId(this)
            viewModel.sendToken(params)

            println("fcm token from pref ${pref.getToken()}")
        }
    }
}