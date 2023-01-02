package com.tikonsil.tikonsil509.ui.fragment.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tikonsil.tikonsil509.databinding.FragmentProfileBinding
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.profile.ProfileViewModel
import com.tikonsil.tikonsil509.ui.fragment.home.HomeValidate


class ProfileFragment : ProfileInfo<FragmentProfileBinding,ProfileViewModel>() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showDataInProfile()
        Logout()
        imageView.setOnClickListener {
            openGallery()
        }
    }

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?) =
        FragmentProfileBinding.inflate(inflater,container,false)
    override fun getViewModel() = ProfileViewModel::class.java


}