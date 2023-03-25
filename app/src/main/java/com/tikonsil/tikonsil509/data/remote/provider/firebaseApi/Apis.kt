package com.tikonsil.tikonsil509.data.remote.provider.firebaseApi

data class Apis(
    val base_url_tikonsil: String = "" ,
    val key: String = "" ,
    val fcm: FCM = FCM() ,
    val base_url_firebase_instance: String = "" ,
    val end_point_country_price: String = "" ,
    val end_point_register_user: String = "" ,
    val end_point_save_notification: String = "" ,
    val end_point_save_sales: String = "" ,
    val end_point_send_product: String = "" ,
    val server_key: String = "",
    val base_url_mercado_pago_create_token:String = "",
    val end_point_installments:String = "",
    val public_key_mercado_pago:String ="",
    val access_token_mercado_pago:String ="",
    val end_point_mercado_pago_create_token:String="",
    val end_point_list_price_recharge_account_master:String="",
    val end_point_mercado_pago_payment:String ="",
    val end_point_save_sales_error_innoverit:String = "",
    val end_point_payment_stripe:String =""
)

data class FCM(
    val base_url_fcm: String = "" ,
    val end_point_fcm: String = ""
)