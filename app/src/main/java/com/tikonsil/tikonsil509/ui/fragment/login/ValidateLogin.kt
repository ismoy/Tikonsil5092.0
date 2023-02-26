package com.tikonsil.tikonsil509.ui.fragment.login

import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.domain.repository.login.LoginRepository
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.presentation.login.LoginViewModel
import com.tikonsil.tikonsil509.presentation.login.LoginViewModelFactory
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
abstract class ValidateLogin<VM:ViewModel,VB:ViewBinding>:Fragment() {
 protected lateinit var binding:VB
 protected lateinit var mConstant: Constant
 protected lateinit var viewmodel: LoginViewModel
 protected lateinit var mAuthProvider: AuthProvider
 lateinit var dialog:Dialog
 protected lateinit var navController: NavController
 private var whatsapp :FloatingActionButton?=null
 private var testApiInnovit :Button?=null
 private lateinit var mUserProvider:UserProvider

 override fun onCreateView(
  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  binding = getFragmentBinding(inflater, container)
  val repository = LoginRepository()
  val factory = LoginViewModelFactory(repository)
  viewmodel = ViewModelProvider(requireActivity(),factory)[LoginViewModel::class.java]
  mAuthProvider = AuthProvider()
  mConstant = Constant()
  dialog = Dialog(requireContext())
  whatsapp =binding.root.findViewById(R.id.whatsapp)
  testApiInnovit =binding.root.findViewById(R.id.testApi)
  mUserProvider =UserProvider()
  return binding.root
 }

 fun validateRealTime(){
  binding.root.apply {
   findViewById<TextInputEditText>(R.id.emaillogin).addTextChangedListener(object :TextWatcher{
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
     when{
      findViewById<TextInputEditText>(R.id.emaillogin).text.toString().isEmpty()->{
       findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = getString(R.string.erroremptyfield)
      }
      !mConstant.validateEmail(findViewById<TextInputEditText>(R.id.emaillogin).text.toString())!! ->{
      findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = getString(R.string.error_email_invalid)
     }else->{
      findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = ""
     }
     }
    }
    override fun afterTextChanged(p0: Editable?) {
    }

   })
   //validate password
   findViewById<TextInputEditText>(R.id.passwordlogin).addTextChangedListener(object :TextWatcher{
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
    }
    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
     when{
      findViewById<TextInputEditText>(R.id.passwordlogin).text.toString().isEmpty()->{
       findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText = getString(R.string.erroremptyfield)
      }
      !mConstant.validatelongitudepassword(findViewById<TextInputEditText>(R.id.passwordlogin).text.toString())->{
       findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText =getString(R.string.error_longitudepassword)
      }else->{
      findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText=""
     }
     }
    }
    override fun afterTextChanged(p0: Editable?) {
    }

   })

  }
 }
 fun validateOnclickButton(){
  binding.root.apply {
   //Validate Email
   when{
    findViewById<TextInputEditText>(R.id.emaillogin).text.toString().isEmpty()->{
     findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = getString(R.string.erroremptyfield)
    }
    !mConstant.validateEmail(findViewById<TextInputEditText>(R.id.emaillogin).text.toString())!! ->{
     findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = getString(R.string.error_email_invalid)
    }else->{
    findViewById<TextInputLayout>(R.id.layoutemaillogin).helperText = ""
   }
   }
   //Validate Password
   when{
    findViewById<TextInputEditText>(R.id.passwordlogin).text.toString().isEmpty()->{
     findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText = getString(R.string.erroremptyfield)
    }
    !mConstant.validatelongitudepassword(findViewById<TextInputEditText>(R.id.passwordlogin).text.toString())->{
     findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText =getString(R.string.error_longitudepassword)
    }else->{
    findViewById<TextInputLayout>(R.id.layoutpasswordlogin).helperText=""
    Login(findViewById<TextInputEditText>(R.id.emaillogin).text.toString(),findViewById<TextInputEditText>(R.id.passwordlogin).text.toString())
   }
   }
  }
 }

 private fun getUser() {
  mUserProvider.getUser()?.addListenerForSingleValueEvent(object : ValueEventListener {
   override fun onDataChange(snapshot: DataSnapshot) {
    if (snapshot.exists()){
     for (ds in snapshot.children){
      val email = ds.child("email").value.toString()
      if (email !=binding.root.findViewById<TextInputEditText>(R.id.emaillogin).text.toString()){
       Toast.makeText(requireContext() , "No tienes permiso para ingresar en la App" , Toast.LENGTH_SHORT).show()
      }else{
       binding.root.apply {
        Login(findViewById<TextInputEditText>(R.id.emaillogin).text.toString(),findViewById<TextInputEditText>(R.id.passwordlogin).text.toString())

       }
      }
     }

    }
   }

   override fun onCancelled(error: DatabaseError) {

   }

  })
 }


  private fun  Login(email:String?, password:String?) {
   dialog.setContentView(R.layout.dialog_loading)
   dialog.setCancelable(false)
   if (dialog.window!=null){
    dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
   }
   dialog.show()
   viewmodel.login(email!!,password!!)
   viewmodel.responseLoginRepository.observe(requireActivity(), Observer { it ->
    it.addOnCompleteListener {
     if (it.isSuccessful){
      if (mAuthProvider.isEmailVerified()==true){
        val intent = Intent(requireContext(), HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        dialog.dismiss()

       }else{
        dialog.dismiss()
        Toast.makeText(requireContext(), getString(R.string.verifyaccount), Toast.LENGTH_LONG).show()
       }
      }else{
       Toast.makeText(requireContext(), it.exception?.message, Toast.LENGTH_LONG).show()
      dialog.dismiss()
      }
     }
   })
 }
 fun clickwhatsapp(){
  whatsapp?.setOnClickListener {
   try {
    val mensaje =getString(R.string.ayudawhatsapp)
    val i = Intent(Intent.ACTION_VIEW)
    i.data = Uri.parse("whatsapp://send?phone=${ConstantGeneral.PHONENUMBERWHATSAPP}&text=$mensaje")
    startActivity(i)
   } catch (e: ActivityNotFoundException) {
    // WhatsApp is not installed on the device. Prompt the entity to install it.
    Toast.makeText(requireActivity(), "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
   }

  }
 }
 abstract fun getViewModel():Class<VM>
 abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?):VB
}