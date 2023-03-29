package com.tikonsil.tikonsil509.ui.fragment.mercadoPago

import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.braintreepayments.cardform.view.CardForm
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.FragmentTakeCredentialsCardFormBinding
import com.tikonsil.tikonsil509.domain.model.CredentialCard
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel


class TakeCredentialsCardFormFragment : Fragment() {

    private lateinit var binding: FragmentTakeCredentialsCardFormBinding
    private val viewModel by lazy { ViewModelProvider(this)[MercadoPagoViewModel::class.java] }
    private lateinit var navController: NavController
    private var resultListener: String? = null
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTakeCredentialsCardFormBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE
        setUpCardForm()
        navController = Navigation.findNavController(view)
        parentFragmentManager.setFragmentResultListener(
            "payMaster" , viewLifecycleOwner
        ) { _ , result ->
            resultListener = result.getString("payMaster")
        }


    }


    private fun setUpCardForm() {
        with(binding) {
            cardForm.cardRequired(true)
                .cardholderName(CardForm.FIELD_REQUIRED)
                .expirationRequired(true)
                .cvvRequired(true)
                .maskCardNumber(true)
                .maskCvv(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .saveCardCheckBoxChecked(false)
                .saveCardCheckBoxVisible(false)
                .setup(requireActivity())
            cardForm.cvvEditText.inputType = InputType.TYPE_CLASS_NUMBER
            cardForm.cardholderNameEditText.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
            cardForm.cardholderNameEditText.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext() ,
                    R.color.white
                )
            )
            cardForm.cardEditText.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext() ,
                    R.color.white
                )
            )
            cardForm.expirationDateEditText.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext() ,
                    R.color.white
                )
            )
            cardForm.cvvEditText.setBackgroundColor(
                ContextCompat.getColor(
                    requireContext() ,
                    R.color.white
                )
            )

            btnNext.setOnClickListener(onClickListener())
        }
    }

    private fun onClickListener(): View.OnClickListener {
        return View.OnClickListener {
            validateForm()
        }

    }

    private fun validateForm() {
        if (binding.cardForm.isValid) {
            saveCredentialCard()
        } else binding.cardForm.validate()
    }

    private fun saveCredentialCard() {
        val cardNumber = binding.cardForm.cardNumber
        val cardMonth = binding.cardForm.expirationMonth
        val cardYear = binding.cardForm.expirationYear
        val cardCvv = binding.cardForm.cvv

        if (resultListener == "payMaster") {
            val result = "RechargeAccountMaster"
            parentFragmentManager.setFragmentResult(
                "RechargeAccountMaster" ,
                bundleOf("RechargeAccountMaster" to result)
            )
        }
        val credentialCard = CredentialCard(cardNumber,cardMonth,cardYear,cardCvv)
        val bundle =  bundleOf("credentialCard" to credentialCard)
        navController.navigate(R.id.action_takeCredentialsCardFormFragment_to_installmentFragment,bundle)
    }

    // TODO: active this function when  used Mercado Pago Payment
    /*  private fun createCardToken() {
        val cardHolder = Cardholder(name = binding.cardForm.cardholderNameEditText.text.toString())
          val mercadoPagoCardTokenBody = MercadoPagoCardTokenBody(securityCode = binding.cardForm.cvv,
          expirationYear = "20${binding.cardForm.expirationYear}",
          expirationMonth = binding.cardForm.expirationMonth.toString().toInt(),
          cardNumber = binding.cardForm.cardNumber,
          cardHolder = cardHolder)
          UtilsView.showProgress(binding.btnNext,binding.progressBar)
        viewModel.createCardToken(mercadoPagoCardTokenBody)
          viewModel.responseMercadoPago.observe(viewLifecycleOwner){response->
              if (response.isSuccessful){
                  saveCredentialInRoom(response)
                  binding.progressBar.isGone = true
                  if (resultListener == "payMaster"){
                      val result = "RechargeAccountMaster"
                      parentFragmentManager.setFragmentResult("RechargeAccountMaster", bundleOf("RechargeAccountMaster" to result))
                  }
                  navController.navigate(R.id.action_takeCredentialsCardFormFragment_to_installmentFragment)
              }
          }
      }

      @SuppressLint("SuspiciousIndentation")
      private fun saveCredentialInRoom(response: Response<JsonObject>?) {
          val cardToken =response?.body()?.get("id")?.asString.toString()
          val binMercadoPago = response?.body()?.get("first_six_digits")?.asString.toString()
          val lastFourDigits =response?.body()?.get("last_four_digits")?.asString.toString()
        val credential =MercadoPagoCredencials(0,cardToken, binMercadoPago,binding.cardForm.cardholderName,lastFourDigits)
          viewModel.insertCredential(credential)
      }*/
}