package com.tikonsil.tikonsil509.ui.fragment.sendrecharge

import android.annotation.SuppressLint
import android.app.Dialog
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
import com.tikonsil.tikonsil509.domain.repository.countryprices.CountryPricesRepository
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.countryprices.CountryPricesViewModel
import com.tikonsil.tikonsil509.presentation.countryprices.CountryPricesViewModelFactory
import com.tikonsil.tikonsil509.presentation.fcm.SendNotificationViewModel
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.mercadoPago.MercadoPagoViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModelProvider
import com.tikonsil.tikonsil509.presentation.sendReceipt.SendReceiptViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.ui.fragment.dialog.DialogConfirm
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEHAITI
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI1
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI2
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI3
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI4
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.constants.UtilsView.getValueSharedPreferences
import com.tikonsil.tikonsil509.utils.constants.UtilsView.hideProgress
import com.tikonsil.tikonsil509.utils.constants.UtilsView.readTokenAdminListFromSharedPreferences
import com.tikonsil.tikonsil509.utils.constants.UtilsView.setValueSharedPreferences
import com.tikonsil.tikonsil509.utils.constants.UtilsView.showProgress
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

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
    private var currentlyCountry:String?=null
    private lateinit var mAuthProvider:AuthProvider
    private var totalBalanceTopUpUser:Float =0F
    private  var tokenUser:String?=null
    private var tokenAdmin:ArrayList<String>?=null
    private var imageUser:String?=null
    private var priceMonCashHaiti:Float=0F
    private var priceNatCashHaiti:Float=0F
    private var priceLapoulaHaiti:Float=0F
    private var subTotalSelected:String?=null
    private lateinit var mUserProvider: UserProvider
    private lateinit var mTokensAdminProvider:TokensAdminProvider
    private lateinit var viewmodelsavenotification: SaveNotificationViewModel
    private lateinit var viewModelCountryPrice: CountryPricesViewModel
    private lateinit var dialog:Dialog
    private lateinit var navController: NavController
    private val sendReceiptViewModel by lazy { ViewModelProvider(this)[SendReceiptViewModel::class.java] }
    private  val bottomSheet = DialogConfirm()

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
        val repositoryCountry = CountryPricesRepository()
        val factoryCountry = CountryPricesViewModelFactory(repositoryCountry)
        viewModelCountryPrice = ViewModelProvider(requireActivity(),factoryCountry)[CountryPricesViewModel::class.java]
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

        viewModelObserver()
        initializeComponent()

        binding.recargar.setOnClickListener {sendTopUPRecharge()}
        manageChipGroup()

    }

    private fun calculateMonCash(chipSelected: String) {
       binding.apply {
           UtilsView.calculatePriceServiceHaitiMonCash(total,chipSelected,priceMonCashHaiti, subtotal)
       }
    }

    private fun calculateNatCash(chipSelected: String) {
        binding.apply {
            UtilsView.calculatePriceServiceHaitiNatCash(total,chipSelected,priceNatCashHaiti, subtotal)
        }
    }

    private fun calculateLapouLa(chipSelected: String) {
        binding.apply {
            UtilsView.calculatePriceServiceHaitiLapouLa(total,chipSelected,priceLapoulaHaiti, subtotal)
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
        val calendar: Calendar? = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate:String = sdf.format(calendar!!.time)
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
           salesPriceFee = "0",
           "HTG"
        )
        sendDataInFirebase(salesData,"NatCash")
    }

    private fun sendLapouLa() {
        val calendar: Calendar? = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate:String = sdf.format(calendar!!.time)
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
            salesPriceFee = "0",
            "HTG"
        )
        sendDataInFirebase(salesData,"Lapoula")
    }

    private fun sendMonCash() {
        val calendar: Calendar? = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate:String = sdf.format(calendar!!.time)
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
            salesPriceFee = "0",
            "HTG"
        )
        sendDataInFirebase(salesData,"MonCash")
    }

    private fun viewModelObserver() {
        sendRechargeViewModel.noExistSnapshot.observe(viewLifecycleOwner, Observer {
                binding.layoutdrop.helperText = getString(R.string.no_rechar_for_country)
                binding.layoutdrop.isEnabled = false
        })

        userViewModel.getOnlyUser(mAuthProvider.getId().toString())
        viewModelCountryPrice.getCountryPrice()
        userViewModel.ResposeUsers.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful){
                it.body()?.apply {
                     roleUser =role
                    firstNameUser = firstname
                    lastNameUser = lastname
                    emailUser = email
                    imageUser = image
                    totalBalanceTopUpUser = soltopup
                }
            }
        })

        viewModelCountryPrice.countryPrice.observe(viewLifecycleOwner){country_price->
            if (country_price.isSuccessful){
                country_price.body()?.apply {
                    priceMonCashHaiti = pricemoncashhaiti
                    priceNatCashHaiti = pricenatcashhaiti
                    priceLapoulaHaiti = pricelapoulahaiti
                }
            }
        }

        sendRechargeViewModel.myResponseSales.observe(viewLifecycleOwner){
            if (it.isSuccessful){
                hideProgress(binding.recargar,binding.progressBar,getString(R.string.SendReload))
                if (idProductSelected.isNullOrEmpty()){
                    bottomSheet.show(childFragmentManager,"DialogConfirm")
                    bottomSheet.subtitle =getString(R.string.thanks) + ConstantGeneral.PHONENUMBERWHATSAPP
                    bottomSheet.btnCancel = false
                    bottomSheet.isSalesMaster = true
                    bottomSheet.isNoTopUp = true
                     it.body().let {sale->
                         if (sale != null) {
                             bottomSheet.saleData = sale
                         }
                    }
                    bottomSheet.btnConfirm = "Confirm"
                    createNotification()
                }
            }else{
                hideProgress(binding.recargar,binding.progressBar,getString(R.string.SendReload))
            }
        }

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
        val calendar: Calendar? = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate:String = sdf.format(calendar!!.time)
            showProgress(binding.recargar,binding.progressBar)
            val product = Product(0,"TOPUP",topUpSelected.toString(),emailUser!!,operatorSelected?:"",
                "${binding.codigo.text}${binding.phone.text.toString()}",
                subTotalSelected.toString(),idProductSelected!!.toInt(),mAuthProvider.getId().toString(),
                firstNameUser!!,lastNameUser!!,roleUser!!.toInt(),tokenUser!!,tokenAdmin.toString(), currentDate,0,countrySelected!!,imageUser!!,
                "${"%.2f".format(topUpSelected).toFloat()}",subTotalSelected.toString(),currentlyCountry!!)
            viewModel.insertProduct(product)
            navController.navigate(R.id.action_sendRechargeFragment_to_takeCredentialsCardFormFragment)

    }
    private fun sendDataInFirebase(salesData: Sales,typeSelected:String) {
        showProgress(binding.recargar,binding.progressBar)
        when (typeSelected) {
            "MonCash" -> {
                sendRechargeViewModel.sales("${mAuthProvider.getId()}${typeSelected}",salesData)
            }
            "Lapoula" -> {
                sendRechargeViewModel.sales("${mAuthProvider.getId()}${typeSelected}",salesData)
            }
            "NatCash" -> {
                sendRechargeViewModel.sales("${mAuthProvider.getId()}${typeSelected}",salesData)
            }
            else -> {
                val inputString = "${mAuthProvider.getId()}${idProductSelected}${salesData.date}"
                val outputString = inputString.replace("/", "").replace(" ", "").replace(":", "")
                sendRechargeViewModel.sales(outputString,salesData)
                setValueSharedPreferences(requireActivity(),"idKeySales",outputString)

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
            tokenAdmin.toString()
        ).also {pushNotification ->
            sendNotification(pushNotification)
        }
        Log.e("tokenAdmin",tokenAdmin.toString())
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
            bottomSheet.show(childFragmentManager,"DialogConfirm")
            bottomSheet.subtitle = requireActivity().getString(R.string.nomoney) + requireContext().getString(R.string.yourbalance) + " $ " + totalBalanceTopUpUser
            bottomSheet.btnCancel = true
            bottomSheet.isSalesMasterError = true
        }else{
            createProcessToPay()
        }
    }

    private fun createProcessToPay() {
        val calendar: Calendar? = Calendar.getInstance()
        @SuppressLint("SimpleDateFormat")
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
        val currentDate:String = sdf.format(calendar!!.time)
        val salesDataAuto = Sales(mAuthProvider.getId()!!,firstNameUser,lastNameUser,emailUser,roleUser!!,SERVICEHAITI4,
            "${binding.codigo.text}${binding.phone.text.toString()}",
            currentDate,binding.paises.selectedCountryName,countrySelected,subTotalSelected.toString(),
            binding.description.text.toString(),tokenUser, 1,idProductSelected!!.toInt(),topUpSelected.toString(),imageUser!!,topUpSelected.toString(),
            salesPriceFee = "0",currentlyCountry!!)

        val sendRechargeProduct = SendRechargeProduct(idProductSelected!!.toInt(),"${binding.codigo.text}${binding.phone.text.toString()}",emailUser!!)
         showProgress(binding.recargar,binding.progressBar)
        sendRechargeViewModel.sendRechargeViaInnoVit(sendRechargeProduct)
        sendRechargeViewModel.responseInnoVit.observe(viewLifecycleOwner){result->
            result.enqueue(object :Callback<SendRechargeResponse>{
                override fun onResponse(
                    call: Call<SendRechargeResponse>,
                    response: Response<SendRechargeResponse>
                ) {
                    if (response.isSuccessful){
                        when(response.body()!!.status){
                            "success" -> {
                                bottomSheet.show(childFragmentManager,"DialogConfirm")
                                bottomSheet.subtitle =  getString(R.string.success)
                                bottomSheet.btnCancel = false
                                bottomSheet.btnConfirm = "Confirm"
                                bottomSheet.isSalesMaster = true
                                updateSoldTopUpUser()
                                sendDataInFirebase(salesDataAuto,"TopUp")
                                hideProgress(binding.recargar,binding.progressBar,requireActivity().getString(R.string.SendReload))
                                val  sendReceipt = SendReceipt(getValueSharedPreferences(requireActivity(),"idKeySales"))
                                sendReceiptViewModel.sendReceipt(sendReceipt)
                                bottomSheet.saleData = salesDataAuto
                                bottomSheet.isSuccessGoToReceipt = true
                            }
                            "provider_error" -> {
                                bottomSheet.show(childFragmentManager,"DialogConfirm")
                                bottomSheet.subtitle =  response.body()?.message
                                bottomSheet.btnCancel = true
                                bottomSheet.isDuplicateRecharge = true
                                hideProgress(binding.recargar,binding.progressBar,requireActivity().getString(R.string.SendReload))
                            }
                        }

                    }else{
                            bottomSheet.show(childFragmentManager,"DialogConfirm")
                            bottomSheet.subtitle =  response.body()?.message
                            bottomSheet.btnCancel = true
                            bottomSheet.isDuplicateRecharge = true
                            hideProgress(binding.recargar,binding.progressBar,requireActivity().getString(R.string.SendReload))
                    }
                }

                override fun onFailure(call: Call<SendRechargeResponse>, t: Throwable) {
                    Log.e("erroresss",t.message.toString())
                    Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor" , Toast.LENGTH_SHORT).show()
                }

            })

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
                currentlyCountry = listPrice.last().nameMoneyCountryReceiver
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
        val tokenAdminList = readTokenAdminListFromSharedPreferences(requireContext())
        tokenAdmin = tokenAdminList
        Log.e("llegoTokens",tokenAdmin.toString())
        lifecycleScope.launch {
            UsersDatabase.getDatabase(requireContext()).productDao().deleteAll()
        }

    }
}