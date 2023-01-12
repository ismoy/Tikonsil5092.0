package com.tikonsil.tikonsil509.utils.constants

import android.util.Patterns

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
class Constant {
 companion object{
  const val BASE_URL = "https://tikonsil509-ea2cc-default-rtdb.firebaseio.com/"
  const val BASE_URL_INNOVIT = "https://www.innoverit.com/api/v2/"
  const val BASE_URL_FCM ="https://fcm.googleapis.com/"
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
  const val KEY ="create"
  const val NOTE ="false"
  const val ID_PRODUCT ="22050786"
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
  return numbers.length <= 9
 }
 //validate longitude password
 fun validatelongitudepassword(numbers: String):Boolean{
  return numbers.length>=8
 }
}