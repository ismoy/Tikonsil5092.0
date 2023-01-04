package com.tikonsil.tikonsil509.ui.base

import android.annotation.SuppressLint
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.domain.repository.login.LoginRepository
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.login.LoginViewModelFactory
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.PHONENUMBERWHATSAPP
import de.hdodenhof.circleimageview.CircleImageView

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
abstract class BaseActivity<VM : ViewModel, VB : ViewBinding> : AppCompatActivity() {
    protected lateinit var binding: VB
    protected lateinit var viewmodel: UserViewModel
    protected lateinit var mAuthProvider: AuthProvider

    protected lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getActivityBinding()
        setContentView(binding.root)
        val repository = LoginRepository()
        val factory = LoginViewModelFactory(repository)
        viewmodel = ViewModelProvider(this, factory)[UserViewModel::class.java]
        mAuthProvider = AuthProvider()
    }

    protected abstract fun getActivityBinding(): VB
    protected abstract fun getViewModel(): Class<VM>


    @SuppressLint("SetTextI18n")
    fun showDataInView() {
        viewmodel.getOnlyUser(mAuthProvider.getId().toString())
        viewmodel.ResposeUsers.observe(this, Observer { response ->
            if (response.isSuccessful) {
                binding.root.apply {
                    val headerdrawer = findViewById<NavigationView>(R.id.nav_view)?.getHeaderView(0)
                    val nameheader = headerdrawer?.findViewById<TextView>(R.id.usernamedrawable)
                    val image_drawable = headerdrawer!!.findViewById<CircleImageView>(R.id.image_drawable)
                    response.body()?.apply {
                        nameheader?.text = "Hola,\n$firstname"
                        if (image!=null){
                            Glide.with(this@BaseActivity).load(image).into(image_drawable)
                        }
                        if (role==1){
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_saleWithOtherAgentFragment).isVisible = false
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_registerReferencesFragment).isVisible = false
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_policyAndPrivacyFragment).isVisible = false
                        }
                        if (role==2){
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_saleWithOtherAgentFragment).isEnabled = false
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_registerReferencesFragment).isEnabled = true
                            binding.root.findViewById<NavigationView>(R.id.nav_view).menu.findItem(R.id.nav_policyAndPrivacyFragment).isEnabled = false

                        }
                    }


                }


            } else {
                Toast.makeText(this, response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }

     fun sendMesageWhatsapp() {
        try {
            val mensaje =getString(R.string.ayudawhatsapp)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("whatsapp://send?phone=$PHONENUMBERWHATSAPP&text=$mensaje")
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed on the device. Prompt the user to install it.
            Toast.makeText(this, "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
        }
    }

}