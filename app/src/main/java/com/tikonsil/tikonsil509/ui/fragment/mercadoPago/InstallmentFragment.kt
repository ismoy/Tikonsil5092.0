package com.tikonsil.tikonsil509.ui.fragment.mercadoPago

import android.os.Bundle
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
import com.tikonsil.tikonsil509.domain.model.Sales
import com.tikonsil.tikonsil509.domain.model.SendRechargeProduct
import com.tikonsil.tikonsil509.domain.model.SendRechargeResponse
import com.tikonsil.tikonsil509.domain.model.mercadoPago.MercadoPagoInstallments
import com.tikonsil.tikonsil509.domain.model.mercadoPago.Payment
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
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


class InstallmentFragment : Fragment() {

    private lateinit var binding:FragmentInstallmentBinding
    private val viewModel by lazy { ViewModelProvider(this)[MercadoPagoViewModel::class.java] }
    private lateinit var navController: NavController
    private lateinit var mUserProvider: UserProvider
    private lateinit var sendRechargeViewModel: SendRechargeViewModel
    private lateinit var  listProduct:List<Product>
    private lateinit var mAuthProvider:AuthProvider
    private var resultListener:String? =null
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
        viewModelObserver()

    }

    private fun manageView(resultListener: String?) {
        if (resultListener == "RechargeAccountMaster"){
            binding.containerPayAgent.isGone = true
            binding.containerMaster.isGone = false
            binding.valueTotalMaster.text = UtilsView.getValueSharedPreferences(requireActivity(),"amountRecharge")
        }else{
            binding.containerPayAgent.isGone = false
            binding.containerMaster.isGone = true
        }

    }

    private fun viewModelObserver() {
        viewModel.createPaymentMercadoPago.observe(viewLifecycleOwner) {
            if (it.isSuccessful) {
               val status =  it?.body()?.get("status")?.asString.toString()
                if (status == "approved"){
                   if (resultListener =="RechargeAccountMaster"){
                       createDialogSuccessRechargeAccountMaster(requireActivity())
                       updateSoldTopUpUser()
                   }else{
                       sendTopUpByInnoverit()
                   }
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.aproved))
                    }
                }else{
                    with(binding) {
                        hideProgress(btnNext,progressBar,getString(R.string.pay))
                    }
                    createDialogErrorPayWithMercadoPago(requireActivity())
                }
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
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
                            } else {
                                createDialogErrorForAgent(requireActivity())
                                sendDataInFirebaseWhenError(response.body()!!.message)
                                sendDataInFirebaseWhenErrorAgent()
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
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,"",
            product.subTotal,message,product.tokenUser,0,product.idProduct,product.soldTopUp.toString(),
            product.imageUrl)
        sendRechargeViewModel.salesWithErrorInnoverit(salesData)
    }

    private fun sendDataInFirebaseWhenErrorAgent() {
        val product = listProduct.last()
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,"",
            product.subTotal,"",product.tokenUser,0,product.idProduct,product.soldTopUp.toString(),
            product.imageUrl)
        sendRechargeViewModel.sales(salesData)
    }

    private fun sendDataInFirebase() {
        val product = listProduct.last()
        val salesData = Sales(mAuthProvider.getId()!!,product.firstName,product.lastName,product.email,product.role,
            ConstantServiceCountry.SERVICEHAITI4,product.phoneNumber,product.date,product.countryName,"",
        product.subTotal,"",product.tokenUser,1,product.idProduct,product.soldTopUp.toString(),
        product.imageUrl)
        sendRechargeViewModel.sales(salesData)
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
                  fetchCredentialData(it.amount)

              }
               listProduct = product
           }
       }
    }

    private fun fetchCredentialData(amount: String) {
        viewModel.getAllData.observe(viewLifecycleOwner){credential->
            credential.map {
                getInstallment(it.first_six_digits,amount,it.card_token)

            }
        }
    }

    private fun getInstallment(firstSixDigits: String , total: String , cardToken: String) {
        viewModel.getInstallment(firstSixDigits,total)
        viewModel.getInstallment.observe(viewLifecycleOwner){response->
            if (response.isSuccessful){
               val  paymentMethodId =
                    response.body()?.get(0)?.asJsonObject?.get("payment_method_id")?.asString.toString()
                val paymentTypeId: String = response.body()
                    ?.get(0)?.asJsonObject?.get("issuer")?.asJsonObject?.get("id")?.asString.toString()
                val jsonInstallments =
                    response.body()!!.get(0).asJsonObject.get("payer_costs").asJsonArray
                val type = object : TypeToken<ArrayList<MercadoPagoInstallments>>() {}.type
                val gson = Gson()
                val installments =
                    gson.fromJson<ArrayList<MercadoPagoInstallments>>(jsonInstallments, type)
                setUpDropList(installments,paymentMethodId,paymentTypeId,total,cardToken)
            }
        }
    }

    private fun setUpDropList(
        installments: ArrayList<MercadoPagoInstallments>? ,
        paymentMethodId: String ,
        paymentTypeId: String ,
        total: String ,
        cardToken: String
    ) {
        val  adapterItems = ArrayAdapter(requireContext(), R.layout.dropdowm_item,installments!!)
        binding.autoCompleteTextView.setAdapter(adapterItems)
        binding.autoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            val  installment = installments[position].installments
            binding.valueSubTotal.text = installments[position].recommendedMessage
            binding.valueSubTotalMaster.text = installments[position].recommendedMessage
            createPayment(paymentMethodId,paymentTypeId,total,cardToken,installment)
            binding.btnNext.isEnabled = true
            binding.btnNext.setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.colorboton))
        }
    }

    private fun createPayment(
        paymentMethodId: String ,
        paymentTypeId: String ,
        total: String ,
        cardToken: String ,
        installment: Int
    ) {
        val product =listProduct.last()
        binding.btnNext.setOnClickListener {
            val payment =Payment(total,cardToken,installment,paymentMethodId,paymentTypeId.toInt(),product.email)
            viewModel.createPayment(payment)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getProduct()
        viewModel.getAllData()
    }
}