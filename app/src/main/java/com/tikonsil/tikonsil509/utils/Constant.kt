package com.tikonsil.tikonsil509.utils

import android.util.Patterns
import com.tikonsil.tikonsil509.R
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.math.floor

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class Constant {
 companion object{
  const val BASE_URL = "https://tikonsil509-ea2cc-default-rtdb.firebaseio.com/"
  const val BASE_URL_FCM ="https://fcm.googleapis.com/"
  const val BASE_URL_MERCADOPAGO ="https://api.mercadopago.com/"
  const val SERVER_KEY ="AAAAj812wm0:APA91bGryDQHRsHZQQqKNkA6gdpeeEBl1w7prud_cFILDbgDwZoEkpYlO8GjkKfZKzzL_GyBxsjKGkjD4BDZ6Zwrfm793aRL-d3EYwTzvLkXeojUQemb5b0XsehoI5oFGYy5yzvY7hw-"
  const val CONTENT_TYPE ="application/json"
  const val MATCHES ="^[ a-zA-Z\\u00F1\\u00D1]+\$"
  const val TITLE = "TIKONSIL509"
  const val SUBTITLE ="Esta boleta sirve por cualquier reclamo"
  const val SLOGAN = "Tikonsil PAM se PAW"
  var DESCRIPTION =""
  var TOTAL =""
  var FIRSTNAME =""
  var COUNTRY =""
  var PHONE_NUMBER =""
  var JOB =0
  var TYPE_RECHARGE =""
  var DATE_CREATE =""
  var EMAIL_ =""
  var SUBJECT ="Nueva ventas"
 }
 //validate email
 fun validateEmail(email:String?): Boolean? {
  val pattern =Patterns.EMAIL_ADDRESS;
  return email?.let { pattern.matcher(it).matches() }
 }

 //validate only letter
 fun validateonlyleter(datos:String):Boolean{
  return datos.matches(MATCHES.toRegex())
 }
 //validate number phone
 fun validatelenghnumberphone(numbers:String):Boolean{
  return numbers.length <= 12
 }
 //validate longitude password
 fun validatelongitudepassword(numbers: String):Boolean{
  return numbers.length>=8
 }
}