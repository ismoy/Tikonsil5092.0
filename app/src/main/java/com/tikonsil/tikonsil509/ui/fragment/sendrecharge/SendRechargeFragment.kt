package com.tikonsil.tikonsil509.ui.fragment.sendrecharge

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.db.UsersDatabase
import com.tikonsil.tikonsil509.data.local.entity.Product
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokensAdminProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.databinding.FragmentSendRechargeBinding
import com.tikonsil.tikonsil509.domain.model.*
import com.tikonsil.tikonsil509.domain.model.sendReceipt.SendReceipt
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.fcm.SendNotificationViewModel
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModelProvider
import com.tikonsil.tikonsil509.presentation.sendReceipt.SendReceiptViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.currentDate
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEHAITI
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI1
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI2
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI3
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI4
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogNoMoney
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogSuccess
import com.tikonsil.tikonsil509.utils.constants.UtilsView.createDialogSuccessManually
import com.tikonsil.tikonsil509.utils.constants.UtilsView.hideProgress
import com.tikonsil.tikonsil509.utils.constants.UtilsView.showProgress
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SendRechargeFragment : Fragment() {
    private lateinit var binding: FragmentSendRechargeBinding
    private lateinit var sendRechargeViewModel:SendRechargeViewModel
    private val userViewModel by lazy { ViewModelProvider(requireActivity())[UserViewModel::class.java] }
    private val viewModel by lazy { ViewModelProvider(requireActivity())[MercadoPagoViewModel::class.java] }
    private val sendNotificationViewModel by lazy { ViewModelProvider(requireActivity())[SendNotificationViewModel::class.java] }
    private var countrySelected: String? = null
    private var chipSelected: String? = null
    private var roleUser:Int?=null
    private var firstNameUser:String?=null
    private var lastNameUser:String?=null
    private var emailUser:String?=null
    private var topUpSelected:Float?=null
    private var idProductSelected:String?=null
    private var operatorSelected:String?=null
    private lateinit var mAuthProvider:AuthProvider
    private var totalBalanceTopUpUser:Float =0F
    private  var tokenUser:String?=null
    private var tokenAdmin:String?=null
    private var imageUser:String?=null
    private var priceMonCash:Float=0F
    private var priceNatCash:Float=0F
    private var priceLapoula:Float=0F
    private var subTotalSelected:Float=0F
    private lateinit var mUserProvider: UserProvider
    private lateinit var mTokensAdminProvider:TokensAdminProvider
    private lateinit var viewmodelsavenotification: SaveNotificationViewModel
    private lateinit var dialog:Dialog
    private lateinit var navController: NavController
    private val sendReceiptViewModel by lazy { ViewModelProvider(this)[SendReceiptViewModel::class.java] }

    override fun onCreateView(
        inflater: LayoutInflater ,
        container: ViewGroup? ,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSendRechargeBinding.inflate(inflater , container , false)
        return binding.root
    }

    override fun onViewCreated(view: View , savedInstanceState: Bundle?) {
        super.onViewCreated(view , savedInstanceState)
        val repository = SendRechargeRepository()
        val factory = SendRechargeViewModelProvider(repository)
        mAuthProvider = AuthProvider()
        mUserProvider = UserProvider()
        dialog = Dialog(requireContext())
        navController = Navigation.findNavController(view)
        mTokensAdminProvider = TokensAdminProvider()
        sendRechargeViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[SendRechargeViewModel::class.java]
        val bottomNavigationView =
            activity!!.findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView.visibility = View.GONE
        val repositorysavenotification = SaveNotificationRepository()
        val factorysavenotification = SaveNotificationViewModelProvider(repositorysavenotification)
        viewmodelsavenotification = ViewModelProvider(
            requireActivity(),
            factorysavenotification
        )[SaveNotificationViewModel::class.java]

        setCountryListener()
        manageChipGroup()
        viewModelObserver()
        initializeComponent()

        binding.recargar.setOnClickListener {sendTopUPRecharge()}
    }

    private fun calculateMonCash(chipSelected: String) {
       binding.apply {
           UtilsView.calculatePriceServiceHaitiMonCash(total,chipSelected,priceMonCash, subtotal)
       }
    }

    private fun calculateNatCash(chipSelected: String) {
        binding.apply {
            UtilsView.calculatePriceServiceHaitiNatCash(total,chipSelected,priceNatCash, subtotal)
        }
    }

    private fun calculateLapouLa(chipSelected: String) {
        binding.apply {
            UtilsView.calculatePriceServiceHaitiLapouLa(total,chipSelected,priceLapoula, subtotal)
        }
    }
    private fun initializeComponent() {
        binding.apply {
            UtilsView.inputValidator(phone,layoutphone,total,layouttotal,recargar,requireContext())
        }

    }

    private fun sendTopUPRecharge() {
        when {
            binding.phone.text.toString().isNotEmpty() && binding.autoCompleteTextView.text.toString().isNotEmpty() -> {
                if (roleUser ==2){verifyAccountBeforeSendTopUp()}else goToPayWithMercadoPago()
            }
            binding.phone.text.toString().isNotEmpty() && chipSelected == SERVICEHAITI1 -> {
                sendMonCash()
            }
            binding.phone.text.toString().isNotEmpty() && chipSelected == SERVICEHAITI2 -> {
                sendLapouLa()
            }
            binding.phone.text.toString().isNotEmpty() && chipSelected == SERVICEHAITI3 -> {
                sendNatCash()
            }
            else -> {
                binding.layoutphone.helperText = getString(R.string.erroremptyfield)
            }
        }
    }

    private fun sendNatCash() {
       val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.text}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!,
            binding.subtotal.text.toString(),
           salesPriceFee = "0"
        )
        sendDataInFirebase(salesData)
    }

    private fun sendLapouLa() {
        val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.text}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!,binding.subtotal.text.toString(),
            salesPriceFee = "0"
        )
        sendDataInFirebase(salesData)
    }

    private fun sendMonCash() {
        val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.text}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!,binding.subtotal.text.toString(),
            salesPriceFee = "0"
        )
        sendDataInFirebase(salesData)
    }

    private fun viewModelObserver() {
        sendRechargeViewModel.noExistSnapshot.observe(viewLifecycleOwner, Observer {
                binding.layoutdrop.helperText = getString(R.string.no_rechar_for_country)
                binding.layoutdrop.isEnabled = false
        })

        userViewModel.getOnlyUser(mAuthProvider.getId().toString())
        userViewModel.ResposeUsers.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful){
                it.body()?.apply {
                     roleUser =role
                    firstNameUser = firstname
                    lastNameUser = lastname
                    emailUser = email
                    imageUser = image
                    totalBalanceTopUpUser = soltopup
                    priceMonCash = soldmoncash
                }
            }
        })
    }

    private fun manageTopUpSelected(valueTopUp: String) {
        if (valueTopUp.isNotEmpty()){
            binding.chipgroup.isGone = true
            binding.containerTV.isGone = true
            binding.recargar.isEnabled = true
        }
    }

    private fun manageChipGroup() {
        binding.chipgroup.setOnCheckedStateChangeListener { group , _ ->
            chipSelected =
                binding.chipgroup.findViewById<Chip>(group.checkedChipId)?.text.toString()
            binding.layoutdrop.isGone =
                chipSelected == SERVICEHAITI1 || chipSelected == SERVICEHAITI2 || chipSelected == SERVICEHAITI3
            calculateMonCash(chipSelected!!)
            calculateNatCash(chipSelected!!)
            calculateLapouLa(chipSelected!!)
        }
    }

    private fun setCountryListener() {
        countrySelected = binding.paises.selectedCountryNameCode
        binding.paises.setOnCountryChangeListener {
            countrySelected = binding.paises.selectedCountryNameCode
            if (countrySelected == CODEHAITI) {
                binding.chipgroup.isGone = false
                binding.containerTV.isGone = false
            }else{
                binding.chipgroup.isGone = true
                binding.containerTV.isGone = true
            }
            sendRechargeViewModel.getIdProductSelected(countrySelected!!)
            sendRechargeViewModel.responseIdProduct.observe(viewLifecycleOwner) {
                binding.layoutdrop.isEnabled = false
                binding.progressBarDrop.isGone = false
                if (it.isNotEmpty()){
                    binding.layoutdrop.isEnabled = true
                    binding.progressBarDrop.isGone = true
                    setUpListTopUpInSpinner(it)
                    binding.layoutdrop.helperText = ""
                }

            }
            binding.codigo.text = binding.paises.selectedCountryCodeWithPlus
        }
    }

    private fun updateSoldTopUpUser() {
        val newSoldTopUp = totalBalanceTopUpUser.minus(topUpSelected!!)
        mUserProvider.updateTopup(mAuthProvider.getId(), newSoldTopUp)?.isSuccessful
    }
    private fun goToPayWithMercadoPago() {
            showProgress(binding.recargar,binding.progressBar)
            val product = Product(0,"TOPUP",topUpSelected.toString(),emailUser!!,operatorSelected?:"",
                "${binding.codigo.text}${binding.phone.text.toString()}",
                subTotalSelected.toString(),idProductSelected!!.toInt(),mAuthProvider.getId().toString(),
                firstNameUser!!,lastNameUser!!,roleUser!!.toInt(),tokenUser!!,tokenAdmin!!, currentDate,0,countrySelected!!,imageUser!!,
                totalBalanceTopUpUser,subTotalSelected.toString(),)
            viewModel.insertProduct(product)
            navController.navigate(R.id.action_sendRechargeFragment_to_takeCredentialsCardFormFragment)

    }
    private fun sendDataInFirebase(salesData: Sales) {
        showProgress(binding.recargar,binding.progressBar)
        sendRechargeViewModel.sales("${mAuthProvider.getId()}${idProductSelected}",salesData)
        sendRechargeViewModel.myResponseSales.observe(viewLifecycleOwner){
            if (it.isSuccessful){
               val  sendReceipt = SendReceipt("${mAuthProvider.getId()}${idProductSelected}")
                sendReceiptViewModel.sendReceipt(sendReceipt)
                hideProgress(binding.recargar,binding.progressBar,getString(R.string.SendReload))
                if (roleUser!=2){
                    createDialogSuccessManually(salesData,requireActivity())
                    createNotification()
                }
            }else{
                hideProgress(binding.recargar,binding.progressBar,getString(R.string.SendReload))
            }
        }
    }

    private fun createNotification() {
        PushNotification(
            NotificationData(
                "NUEVA VENTA",
                "Tienes una nueva venta de $chipSelected por favor ingresa en la aplicación para" +
                        "confirmar la compra",
            ),
            tokenAdmin!!
        ).also {pushNotification ->
            sendNotification(pushNotification)
        }
    }

    private fun sendNotification(pushNotification: PushNotification) {
        sendNotificationViewModel.sendNotification(pushNotification)
        sendNotificationViewModel.sendNotification.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                Log.e("notificated","se ha enviado la notificacion correctamente ${it.body()}")
            }
        }
    }


    private fun verifyAccountBeforeSendTopUp() {
        if (topUpSelected!! >totalBalanceTopUpUser){
            createDialogNoMoney(requireContext(),totalBalanceTopUpUser)
        }else{
            createProcessToPay()
        }
    }

    private fun createProcessToPay() {
        val salesDataAuto = Sales(mAuthProvider.getId()!!,firstNameUser,lastNameUser,emailUser,roleUser!!,SERVICEHAITI4,
            "${binding.codigo.text}${binding.phone.text.toString()}",
            currentDate,binding.paises.selectedCountryName,countrySelected,subTotalSelected.toString(),
            binding.description.text.toString(),tokenUser, 1,idProductSelected!!.toInt(),topUpSelected.toString(),imageUser!!,topUpSelected.toString(),
            salesPriceFee = "0")

        val sendRechargeProduct = SendRechargeProduct(idProductSelected!!.toInt(),"${binding.codigo.text}${binding.phone.text.toString()}",emailUser!!)
        dialog.setContentView(R.layout.dialog_loading)
        dialog.setCancelable(false)
        if (dialog.window!=null){
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        dialog.show()
        sendRechargeViewModel.sendRechargeViaInnoVit(sendRechargeProduct)
        sendRechargeViewModel.responseInnoVit.observe(viewLifecycleOwner){result->
            when{
                result.isSuccess ->{
                    result.getOrNull()?.enqueue(object :Callback<SendRechargeResponse>{
                        override fun onResponse(
                            call: Call<SendRechargeResponse> ,
                            response: Response<SendRechargeResponse>
                        ) {
                            if (response.body()?.status =="success"){
                                createDialogSuccess(salesDataAuto,requireActivity())
                                updateSoldTopUpUser()
                                sendDataInFirebase(salesDataAuto)
                                dialog.dismiss()
                            }else{
                                Toast.makeText(requireContext() , " ${response.body()?.message}" , Toast.LENGTH_LONG).show()
                                startActivity(Intent(requireContext(),HomeActivity::class.java))
                            }
                        }

                        override fun onFailure(call: Call<SendRechargeResponse> , t: Throwable) {}

                    })
                }
                result.isFailure ->{
                    Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor ${result.getOrThrow()}" , Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
            }

        }
    }

    private fun setUpListTopUpInSpinner(listPrice: List<CostInnoverit>?) {
            val newListProductPair = listPrice?.map { Pair(it.formatPrice, it.idProduct) }
            val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item,newListProductPair!!.map { it.first })
            binding.autoCompleteTextView.setAdapter(adapterItems)
            binding.autoCompleteTextView.setOnItemClickListener { _, _, i, _ ->
                val selectedIdProduct = newListProductPair[i].second
                idProductSelected = selectedIdProduct
                takeValueForTopUp(newListProductPair[i].first)
                manageTopUpSelected(newListProductPair[i].first)
                subTotalSelected = UtilsView.extractNumberSubTotal(newListProductPair[i].first)
                val result = newListProductPair[i].first.substringAfterLast(" ")
               operatorSelected = result
            }
    }

    private fun takeValueForTopUp(first: String) {
        val pattern = "([\\d.]+) USD".toRegex()
        val result = pattern.find(first)?.groups?.get(1)?.value
        topUpSelected = result?.toFloat()
    }


    override fun onResume() {
        super.onResume()
       tokenUser = UtilsView.getValueSharedPreferences(requireActivity(),"tokenUsers")
        tokenAdmin = UtilsView.getValueSharedPreferences(requireActivity(),"tokenAdmin")
        lifecycleScope.launch {
            UsersDatabase.getDatabase(requireContext()).productDao().deleteAll()
        }

    }
}