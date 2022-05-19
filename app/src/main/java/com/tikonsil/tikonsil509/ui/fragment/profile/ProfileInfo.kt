package com.tikonsil.tikonsil509.ui.fragment.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.domain.repository.profile.ProfileRepository
import com.tikonsil.tikonsil509.presentation.profile.ProfileViewModel
import com.tikonsil.tikonsil509.presentation.profile.ProfileViewModelFactory
import com.tikonsil.tikonsil509.ui.activity.login.LoginActivity

/** * Created by ISMOY BELIZAIRE on 27/04/2022. */
abstract class ProfileInfo<VB:ViewBinding,VM:ViewModel>:Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewmodel: ProfileViewModel
    protected lateinit var mAuthProvider: AuthProvider
    protected lateinit var navController: NavController
    protected var logout:LinearLayout?=null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        val repository = ProfileRepository()
        val factory = ProfileViewModelFactory(repository)
        viewmodel = ViewModelProvider(requireActivity(), factory)[ProfileViewModel::class.java]
        mAuthProvider = AuthProvider()
        logout =binding.root.findViewById(R.id.logout)
        return binding.root
    }

    fun showDataInProfile(){
        viewmodel.getOnlyUser(mAuthProvider.getId().toString())
        viewmodel.ResposeUsers.observe(viewLifecycleOwner, Observer { response->
            if (response.isSuccessful){
                val role_item =   view?.findViewById<TextView>(R.id.role_item)
                val firstname_item =view?.findViewById<TextView>(R.id.firstname_item)
                val lastname_item = view?.findViewById<TextView>(R.id.lastname_item)
                val number_item = view?.findViewById<TextView>(R.id.number_item)
                val email_item = view?.findViewById<TextView>(R.id.email_item)
                response.body()?.apply {
                    if (role==1){
                        role_item?.text =getString(R.string.agent)
                    }else{
                        role_item?.text =getString(R.string.master)
                    }
                    firstname_item?.text = firstname
                    lastname_item?.text = lastname
                    number_item?.text =phone
                    email_item?.text =email
                }

            }else{
                Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
            }
        })
    }
    fun Logout(){
        logout?.setOnClickListener {
            mAuthProvider.logout()
           startActivity(Intent(requireContext(),LoginActivity::class.java))
        }
    }
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    abstract fun getViewModel():Class<VM>
}