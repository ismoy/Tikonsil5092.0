package com.tikonsil.tikonsil509.ui.fragment.mercadoPago

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.databinding.FragmentInstallmentBinding
import com.tikonsil.tikonsil509.domain.model.CredentialCard
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRechargeProduct
import com.tikonsil.tikonsil509.domain.model.SendRechargeResponse
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceipt
import com.tikonsil.tikonsil509.domain.model.stripePayment.StripePayment
import com.tikonsil.tikonsil509.domain.repository.lastsales.LastSalesRepository
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModel
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModelProvider
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel
import com.tikonsil.tikonsil509.presentation.sendReceipt.SendReceiptViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.presentation.stripePayment.StripePaymentViewModel
import com.tikonsil.tikonsil509.ui.fragment.dialog.DialogConfirm
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.constants.UtilsView.hideProgress
import com.tikonsil.tikonsil509.utils.constants.UtilsView.showProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


@Suppress("IMPLICIT_CAST_TO_ANY")
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
    private  lateinit var mviewmodellastsales:LastSalesViewModel
    private val sendReceiptViewModel by lazy { ViewModelProvider(this)[SendReceiptViewModel::class.java] }
    private  val bottomSheet = DialogConfirm()




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
        val repositorylastsales = LastSalesRepository()
        val factorylastsales = LastSalesViewModelProvider(repositorylastsales)
        mviewmodellastsales = ViewModelProvider(requireActivity(),factorylastsales)[LastSalesViewModel::class.java]

    }

    private fun createPayment(): View.OnClickListener {
     return View.OnClickListener {
         if (resultListener == "RechargeAccountMaster"){
             cretePaymentAccount()
         }else{
             val valueTotal = binding.valueTotal.text.toString()
             val stripePayment = StripePayment(credentialCard.cardNumber,credentialCard.cardMonth,
                 credentialCard.cardYear,credentialCard.cardCvv,UtilsView.extractValue(valueTotal).toString())
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
            "${UtilsView.getValueSharedPreferences(requireActivity(),"amountRecharge")} USD".also { binding.valueTotalMaster.text = it }
        }else{
            binding.containerPayAgent.isGone = false
            binding.containerMaster.isGone = true
        }

    private fun viewModelObserver() {
        stripePaymentViewModel.responsePaymentStripe.observe(viewLifecycleOwner){responseStripe->
            if (responseStripe.isSuccessful){
                if (responseStripe.body()?.status == "succeeded"){
                    if (resultListener =="RechargeAccountMaster"){
                        bottomSheet.show(childFragmentManager,"DialogConfirm")
                        bottomSheet.subtitle = requireActivity().getString(R.string.successrecarcheaccount)
                        bottomSheet.btnCancel = false
                        bottomSheet.btnConfirm = "Confirm"
                        updateSoldTopUpUser()
                    }else{
                        sendTopUpByInnoverit()
                    }
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.aproved))
                        btnNext.isEnabled = false
                    }
                }else{
                    when (responseStripe.body()!!.message) {
                        "Your card has insufficient funds." -> {
                            bottomSheet.show(childFragmentManager,"DialogConfirm")
                            bottomSheet.subtitle = requireActivity().getString(R.string.insifficientfunds)
                            bottomSheet.btnCancel = true
                        }
                        "Your card was declined." -> {
                            bottomSheet.show(childFragmentManager,"DialogConfirm")
                            bottomSheet.subtitle = "Your card was declined."
                            bottomSheet.btnCancel = true
                        }
                        "Your card's security code is incorrect." -> {
                            bottomSheet.show(childFragmentManager,"DialogConfirm")
                            bottomSheet.subtitle = "Your card's security code is incorrect."
                            bottomSheet.btnCancel = true
                        }
                        "Your card has expired." -> {
                            bottomSheet.show(childFragmentManager,"DialogConfirm")
                            bottomSheet.subtitle = "Your card has expired."
                            bottomSheet.btnCancel = true
                        }
                    }
                    Log.e("respuesta",responseStripe.body()!! .message)
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.pay))
                    }
                }
            }else{
                Log.e("respuesta",responseStripe.body().toString())
            }
        /*else if (responseStripe.code() ==500){
                createDialogErrorServer(requireActivity())

            }*/
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
                                bottomSheet.show(childFragmentManager,"DialogConfirm")
                                bottomSheet.subtitle = requireActivity().getString(R.string.success)
                                bottomSheet.btnCancel = false
                                bottomSheet.btnConfirm = "Confirm"
                               // createDialogSuccessForAgent(requireActivity())
                                sendDataInFirebase()
                                hideProgress(binding.btnNext,binding.progressBar,getString(R.string.aproved))
                                viewModel.deleteAll()
                                viewModel.deleteProduct()
                            } else {
                                bottomSheet.show(childFragmentManager,"DialogConfirm")
                                bottomSheet.subtitle = requireActivity(). getString(R.string.errorsendtopupinnoverit)
                                bottomSheet.btnCancel = false
                                bottomSheet.btnConfirm = "Confirm"
                               // createDialogErrorForAgent(requireActivity())
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

        sendRechargeViewModel.myResponseSales.observe(viewLifecycleOwner){
            val product = listProduct.last()
            val inputString = "${mAuthProvider.getId()}${product.idProduct}${product.date}"
            val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
           if (it.isSuccessful){
               val sendReceipt = SendReceipt(outputString)
                sendReceiptViewModel.sendReceipt(sendReceipt)
               sendReceiptViewModel.responseSendReceipt.observe(viewLifecycleOwner){receipt->
                   Log.e("respuesta",receipt?.body()?.status!!)
               }
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
            product.imageUrl,product.soldTopUp.toString(),valueFees.toString(),product.currently)
        sendRechargeViewModel.salesWithErrorInnoverit(salesData)
    }

    private fun sendDataInFirebaseWhenErrorAgent() {
        val product = listProduct.last()
        val inputString = "${mAuthProvider.getId()}${product.idProduct}${product.date}"
        val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,product.countryName,
            product.subTotal,"",product.tokenUser,0,product.idProduct,product.soldTopUp.toString(),
            product.imageUrl,product.soldTopUp.toString(),valueFees.toString(),product.currently)
        sendRechargeViewModel.sales(outputString,salesData)
    }

    private fun sendDataInFirebase() {
        val product = listProduct.last()
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,product.countryName,
        product.subTotal,"",product.tokenUser,1,product.idProduct,"${product.amount.toFloat().plus("%.2f".format(valueFees).toFloat())}" ,
        product.imageUrl,product.amount,"${"%.2f".format(valueFees).toFloat()}",product.currently)
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
                  val resultado = it.amount.toFloat() * 0.029F + 0.30F
                  val redondeado = String.format("%.2f", resultado)
                  valueFees = redondeado.toFloatOrNull()
                  "${it.amount} USD ".also {aL-> binding.valueSubTotal.text = aL }

              }
               listProduct = product
               "$valueFees USD".also { binding.valueFee.text = it }
               "${product.last().amount.toFloat() + valueFees!!.toFloat()} USD ".also { binding.valueTotal.text = it }
           }
       }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProduct()
        viewModel.getAllData()
    }
}