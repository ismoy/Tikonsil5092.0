package com.tikonsil.tikonsil509.ui.fragment.sendrecharge

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstanceFCM
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokenProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokensAdminProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.databinding.FragmentSendRechargeBinding
import com.tikonsil.tikonsil509.domain.model.*
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModelProvider
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.activity.invoice.InvoiceActivity
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.currentDate
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEHAITI
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEGENERAL
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI1
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI2
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI3
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI4
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class SendRechargeFragment : Fragment() {
    private lateinit var binding: FragmentSendRechargeBinding
    private lateinit var sendRechargeViewModel:SendRechargeViewModel
    private val userViewModel by lazy { ViewModelProvider(requireActivity())[UserViewModel::class.java] }
    private var countrySelected: String? = null
    private var chipSelected: String? = null
    private var roleUser:Int?=null
    private var firstNameUser:String?=null
    private var lastNameUser:String?=null
    private var emailUser:String?=null
    private var topUpSelected:Float?=null
    private var idProductSelected:String?=null
    private lateinit var mAuthProvider:AuthProvider
    private lateinit var userTokenProvider:TokenProvider
    private var totalBalanceTopUpUser:Float =0F
    private var tokenUser:String?=null
    private var imageUser:String?=null
    private var priceMonCash:Float=0F
    private var priceNatCash:Float=0F
    private var priceLapoula:Float=0F
    private var subTotalSelected:Float=0F
    private lateinit var mUserProvider: UserProvider
    private lateinit var mTokensAdminProvider:TokensAdminProvider
    private lateinit var viewmodelsavenotification: SaveNotificationViewModel
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
        userTokenProvider = TokenProvider()
        mUserProvider = UserProvider()
        mTokensAdminProvider = TokensAdminProvider()
        sendRechargeViewModel = ViewModelProvider(
            requireActivity(),
            factory
        )[SendRechargeViewModel::class.java]
        val repositorysavenotification = SaveNotificationRepository()
        val factorysavenotification = SaveNotificationViewModelProvider(repositorysavenotification)
        viewmodelsavenotification = ViewModelProvider(
            requireActivity(),
            factorysavenotification
        )[SaveNotificationViewModel::class.java]
        setCountryListener()
        manageChipGroup()
        viewModelObserver()
        getTokenUser()
        initializeComponent()
        sendTopUPRecharge()
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
        binding.recargar.setOnClickListener {
            when {
                binding.phone.text.toString().isNotEmpty() && binding.autoCompleteTextView.text.toString().isNotEmpty() -> {
                    sendTopUp()
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


    }

    private fun sendNatCash() {
       val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!
        )
        sendDataInFirebase(salesData,16)
    }

    private fun sendLapouLa() {
        val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!
        )
        sendDataInFirebase(salesData,15)
    }

    private fun sendMonCash() {
        val  salesData= Sales(
            mAuthProvider.getId()!!,
            firstNameUser ,
            lastNameUser ,
            emailUser ,
            roleUser!!,
            chipSelected ,
            "${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}",
            currentDate,
            binding.paises.selectedCountryName,
            countrySelected,
            binding.subtotal.text.toString(),
            binding.description.text.toString(),
            tokenUser,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            imageUser!!
        )
        sendDataInFirebase(salesData,14)
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
                if (it.isNotEmpty()){
                    binding.layoutdrop.isEnabled = true
                    setUpListTopUpInSpinner(it)
                    binding.layoutdrop.helperText = ""
                }

            }
        }

    }

    private fun sendTopUp() {
        if (roleUser==2 && binding.autoCompleteTextView.text.toString().isNotEmpty()){
            sendTopUpAutomatically()
        }else{
            sendTopUpManually()
        }
    }

    private fun sendTopUpManually() {
        val salesData = Sales(mAuthProvider.getId()!!,firstNameUser,lastNameUser,emailUser,roleUser!!,SERVICEHAITI4,
            "${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}",
            Constant.currentDate,binding.paises.selectedCountryName,countrySelected,subTotalSelected.toString(),
            binding.description.text.toString(),tokenUser, 0,idProductSelected!!.toInt(),topUpSelected.toString(),imageUser!!)

        sendTopUpPendingVerificationByAdmin(salesData)
    }

    private fun sendTopUpPendingVerificationByAdmin(salesData: Sales) {
        sendDataInFirebase(salesData,10)
    }

    private fun sendTopUpAutomatically() {
        val salesDataAuto = Sales(mAuthProvider.getId()!!,firstNameUser,lastNameUser,emailUser,roleUser!!,SERVICEHAITI4,
        "${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}",
            currentDate,binding.paises.selectedCountryName,countrySelected,subTotalSelected.toString(),
        binding.description.text.toString(),tokenUser, 1,idProductSelected!!.toInt(),topUpSelected.toString(),imageUser!!)

        verifyAccountBeforeSendTopUp(salesDataAuto)

    }

    private fun verifyAccountBeforeSendTopUp(salesData: Sales) {
        if (topUpSelected!! >totalBalanceTopUpUser){
            createDialogNoMoney()
        }else{
            createProcessToPay(salesData)
            sendRechargeViewModel.sales(salesData)
        }
    }

    private fun createProcessToPay(salesData: Sales) {
        sendRechargeViewModel.sendRechargeViaInnoVit(idProductSelected!!,"${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}")
        sendRechargeViewModel.responseInnoVit.observe(viewLifecycleOwner){call->
            call.enqueue(object :Callback<SendRecharge>{
                override fun onResponse(
                    call: Call<SendRecharge> ,
                    response: Response<SendRecharge>
                ) {
                   if (response.isSuccessful){
                       try {
                           if (response.body()?.status =="success"){
                               sendDataInFirebase(salesData , 11)
                               createDialogSuccess(salesData)
                               updateSoldTopUpUser()
                           }else{
                               Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor ${response.body()?.status}" , Toast.LENGTH_SHORT).show()
                               Log.d("responseApi",response.body().toString())
                           }
                       }catch (ex: IOException){
                           Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor $ex" , Toast.LENGTH_SHORT).show()
                       }
                   }else{
                       Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor ${response.body()?.status}" , Toast.LENGTH_SHORT).show()
                   }
                }

                override fun onFailure(call: Call<SendRecharge> , t: Throwable) {
                    Toast.makeText(requireContext() , "No fue posible realizar la recarga pon en contacto con su proveedor $t" , Toast.LENGTH_SHORT).show()

                }

            })
        }
    }

    private fun updateSoldTopUpUser() {
        val newSoldTopUp = totalBalanceTopUpUser.minus(topUpSelected!!)
        mUserProvider.updateTopup(mAuthProvider.getId(), newSoldTopUp)?.isSuccessful
    }

    private fun sendDataInFirebase(salesData: Sales , sendMode: Int?) {
        sendRechargeViewModel.sales(salesData)
        sendRechargeViewModel.myResponseSales.observe(viewLifecycleOwner){
            if (it.isSuccessful){
               sendNotificationToOtherDevice()
                saveNotification()
                if (sendMode==10 || sendMode==14 || sendMode==15 || sendMode==16){
                    createDialogSuccessManually(salesData)
                }
            }
        }
    }

    private fun createDialogSuccessManually(salesData: Sales) {
        val view = View.inflate(requireContext(), R.layout.dialog_success, null)
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = getString( R.string.thanks ) + ConstantGeneral.PHONENUMBERWHATSAPP
        button.setOnClickListener {
           sendWhatsapp(salesData)
            dialog.dismiss()
            requireActivity().finish()
        }
    }

    private fun sendWhatsapp(salesData: Sales) {
        try {
            val mensaje = getString(R.string.extract_whatsapp ,"${salesData.phone} Name: ${salesData.firstname}"  +
                    " Total TopUp ${salesData.subtotal} Country: ${salesData.country}")
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse("whatsapp://send?phone=${ConstantGeneral.PHONENUMBERWHATSAPP}&text=$mensaje")
            startActivity(i)
        } catch (e: ActivityNotFoundException) {
            // WhatsApp is not installed on the device. Prompt the user to install it.
            Toast.makeText(requireActivity(), "WhatsApp no está instalado en este dispositivo", Toast.LENGTH_SHORT).show()
        }
    }


    private fun createDialogNoMoney() {
        val view = View.inflate(requireContext() , R.layout.dialognomoney , null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val message = view.findViewById<TextView>(R.id.messagenomoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text =
            getString(R.string.nomoney) + getString(R.string.yourbalance ) + " $ " + totalBalanceTopUpUser
        button.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun createDialogSuccess(salesData: Sales) {
        val view = View.inflate(requireContext() , R.layout.dialog_success , null)
        val builder = AlertDialog.Builder(requireContext())
        builder.setView(view)
        val dialog = builder.create()
        dialog.show()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        val message = view.findViewById<TextView>(R.id.messagehasmoney)
        val button = view.findViewById<Button>(R.id.confirm)
        message.text = getString(R.string.success)
        button.setOnClickListener {
            val intent = Intent(requireContext(), InvoiceActivity::class.java)
            intent.putExtra("saleData",salesData)
            startActivity(intent)
            requireActivity().finish()
            dialog.dismiss()
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
                subTotalSelected = extractNumberSubTotal(newListProductPair[i].first)

            }

    }

    private fun takeValueForTopUp(first: String) {
        val pattern = "([\\d.]+) USD".toRegex()
        val result = pattern.find(first)?.groups?.get(1)?.value
        topUpSelected = result?.toFloat()
    }

    private fun extractNumberSubTotal(input: String): Float {
        val regex = "([\\d.]+)".toRegex()
        val match = regex.find(input)
        return match?.value?.toFloat() ?:0.0F
    }


    private fun getTokenUser() {
        userTokenProvider.getToken(mAuthProvider.getId().toString()).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    tokenUser=snapshot.child("token").value.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
    private fun sendNotificationToOtherDevice() {
        mTokensAdminProvider.getToken()
            ?.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (ds in snapshot.children) {
                            val token: String = ds.child("token").value.toString()
                            val role = when (roleUser) {
                                1 -> {
                                    getString(R.string.agent)
                                }
                                else -> {
                                    getString(R.string.master)

                                }
                            }
                            PushNotification(
                                NotificationData(
                                    ConstanteMessage.TITLENOTIFICATION ,
                                    "${ConstanteMessage.EL} $role  $firstNameUser ${ConstanteMessage.MESSAGENOTIFICATION} "
                                ) ,
                                token
                            ).also { push ->
                                sendNotification(push)

                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstanceFCM.notificationAPI.postNotification(notification)
                if (!response.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Error: ${response.errorBody().toString()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
            }
        }
    private fun saveNotification() {
        val role = when (roleUser) {
            1 -> {
                getString(R.string.agent)
            }
            else -> {
                getString(R.string.master)

            }
        }
        val saveData = SaveNotification(
            mAuthProvider.getId().toString(),
            ConstanteMessage.TITLENOTIFICATION , "${ConstanteMessage.EL} $role  $firstNameUser ${ConstanteMessage.MESSAGENOTIFICATION} $SERVICEGENERAL ${ConstanteMessage.MESSAGETOTALNOTIFICATION} $topUpSelected" +
                    " ${ConstanteMessage.FOR} ${binding.paises.selectedCountryName} ",
            currentDate ,"${binding.codigo.selectedCountryCodeWithPlus}${binding.phone.text.toString()}"
        )
        viewmodelsavenotification.saveNotification(saveData)
        viewmodelsavenotification.myResponsesavenotification.observe(
            viewLifecycleOwner
        ) { _ -> }

    }

}