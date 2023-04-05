package com.tikonsil.tikonsil509.utils.constants

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Point
import android.net.Uri
import android.preference.PreferenceManager
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.isGone
import androidx.core.widget.doOnTextChanged
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.activity.invoice.InvoiceActivity
import com.tikonsil.tikonsil509.ui.fragment.mercadoPago.TakeCredentialsCardFormFragment
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral
import kotlin.math.roundToInt

object UtilsView {

    val TOLERANCE = 0.9
    val WITDH = 1
    val HEIGHT = 2
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

    fun registerUserValidator(
        textInputLayoutFirsName:TextInputLayout,
        textFirstName:TextInputEditText,
        textInputLayoutLastName:TextInputLayout,
        textLastName:TextInputEditText,
        textInputLayoutEmail: TextInputLayout,
        textEmail:TextInputEditText,
        textInputLayoutPhone:TextInputLayout,
        textPhone:TextInputEditText,
        textInputLayoutPassword: TextInputLayout,
        textPassword:TextInputEditText,
        textInputLayoutConfirmPassword: TextInputLayout,
        textConfirmPassword:TextInputEditText,
        btnRegister:MaterialButton,
        context: Context,
        mConstant: Constant

    ){
        textFirstName.doOnTextChanged { firstName, _, _, _ ->
            when {
                firstName.toString().isEmpty() -> {
                    textInputLayoutFirsName.helperText = context.getString(R.string.erroremptyfield)
                    btnRegister.isEnabled = false
                }
                !mConstant.validateonlyleter(firstName.toString()) -> {
                    textInputLayoutFirsName.helperText = context.getString(R.string.error_firsname_onlyletter)
                    btnRegister.isEnabled = false
                }
                else -> {
                    textInputLayoutFirsName.helperText = ""
                    btnRegister.isEnabled = true
                }
            }
        }

        textLastName.doOnTextChanged { lastName, _, _, _ ->
            when{
               lastName.toString().isEmpty() ->{
                   textInputLayoutLastName.helperText = context.getString(R.string.erroremptyfield)
                   btnRegister.isEnabled = false
               }
                !mConstant.validateonlyleter(lastName.toString()) -> {
                    textInputLayoutLastName.helperText = context.getString(R.string.error_lastname_onlyletter)
                    btnRegister.isEnabled = false
                }
                else -> {
                    textInputLayoutLastName.helperText = ""
                    btnRegister.isEnabled = true
                }
            }
        }

        textEmail.doOnTextChanged { txtEmail, _, _, _ ->
            when {
                txtEmail!!.toString().isEmpty() -> {
                    textInputLayoutEmail.helperText =context.getString(R.string.erroremptyfield)
                    btnRegister.isEnabled = false
                    return@doOnTextChanged
                }
                !mConstant.validateEmail(txtEmail.toString())!! -> {
                    textInputLayoutEmail.helperText = context.getString(R.string.error_email_invalid)
                    btnRegister.isEnabled = false
                }
                else -> {
                    textInputLayoutEmail.helperText = ""
                    btnRegister.isEnabled = true
                }
            }
        }

        textPhone.doOnTextChanged { phone, _, _, _ ->
            when{
                phone.toString().isEmpty() ->{
                    textInputLayoutPhone.helperText = context.getString(R.string.erroremptyfield)
                    btnRegister.isEnabled = false
                }
                !mConstant.validatelenghnumberphone(phone.toString()) ->{
                   textInputLayoutPhone.helperText = context.getString(R.string.error_longitud_number)
                    btnRegister.isEnabled = false
                }
                else ->{
                    textInputLayoutPhone.helperText = ""
                    btnRegister.isEnabled = true
                }
            }
        }

        textPassword.doOnTextChanged { password, _, _, _ ->
            when{
                password.toString().isEmpty() ->{
                    textInputLayoutPassword.helperText = context.getString(R.string.erroremptyfield)
                    btnRegister.isEnabled = false
                }
                !mConstant.validatelongitudepassword(password.toString()) ->{
                    textInputLayoutPassword.helperText = context.getString(R.string.error_longitudepassword)
                    btnRegister.isEnabled = false
                }
                else ->{
                    textInputLayoutPassword.helperText = ""
                    btnRegister.isEnabled = true
                }
            }
        }

        textConfirmPassword.doOnTextChanged { password, _, _, _ ->
            when{
                password.toString().isEmpty() ->{
                    textInputLayoutConfirmPassword.helperText = context.getString(R.string.erroremptyfield)
                    btnRegister.isEnabled = false
                }
                !mConstant.validatelongitudepassword(password.toString()) ->{
                    textInputLayoutConfirmPassword.helperText = context.getString(R.string.error_longitudepassword)
                    btnRegister.isEnabled = false
                }
                textPassword.text.toString() != textConfirmPassword.text.toString() ->{
                    textInputLayoutConfirmPassword.helperText = context.getString(R.string.error_no_coecidence_password)
                    btnRegister.isEnabled = false
                }
                else ->{
                    textInputLayoutConfirmPassword.helperText = ""
                    btnRegister.isEnabled = true
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
                val roundString = String.format("%.2f", countvaluemoncash)
                subtotal.setText(roundString)

            }
        }

    }

    fun calculatePriceServiceHaitiNatCash(
        amount: TextInputEditText ,
        chipSelected: String ,
        priceNatCash: Float ,
        subtotal: TextInputEditText
    ) {
        amount.doOnTextChanged { valueInput , _ , _ , _ ->
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI3) {
                val countvaluenatcash: Double = valueInput.toString().toDouble() / priceNatCash
                val roundString = String.format("%.2f", countvaluenatcash)
                subtotal.setText(roundString)

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
            if (valueInput!!.isNotEmpty() && chipSelected == ConstantServiceCountry.SERVICEHAITI2) {
                val countvaluelapoula: Double = valueInput.toString().toDouble() / priceLapouLa
                val roundString = String.format("%.2f", countvaluelapoula)
                subtotal.setText(roundString)

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

    fun screenDisplay(
        window: WindowManager,
        orientation: Int = WITDH,
        tolerance: Double = TOLERANCE
    ): Int {
        val point = Point()
        window.defaultDisplay.getSize(point)

        val size = (if (orientation == HEIGHT) point.y else point.x)
        return ((size * tolerance).toInt())
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

    fun saveTokenAdminToSharedPreferences(context: Context, tokenAdminList: ArrayList<String>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val set = HashSet<String>(tokenAdminList)
        editor.putStringSet("tokenAdminList", set)
        editor.apply()
    }

    fun readTokenAdminListFromSharedPreferences(context: Context): ArrayList<String> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val set = prefs.getStringSet("tokenAdminList", HashSet<String>()) ?: HashSet<String>()
        return ArrayList<String>(set)
    }


}