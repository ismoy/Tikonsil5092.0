package com.tikonsil.tikonsil509.ui.fragment.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.BuildConfig
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.db.UsersDatabase
import com.tikonsil.tikonsil509.databinding.FragmentHomeBinding
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.version.VersionCodeViewModel
import com.tikonsil.tikonsil509.ui.fragment.dialog.DialogConfirm
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral

class HomeFragment :HomeValidate<FragmentHomeBinding,UserViewModel>() {

    private val versionCodeViewModel by lazy { ViewModelProvider(this)[VersionCodeViewModel::class.java] }
    private  val bottomSheet = DialogConfirm()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        showDataInView()
        observeData()
        generateToken()
        gotosendrecharge()
        gotosendrecharge2()
        gotosendrecharge3()
        gotosendrecharge4()
         getTokenUser()
        getTokenAdmin()
        viewModelObserver()
    }

    private fun viewModelObserver() {
        versionCodeViewModel.versionCode.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                val versionName = BuildConfig.VERSION_NAME
                if (it.body()?.versionCode!! >versionName){
                    bottomSheet.show(childFragmentManager,"DialogConfirm")
                    bottomSheet.subtitle =getString(R.string.nue_version_available)
                    bottomSheet.btnCancel = false
                    bottomSheet.hasNewVersion =true
                    bottomSheet.btnConfirm = getString(R.string.updated)
                }

            }
        }
    }


    private fun generateToken() {
        userTokenProvider.createToken(mAuthProvider.getId().toString())
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =FragmentHomeBinding.inflate(inflater,container,false)
    override fun getViewModel() = UserViewModel::class.java

    override fun onResume() {
        super.onResume()
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.VISIBLE
        versionCodeViewModel.getVersionCode()
    }
}