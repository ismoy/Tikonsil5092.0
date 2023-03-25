package com.tikonsil.tikonsil509.ui.fragment.mercadoPago

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.databinding.FragmentInstallmentBinding
import com.tikonsil.tikonsil509.domain.model.CredentialCard
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRechargeProduct
import com.tikonsil.tikonsil509.domain.model.SendRechargeResponse
import com.tikonsil.tikonsil509.domain.model.mercadoPago.MercadoPagoInstallments
import com.tikonsil.tikonsil509.domain.model.mercadoPago.Payment
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePayment
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.presentation.stripePayment.StripePaymentViewModel
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogErrorForAgent
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogErrorPayWithMercadoPago
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogSuccessForAgent
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogSuccessRechargeAccountMaster
import com.tikonsil.tikonsil509.utils.constants.UtilsView.hideProgress
import com.tikonsil.tikonsil509.utils.constants.UtilsView.showProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.roundToInt


class InstallmentFragment : Fragment() {

    private lateinit var binding:FragmentInstallmentBinding
    private val viewModel by lazy { ViewModelProvider(this)[MercadoPagoViewModel::class.java] }
    private lateinit var navController: NavController
    private lateinit var mUserProvider: UserProvider
    private lateinit var sendRechargeViewModel: SendRechargeViewModel
    private lateinit var  listProduct:List<Product>
    private lateinit var mAuthProvider:AuthProvider
    private var resultListener:String? =null
    private lateinit var credentialCard:CredentialCard
    private val stripePaymentViewModel by lazy { ViewModelProvider(this)[StripePaymentViewModel::class.java] }
    private var valueFees:Float?=0F


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {bundle->
             credentialCard = bundle.getParcelable("credentialCard")!!
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater , container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInstallmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        navController = Navigation.findNavController(view)
        mUserProvider = UserProvider()
        mAuthProvider = AuthProvider()
        val repository = SendRechargeRepository()
        val factory = SendRechargeViewModelProvider(repository)
        sendRechargeViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[SendRechargeViewModel::class.java]
        parentFragmentManager.setFragmentResultListener("RechargeAccountMaster",viewLifecycleOwner
        ) { _ , result ->
             resultListener = result.getString("RechargeAccountMaster")
            manageView(resultListener)

        }
        fetchProductData()
        binding.btnNext.setOnClickListener(createPayment())
        viewModelObserver()

    }

    private fun createPayment(): View.OnClickListener {
     return View.OnClickListener {
         if (resultListener == "RechargeAccountMaster"){
             cretePaymentAccount()
         }else{
             val product =listProduct.last()
             val stripePayment = StripePayment(credentialCard.cardNumber,credentialCard.cardMonth,
                 credentialCard.cardYear,credentialCard.cardCvv,"${product.amount.toFloat().plus(product.amount.toFloat() * 0.05).roundToInt()}")
             stripePaymentViewModel.createPayment(stripePayment)
         }

     }
    }

    private fun cretePaymentAccount() {
        val stripePayment = StripePayment(credentialCard.cardNumber,credentialCard.cardMonth,
            credentialCard.cardYear,credentialCard.cardCvv,
            UtilsView.getValueSharedPreferences(requireActivity(),"amountRecharge")
        )
        stripePaymentViewModel.createPayment(stripePayment)
    }

    private fun manageView(resultListener: String?) =
        if (resultListener == "RechargeAccountMaster"){
            binding.containerPayAgent.isGone = true
            binding.containerMaster.isGone = false
            binding.valueTotalMaster.text = "${UtilsView.getValueSharedPreferences(requireActivity(),"amountRecharge")} USD"
        }else{
            binding.containerPayAgent.isGone = false
            binding.containerMaster.isGone = true
        }

