package com.tikonsil.tikonsil509.ui.fragment.request_recharge

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.entity.PriceRechargeAccountMaster
import com.tikonsil.tikonsil509.databinding.FragmentRequestRechargeBinding
import com.tikonsil.tikonsil509.presentation.requestrecharge.RequestRechargeViewModel
import com.tikonsil.tikonsil509.utils.constants.UtilsView


class RequestRechargeFragment :Fragment() {
    private lateinit var binding:FragmentRequestRechargeBinding
    private lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestRechargeBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE
        observerViewModel()
        binding.paysold.setOnClickListener(onclickPayListener())
    }

    private fun onclickPayListener(): View.OnClickListener {
        return View.OnClickListener {
            val result = "payMaster"
            parentFragmentManager.setFragmentResult("payMaster", bundleOf("payMaster" to result))
            navController.navigate(R.id.action_requestRechargeFragment_to_takeCredentialsCardFormFragment)
            UtilsView.setValueSharedPreferences(requireActivity(),"amountRecharge",binding.autoCompleteTextView.text.toString())
        }


    }

    private fun observerViewModel() {
        binding.autoCompleteTextView.doOnTextChanged { text, _, _, _ ->
            if (text.toString().isNotEmpty()){
                binding.paysold.isEnabled = true
            }
        }

    }
}