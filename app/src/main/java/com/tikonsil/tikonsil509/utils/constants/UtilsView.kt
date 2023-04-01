package com.tikonsil.tikonsil509.utils.constants

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.activity.invoice.InvoiceActivity
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral
import kotlin.math.roundToInt

object UtilsView {

    fun inputValidator(
        phone: TextInputEditText ,
        phoneLayout: TextInputLayout ,
        total: TextInputEditText ,
        totalLayout: TextInputLayout ,
        bntRecharge: Button ,
        context: Context
    ) {
        phone.doOnTextChanged { valuePhone , _ , _ , _ ->
            if (valuePhone!!.toString().isEmpty()) {
                phoneLayout.helperText = context.getString(R.string.erroremptyfield)
                bntRecharge.isEnabled = false
                return@doOnTextChanged
            } else {
                phoneLayout.helperText = ""
                bntRecharge.isEnabled = true
            }
        }

        total.doOnTextChanged { valueTotal , _ , _ , _ ->
            if (valueTotal!!.toString().isEmpty()) {
                totalLayout.helperText = context.getString(R.string.erroremptyfield)
                bntRecharge.isEnabled = false
                return@doOnTextChanged
            } else {
                totalLayout.helperText = ""
                bntRecharge.isEnabled = true
            }
        }
    }

    fun loginValidator(
        textInputLayoutEmail: TextInputLayout ,
        email: TextInputEditText ,
        textInputLayoutPassword: TextInputLayout ,
        password:TextInputEditText,
        context: Context,
        mConstant: Constant
    ) {
     email.doOnTextChanged { txtEmail, _, _, _ ->
         when {
             txtEmail!!.toString().isEmpty() -> {
                 textInputLayoutEmail.helperText =context.getText(R.string.erroremptyfield)
                 return@doOnTextChanged
             }
             !mConstant.validateEmail(txtEmail.toString())!! -> {
                 textInputLayoutEmail.helperText = context.getString(R.string.error_email_invalid)
             }
             else -> {
                 textInputLayoutEmail.helperText = ""
             }
         }
     }

        password.doOnTextChanged { txtPassword, _, _, _ ->
            when {
                txtPassword!!.toString().isEmpty() -> {
                    textInputLayoutPassword.helperText =context.getText(R.string.erroremptyfield)
                    return@doOnTextChanged
                }
                !mConstant.validatelongitudepassword(txtPassword.toString()) -> {
                    textInputLayoutPassword.helperText = context.getString(R.string.error_longitudepassword)
                }
                else -> {
                    textInputLayoutPassword.helperText = ""
                }
            }
        }
    }

    fun calculatePriceServiceHaitiMonCash(
        total: TextInputEditText ,
        chipSelected: String ,
        priceMonCash: Float ,
        subtotal: TextInputEditText
    ) {
        total.doOnTextChanged { valueInput , _ , _ , _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI1) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceMonCash
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }

    fun calculatePriceServiceHaitiNatCash(
        total: TextInputEditText ,
        chipSelected: String ,
        priceNatCash: Float ,
        subtotal: TextInputEditText
    ) {
        total.doOnTextChanged { valueInput , _ , _ , _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI2) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceNatCash
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }

