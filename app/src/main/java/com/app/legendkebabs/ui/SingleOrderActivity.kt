package com.app.legendkebabs.ui

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListAdapter
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.app.legendkebabs.R
import com.app.legendkebabs.constants.WebServiceAppConstants
import com.app.legendkebabs.data.model.OrderListItemModel
import com.app.legendkebabs.data.model.SingleOrderModel
import com.app.legendkebabs.databinding.ActivitySingleOrderBinding
import com.app.legendkebabs.ui.base.BaseActivity
import com.app.legendkebabs.ui.events.BaseEvent
import com.app.legendkebabs.ui.events.SingleOrderEvents
import com.app.legendkebabs.utils.CustomProgressDialog
import com.app.legendkebabs.view_model.SingleOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import org.jetbrains.anko.toast
import javax.inject.Inject

@AndroidEntryPoint
class SingleOrderActivity : BaseActivity() {
    private val binding : ActivitySingleOrderBinding by binding(R.layout.activity_single_order)

    lateinit var model: SingleOrderModel

    private var selectedIndex1 : Int = -1

    var cityListAdapter: ListAdapter? = null
    val status: ArrayList<String> = ArrayList()

    lateinit var viewModel: SingleOrderViewModel

    val params: HashMap<String, Any> = HashMap()

    var detail: String = "NA"

    @Inject
    lateinit var progressDialog : CustomProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(SingleOrderViewModel::class.java)

        viewModel.navEvent.observe(this, eventObserver)

        binding.imgBack.setOnClickListener {
            this.onBackPressed()
        }

        binding.btnSearch.setOnClickListener {
            params[WebServiceAppConstants.Q_PARAM_ORDER_ID] = model.order_id
            params[WebServiceAppConstants.Q_PARAM_ORDER_STATUS] = status[selectedIndex1]
            viewModel.updateOrder(params)
        }

        if(intent.hasExtra("sNo") && intent.getStringExtra("sNo") != null){
            binding.sNo = intent.getStringExtra("sNo")
        }

        if(intent.hasExtra("model") && intent.getSerializableExtra("model") != null){
            var model2 = intent.getSerializableExtra("model") as OrderListItemModel.Data
            println("model $model2")

            params[WebServiceAppConstants.Q_PARAM_ORDER_ID] = model2.order_id
            viewModel.getOrder(params)

//            params[WebServiceAppConstants.Q_PARAM_ORDER_ID] = model.order_id
//            viewModel.getOrderDetail(params)

//            status.add("Processing")
//            status.add("Pending")
//            status.add("Completed")
//            status.add("Cancelled")
//
//            initCityAdapter()
//
//            binding.model = model
//
//            detail = model.order_selected
//            detail = detail.replace("{", "")
//            detail = detail.replace("}", "")
//            detail = detail.replace("\\", "")
//            detail = detail.replace("\"", "")
//            detail = detail.replace(",", ",\n")
//
////            binding.tvValueDetail.setText(detail)
//
//            val html = "Order\n" +
//                    "id:1029<br /><br />Name:\"myproduct\"Qty:1<br />Meat: Lamb<br /><br />Extra Meat: 3.00<br /><br />Name:\"Legend Family combo = 4 kebab, 1 large chips, 1 large bottle of drink 1.25ml = \$46.50\"Qty:1<br /><b> Kabab 1</b><br />Type: Vegetarian<br /><br />Chips or Rice:\n" +
//                    "<br /><br />Type of Meat:\n" +
//                    "<br /><br />Type of Salad: Lettuce,Onion<br /><br />Type of Sauces: Sweet Chili,Mayonnaise,Test Sauce<br /><br /><b> Kabab 2</b><br />Type: Meat<br /><br />Extra Cheeze: 3.00,3.00<br /><br />Type of Meat:\n" +
//                    "<br /><br /><b> Kabab 3</b><br />Type: Vegetarian<br /><br />Type of Meat:\n" +
//                    "<br /><br />Falafel: Falafel<br /><br /><b> Kabab 4</b><br />Type: Meat<br /><br />Type of Meat:\n" +
//                    "<br /><br />Type of Drink: Pepsi<br />"
//            binding.tvValueDetail.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
//            } else {
//                Html.fromHtml(html)
//            }
//
//            when(model.order_status) {
//                "Processing" -> {
//                    selectedIndex1 = 0
//                }
//                "Pending" -> {
//                    selectedIndex1 = 1
//                }
//                "Completed" -> {
//                    selectedIndex1 = 2
//                }
//                "Cancelled" -> {
//                    selectedIndex1 = 3
//                }
//            }

        }
        binding.layoutStatus.setOnClickListener {
            showDialog()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private val eventObserver = Observer<BaseEvent<SingleOrderEvents>> {
        when (val event = it.getEventIfNotHandled()) {
            is SingleOrderEvents.StartLoading -> {
                progressDialog.show(this, "Loading...")
            }
            is SingleOrderEvents.StopLoading -> {
                progressDialog.dialog.cancel()
            }
            is SingleOrderEvents.OnTokenExpiredData -> {

            }
            is SingleOrderEvents.OnUpdateOrderData -> {
                toast(event.updateOrderMessage.toString())
                this.onBackPressed()
            }
            is SingleOrderEvents.OnOrderData -> {
//                toast(event.updateOrderMessage.toString())
                println(event.order.toString())
                model = event.order!!

                status.add("Processing")
                status.add("Pending")
                status.add("Completed")
                status.add("Cancelled")

                initCityAdapter()

                binding.model = model

                when(model.order_status) {
                    "Processing" -> {
                        selectedIndex1 = 0
                    }
                    "Pending" -> {
                        selectedIndex1 = 1
                    }
                    "Completed" -> {
                        selectedIndex1 = 2
                    }
                    "Cancelled" -> {
                        selectedIndex1 = 3
                    }
                }
                params[WebServiceAppConstants.Q_PARAM_ORDER_ID] = model.order_id
                viewModel.getOrderDetail(params)
            }

            is SingleOrderEvents.OnOrderDetailData -> {
//                toast(event.orderDetail.toString())
                println(event.orderDetail.toString())
                val html = event.orderDetail.toString()
                binding.tvValueDetail.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT)
                } else {
                    Html.fromHtml(html)
                }
            }

            is SingleOrderEvents.OnNoInternetAvailable -> {
                toast(event.noInternetAvailable.toString())
            }

            is SingleOrderEvents.Error -> {
                Log.d("TAG", "${event.error}")
                toast(event.error.toString())
            }

            is SingleOrderEvents.Exception -> {
                Log.d("TAG", "${event.exception?.message.toString()}")
                toast(event.exception?.message.toString())
            }
        }
    }

    private fun showDialog() {
        val builder = android.app.AlertDialog.Builder(this, R.style.MyAlertDialogTheme)
        builder.setTitle("Select Status")
        builder.setSingleChoiceItems(
            cityListAdapter, selectedIndex1
        ) { dialog, index ->
            selectedIndex1 = index

            binding.tvValueStatus.text = status[selectedIndex1]

            dialog.dismiss()
        }
        builder.show()
    }

    fun initCityAdapter() {
        cityListAdapter = object : ArrayAdapter<String?>(
            this,
            R.layout.select_dialog_singlechoice,
            android.R.id.text1,
            status as List<String>
        ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val v: View = super.getView(position, convertView, parent)
                val tv = v.findViewById<TextView>(android.R.id.text1)
                val dp10 =
                    (10 * context.resources.displayMetrics.density + 0.5f).toInt()
                tv.compoundDrawablePadding = dp10
                return v
            }
        }
    }
}