    private fun viewModelObserver() {
        stripePaymentViewModel.responsePaymentStripe.observe(viewLifecycleOwner){responseStripe->
            if (responseStripe.isSuccessful){
                if (responseStripe.body()?.status == "succeeded"){
                    if (resultListener =="RechargeAccountMaster"){
                        createDialogSuccessRechargeAccountMaster(requireActivity())
                        updateSoldTopUpUser()
                    }else{
                        sendTopUpByInnoverit()
                    }
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.aproved))
                        btnNext.isEnabled = false
                    }
                }else{
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.pay))
                    }
                    createDialogErrorPayWithMercadoPago(requireActivity())
                }
            }
        }
        stripePaymentViewModel.isLoading.observe(viewLifecycleOwner) {
            with(binding) {
               showProgress(btnNext,progressBar)
            }
        }
        sendRechargeViewModel.responseInnoVit.observe(viewLifecycleOwner) { result ->
            when {
                result.isSuccess -> {
                    result.getOrNull()?.enqueue(object : Callback<SendRechargeResponse> {
                        override fun onResponse(
                            call: Call<SendRechargeResponse> ,
                            response: Response<SendRechargeResponse>
                        ) {
                            if (response.body()?.status == "success") {
                                createDialogSuccessForAgent(requireActivity())
                                sendDataInFirebase()
                                hideProgress(binding.btnNext,binding.progressBar,getString(R.string.aproved))
                                viewModel.deleteAll()
                                viewModel.deleteProduct()
                            } else {
                                createDialogErrorForAgent(requireActivity())
                                sendDataInFirebaseWhenError(response.body()!!.message)
                                sendDataInFirebaseWhenErrorAgent()
                                viewModel.deleteAll()
                                viewModel.deleteProduct()
                            }
                        }

                        override fun onFailure(call: Call<SendRechargeResponse> , t: Throwable) {}

                    })
                }
                result.isFailure -> {}
            }

        }

    }

    private fun updateSoldTopUpUser() {
        val newSoldTopUp = UtilsView.getValueSharedPreferences(requireActivity(),"totalBalance").toFloat().plus(UtilsView.getValueSharedPreferences(requireActivity(),"amountRecharge").toFloat())
        mUserProvider.updateTopup(mAuthProvider.getId(), newSoldTopUp)?.isSuccessful
        UtilsView.setValueSharedPreferences(requireActivity(),"amountRecharge","")

    }
    private fun sendDataInFirebaseWhenError(message: String) {
        val product = listProduct.last()
        val inputString = "${mAuthProvider.getId()}${product.idProduct}${product.date}"
        val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
        val salesData = Sales(outputString,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,"",
            product.subTotal,message,product.tokenUser,0,product.idProduct,product.soldTopUp.toString(),
            product.imageUrl)
        sendRechargeViewModel.salesWithErrorInnoverit(salesData)
    }

    private fun sendDataInFirebaseWhenErrorAgent() {
        val product = listProduct.last()
        val inputString = "${mAuthProvider.getId()}${product.idProduct}${product.date}"
        val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,product.countryName,
            product.subTotal,"",product.tokenUser,0,product.idProduct,product.soldTopUp.toString(),
            product.imageUrl)
        sendRechargeViewModel.sales(outputString,salesData)
    }

    private fun sendDataInFirebase() {
        val product = listProduct.last()
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,product.countryName,
        product.subTotal,"",product.tokenUser,1,product.idProduct,product.soldTopUp.toString(),
        product.imageUrl)
        val inputString = "${mAuthProvider.getId()}${product.idProduct}${product.date}"
        val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
        sendRechargeViewModel.sales(outputString,salesData)

    }

    private fun sendTopUpByInnoverit() {
        val product = listProduct.last()
        val sendRechargeProduct = SendRechargeProduct(product.idProduct,product.phoneNumber,product.email)
        sendRechargeViewModel.sendRechargeViaInnoVit(sendRechargeProduct)
    }

    private fun fetchProductData() {
       viewModel.getListProduct.observe(viewLifecycleOwner){product->
           if (product.isNotEmpty()){
              product.map {
                  binding.product = it
                  valueFees =it.amount.toFloat() * 0.05F
                  "${it.amount} USD ".also {aL-> binding.valueSubTotal.text = aL }

              }
               listProduct = product
               "${"%.2f".format(valueFees)} USD ".also { binding.valueFee.text = it }
               "${product.last().amount.toFloat().plus("%.2f".format(valueFees).toFloat())} USD ".also { binding.valueTotal.text = it }
           }
       }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProduct()
        viewModel.getAllData()
    }
}