    fun calculatePriceServiceHaitiLapouLa(
        total: TextInputEditText ,
        chipSelected: String ,
        priceLapouLa: Float ,
        subtotal: TextInputEditText
    ) {
        total.doOnTextChanged { valueInput , _ , _ , _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI3) {
                val countvaluemoncash: Double = valueInput.toString().toDouble() / priceLapouLa
                subtotal.setText(countvaluemoncash.roundToInt().toString())

            }
        }

    }

    fun getValueSharedPreferences(activity: Activity , value: String): String {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences("sharedPreferences" , Context.MODE_PRIVATE)
        return sharedPreferences.getString(value , "").toString()
    }

    fun setValueSharedPreferences(activity: Activity , key: String , value: String) {
        val sharedPreferences: SharedPreferences =
            activity.getSharedPreferences("sharedPreferences" , Context.MODE_PRIVATE)
        sharedPreferences.edit()
            .putString(key , value)
            .apply()
    }

    @SuppressLint("StringFormatInvalid")
    fun sendWhatsapp(salesData: Sales , activity: Activity) = try {
        val mensaje = activity.getString(
            R.string.extract_whatsapp ,
            "${salesData.phone} Name: ${salesData.firstname}" + " Total TopUp ${salesData.subtotal} Country: ${salesData.country}"
        )
        val i = Intent(Intent.ACTION_VIEW)
        i.data =
            Uri.parse("whatsapp://send?phone=${ConstantGeneral.PHONENUMBERWHATSAPP}&text=$mensaje")
        activity.startActivity(i)
    } catch (e: ActivityNotFoundException) {
        // WhatsApp is not installed on the device. Prompt the entity to install it.
        Toast.makeText(
            activity ,
            "WhatsApp no está instalado en este dispositivo" ,
            Toast.LENGTH_SHORT
        ).show()
    }

    fun supportWhatsapp(whatsapp: FloatingActionButton,activity: Activity){
        whatsapp.setOnClickListener {
            try {
                val mensaje =activity.getString(R.string.ayudawhatsapp)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse("whatsapp://send?phone=${ConstantGeneral.PHONENUMBERWHATSAPP}&text=$mensaje")
                activity.startActivity(i)
            } catch (e: ActivityNotFoundException) {
                // WhatsApp is not installed on the device. Prompt the entity to install it.
                Toast.makeText(activity, "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
            }

        }
    }
    @SuppressLint("SetTextI18n")
    fun createDialogNoMoney(context: Context , totalBalanceTopUpUser: Float) {
        val view = View.inflate(context , R.layout.dialognomoney , null)
        val builder = AlertDialog.Builder(context)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagenomoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text =
            context.getString(R.string.nomoney) + context.getString(R.string.yourbalance) + " $ " + totalBalanceTopUpUser
        button.setOnClickListener {
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    fun createDialogErrorPayWithMercadoPago(activity: Activity) {
        val view = View.inflate(activity , R.layout.dialognomoney , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagenomoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.errorpaymenymercadopago)
        button.text = "Ok"
        button.setOnClickListener {
            activity.startActivity(Intent(activity , HomeActivity::class.java))
            dialog.dismiss()
        }
    }

    fun createDialogSuccess(salesData: Sales , activity: Activity) {
        val view = View.inflate(activity , R.layout.dialog_success , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.success)
        button.setOnClickListener {
            val intent = Intent(activity , InvoiceActivity::class.java)
            intent.putExtra("saleData" , salesData)
            activity.startActivity(intent)
            activity.finish()
            dialog.dismiss()
        }
    }

    fun createDialogSuccessForAgent(activity: Activity) {
        val view = View.inflate(activity , R.layout.dialog_success , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.success)
        button.setOnClickListener {
            val intent = Intent(activity , HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            dialog.dismiss()
        }
    }

    fun createDialogErrorServer(activity: Activity) {
        val view = View.inflate(activity , R.layout.error_server , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)
        dialog.setCancelable(false)
        val button = view.findViewById<Button>(R.id.btnRetry)
        button.setOnClickListener {
            val intent = Intent(activity , HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            dialog.dismiss()
        }
    }




    fun createDialogSuccessRechargeAccountMaster(activity: Activity) {
        val view = View.inflate(activity , R.layout.dialog_success , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.successrecarcheaccount)
        button.setOnClickListener {
            val intent = Intent(activity , HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            dialog.dismiss()
        }
    }

    fun createDialogErrorForAgent(activity: Activity) {
        val view = View.inflate(activity , R.layout.dialog_success , null)
        val builder = AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.errorsendtopupinnoverit)
        button.setOnClickListener {
            val intent = Intent(activity , HomeActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
            dialog.dismiss()
        }
    }

    @SuppressLint("SetTextI18n")
    fun createDialogSuccessManually(salesData: Sales , activity: Activity) {
        val view = View.inflate(activity , R.layout.dialog_success , null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(activity)
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = activity.getString(R.string.thanks) + ConstantGeneral.PHONENUMBERWHATSAPP
        button.setOnClickListener {
            sendWhatsapp(salesData , activity)
            dialog.dismiss()
        }
    }

    fun extractNumberSubTotal(input: String): String {
        return Regex("\\s==>\\s").split(input)[0]
    }

    fun extractValue(valueTotal: String): String? {
        val regex = Regex("\\s*(\\d+(?:\\.\\d+)?)\\s*USD\\s*")
        val matchResult = regex.find(valueTotal)
        return matchResult?.groups?.get(1)?.value
    }



    fun showProgress(materialButton: MaterialButton , progressBar: ProgressBar) {
        materialButton.isEnabled = false
        materialButton.text = ""
        progressBar.isGone = false
    }

    fun hideProgress(materialButton: MaterialButton , progressBar: ProgressBar , text: String) {
        materialButton.isEnabled = true
        materialButton.text = text
        progressBar.isGone = true
    }
}