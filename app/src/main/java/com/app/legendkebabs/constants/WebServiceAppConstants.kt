package com.app.legendkebabs.constants

class WebServiceAppConstants {
    companion object{

        /**
         * API PATHS NAMES
         */

        //Orders
        const val PATH_ORDERS = "orders"
        const val PATH_ORDER = "order"
        const val PATH_UPDATE_ORDER = "updOrd"
        const val PATH_GET_DEVICE_ID = "getDevId"
        const val PATH_GET_ORDER_DETAIL = "orderdtail"

        /**
         * QUERY PARAMS
         */

        //Auth
        const val Q_PARAM_PAGE = "page"
        const val Q_PARAM_DEVICE_ID = "device_id"
        const val Q_PARAM_FCM_TOKEN = "fcm_token"
        const val Q_PARAM_ORDER_ID = "order_id"
        const val Q_PARAM_ORDER_STATUS = "order_status"

    }
}