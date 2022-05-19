package com.tikonsil.tikonsil509.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.tikonsil.tikonsil509.presentation.register.RegisterViewModel
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider

/** * Created by ISMOY BELIZAIRE on 23/04/2022. */
abstract class BaseFragment<VM:ViewModel,VB:ViewBinding>:Fragment() {
 protected lateinit var binding:VB
 protected lateinit var viewmodel: RegisterViewModel
 protected lateinit var mAuthProvider: AuthProvider

 override fun onCreateView(
  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  binding = getFragmentBinding(inflater, container)
  /*val repository = RegisterRepository(this)
  val factory = RegisterViewModelFactory(repository)
  viewmodel = ViewModelProvider(requireActivity(),factory)[RegisterViewModel::class.java]
  mAuthProvider = AuthProvider()*/
  return binding.root
 }

 abstract fun getViewModel():Class<VM>
 abstract fun getFragmentBinding(inflater: LayoutInflater,container: ViewGroup?):VB
}