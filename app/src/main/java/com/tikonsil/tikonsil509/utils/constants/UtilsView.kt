package com.tikonsil.tikonsil509.utils.constants

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.widget.Button
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tikonsil.tikonsil509.R
import kotlin.math.roundToInt

object UtilsView {

    fun inputValidator(phone:TextInputEditText,phoneLayout:TextInputLayout,total:TextInputEditText,totalLayout:TextInputLayout,bntRecharge:Button,context: Context){
        phone.doOnTextChanged { valuePhone, _, _, _ ->
            if (valuePhone!!.toString().isEmpty()){
                phoneLayout.helperText = context.getString(R.string.erroremptyfield)
                bntRecharge.isEnabled = false
                return@doOnTextChanged
            }else{
                phoneLayout.helperText = ""
                bntRecharge.isEnabled = true
            }
        }

        total.doOnTextChanged { valueTotal, _, _, _ ->
            if (valueTotal!!.toString().isEmpty()){
                totalLayout.helperText = context.getString(R.string.erroremptyfield)
                bntRecharge.isEnabled = false
                return@doOnTextChanged
            }else{
                totalLayout.helperText = ""
                bntRecharge.isEnabled = true
            }
        }
    }

    fun calculatePriceServiceHaitiMonCash(total: TextInputEditText,chipSelected:String,priceMonCash:Float,subtotal:TextInputEditText){
        total.doOnTextChanged { valueInput, _, _, _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI1) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceMonCash
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }

    fun calculatePriceServiceHaitiNatCash(total: TextInputEditText,chipSelected:String,priceNatCash:Float,subtotal:TextInputEditText){
        total.doOnTextChanged { valueInput, _, _, _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI2) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceNatCash
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }

    fun calculatePriceServiceHaitiLapouLa(total: TextInputEditText,chipSelected:String,priceLapouLa:Float,subtotal:TextInputEditText){
        total.doOnTextChanged { valueInput, _, _, _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI3) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceLapouLa
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }
    fun getValueSharedPreferences(activity: Activity , value: String): String {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString(value, "").toString()
    }

    fun setValueSharedPreferences(activity: Activity , key: String , value: String) {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences("sharedPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(key, value)
            .apply()
    }
}