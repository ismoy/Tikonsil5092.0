package com.tikonsil.tikonsil509.ui.fragment.request_recharge

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.stripe.android.PaymentConfiguration
import com.stripe.android.paymentsheet.PaymentSheet
import com.stripe.android.paymentsheet.PaymentSheetResult
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentRequestRechargeBinding
import com.tikonsil.tikonsil509.utils.constants.UtilsView


class RequestRechargeFragment :Fragment() {
    private lateinit var binding:FragmentRequestRechargeBinding
    private lateinit var navController: NavController
    private val REQUEST_CODE_LOCATION:Int =0
    lateinit var paymentSheet: PaymentSheet
    lateinit var paymentIntentClientSecret: String
    lateinit var customerConfig: PaymentSheet.CustomerConfiguration


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
            requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
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