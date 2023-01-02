package com.tikonsil.tikonsil509.ui.fragment.sendrecharge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isNotEmpty
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.hbb20.CountryCodePicker
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.local.db.UsersEntity
import com.tikonsil.tikonsil509.data.remote.api.RetrofitInstanceFCM
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokenProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokensAdminProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.domain.JavaMailApi.JavaMailAPI
import com.tikonsil.tikonsil509.domain.model.*
import com.tikonsil.tikonsil509.domain.repository.FCM.SendNotificationRepository
import com.tikonsil.tikonsil509.domain.repository.countryprices.CountryPricesRepository
import com.tikonsil.tikonsil509.domain.repository.savenotification.SaveNotificationRepository
import com.tikonsil.tikonsil509.domain.repository.sendrecharge.SendRechargeRepository
import com.tikonsil.tikonsil509.presentation.FCM.SendNotificationViewModel
import com.tikonsil.tikonsil509.presentation.FCM.SendNotificationViewModelFactory
import com.tikonsil.tikonsil509.presentation.countryprices.CountryPricesViewModel
import com.tikonsil.tikonsil509.presentation.countryprices.CountryPricesViewModelFactory
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModel
import com.tikonsil.tikonsil509.presentation.savenotification.SaveNotificationViewModelProvider
import com.tikonsil.tikonsil509.presentation.saveusersroom.UsersRoomViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModel
import com.tikonsil.tikonsil509.presentation.sendrecharge.SendRechargeViewModelProvider
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.activity.invoice.InvoiceActivity
import com.tikonsil.tikonsil509.utils.constants.Constant
import com.tikonsil.tikonsil509.utils.constants.Constant.Companion.SUBJECT
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEBRAZIL
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODECHILE
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODECUBA
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEHAITI
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEMEXICO
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEPANAMA
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEREPUBLICANDOMINIK
import com.tikonsil.tikonsil509.utils.constants.ConstantCodeCountry.CODEUSA
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEGENERAL
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI1
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI2
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI3
import com.tikonsil.tikonsil509.utils.constants.ConstantServiceCountry.SERVICEHAITI4
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage.EL
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage.FOR
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage.MESSAGENOTIFICATION
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage.MESSAGETOTALNOTIFICATION
import com.tikonsil.tikonsil509.utils.constants.ConstanteMessage.TITLENOTIFICATION
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.roundToInt

/** * Created by ISMOY BELIZAIRE on 28/04/2022. */
@Suppress("DEPRECATION")
abstract class ValidateFormSales<VB : ViewBinding, VM : ViewModel> : Fragment() {
    protected lateinit var binding: VB
    protected lateinit var viewmodel: SendRechargeViewModel
    private lateinit var countrypricesviewmodel: CountryPricesViewModel
    private lateinit var sendNotificationViewModel: SendNotificationViewModel
    protected lateinit var mAuthProvider: AuthProvider
    protected lateinit var mConstant: Constant
    protected var selectedcountry: CountryCodePicker? = null
    private var areaCodeCountry: CountryCodePicker? = null
    private var typeselected: ChipGroup? = null
    protected var TOTAL: TextInputEditText? = null
    protected var PHONES: TextInputEditText? = null
    protected var SUBTOTAL: TextInputEditText? = null
    protected var DESCRIPTION: TextInputEditText? = null
    protected var BTN_RECARGA: Button? = null
    protected var LAYOUPHONE: TextInputLayout? = null
    protected var LAYOUTTOTAL: TextInputLayout? = null
    protected var LAYOUTDESCRIPTION: TextInputLayout? = null
    private var CHIPMONCASH:Chip?=null
    private var listTopUp:TextInputLayout?=null
    private var CHIPNATCASH:Chip?=null
    private var CHIPLAPOULA:Chip?=null
    var containerTV:ConstraintLayout?=null
    private lateinit var mUserProvider: UserProvider
    private val roomviewmodel by lazy { ViewModelProvider(this)[UsersRoomViewModel::class.java] }
    private val userviewmodel by lazy { ViewModelProvider(requireActivity())[UserViewModel::class.java] }
    lateinit var dialog: Dialog
    private lateinit var mTokensAdminProvider: TokensAdminProvider
    private lateinit var viewmodelsavenotification: SaveNotificationViewModel
    private var errorchip:TextView?=null
    private lateinit var userTokenProvider: TokenProvider
    private var tokenuser:String?=null
    private var idProductSelected:Int?=null
    private var salesPriceSelected:String?=null
    protected lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        val repository = SendRechargeRepository()
        val factory = SendRechargeViewModelProvider(repository)
        viewmodel = ViewModelProvider(
            requireActivity(),
            factory
        )[SendRechargeViewModel::class.java]
        val repositorysendnotification = SendNotificationRepository()
        val factorysendnotification = SendNotificationViewModelFactory(repositorysendnotification)
        sendNotificationViewModel = ViewModelProvider(
            requireActivity(),
            factorysendnotification
        )[SendNotificationViewModel::class.java]
        val repositorycountry = CountryPricesRepository()
        val factorycountry = CountryPricesViewModelFactory(repositorycountry)
        countrypricesviewmodel =
            ViewModelProvider(requireActivity(), factorycountry)[CountryPricesViewModel::class.java]
        val repositorysavenotification = SaveNotificationRepository()
        val factorysavenotification = SaveNotificationViewModelProvider(repositorysavenotification)
        viewmodelsavenotification = ViewModelProvider(
            requireActivity(),
            factorysavenotification
        )[SaveNotificationViewModel::class.java]
        mAuthProvider = AuthProvider()
        mConstant = Constant()
        dialog = Dialog(requireContext())
        selectedcountry = binding.root.findViewById(R.id.paises)
        typeselected = binding.root.findViewById(R.id.chipgroup)
        mUserProvider = UserProvider()
        PHONES = binding.root.findViewById(R.id.phone)
        SUBTOTAL = binding.root.findViewById(R.id.subtotal)
        DESCRIPTION = binding.root.findViewById(R.id.description)
        TOTAL = binding.root.findViewById(R.id.total)
        BTN_RECARGA = binding.root.findViewById(R.id.recargar)
        LAYOUPHONE = binding.root.findViewById(R.id.layoutphone)
        LAYOUTTOTAL = binding.root.findViewById(R.id.layouttotal)
        LAYOUTDESCRIPTION = binding.root.findViewById(R.id.layoutdescription)
        CHIPMONCASH =binding.root.findViewById(R.id.moncash)
        CHIPNATCASH =binding.root.findViewById(R.id.natcash)
        CHIPLAPOULA = binding.root.findViewById(R.id.lapula)
        mTokensAdminProvider = TokensAdminProvider()
        errorchip=binding.root.findViewById(R.id.errorchip)
        userTokenProvider = TokenProvider()
        listTopUp=binding.root.findViewById(R.id.layoutdrop)
        containerTV = binding.root.findViewById(R.id.containerTV)
        areaCodeCountry =binding.root.findViewById(R.id.codigo)
        observeViewModel()
        tokenUser()
        return binding.root
    }

    private fun observeViewModel() {
        viewmodel.noExistSnapshot.observe(viewLifecycleOwner, Observer {
            if (it){
                listTopUp?.helperText = getString(R.string.no_rechar_for_country)
                val listClear = mutableListOf<CostInnoverit>()
                setUpDataInSpinner(listClear)
            }
        })
    }


    private fun setUpDataInSpinner(listPrice: List<CostInnoverit>?) {
        val  adapterItems = ArrayAdapter(requireContext(),R.layout.dropdowm_item,listPrice!!)
        val idAutoComplete =binding.root.findViewById<AutoCompleteTextView>(R.id.autoCompleteTextView)

        idAutoComplete.setAdapter(adapterItems)
        idAutoComplete.setOnItemClickListener { adapterView, view, i, l ->
            val selecteditem = adapterView.getItemAtPosition(i)
            salesPriceSelected=selecteditem.toString()
            viewmodel.getIdProductSelected(selecteditem.toString())
            viewmodel.responseIdProduct.observe(viewLifecycleOwner, Observer {
               idProductSelected=it.toInt()
            })
            containerTV?.isVisible =false
            typeselected?.isVisible =false

        }

    }

    private fun tokenUser() {
        userTokenProvider.getToken(mAuthProvider.getId().toString()).addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    tokenuser=snapshot.child("token").value.toString()

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun validateitemrealtime() {
        PAIS = selectedcountry?.selectedCountryNameCode.toString()

        selectedcountry?.setOnCountryChangeListener {
            selectedListener = selectedcountry!!.selectedCountryNameCode.toString()
            if (selectedListener == CODEHAITI || selectedListener == CODECHILE || selectedListener == CODEREPUBLICANDOMINIK || selectedListener == CODEUSA ||
                selectedListener == CODEPANAMA || selectedListener == CODECUBA || selectedListener == CODEBRAZIL || selectedListener == CODEMEXICO
            ) {
                selectedCountry()
                BTN_RECARGA?.isEnabled = true

            } else {
                BTN_RECARGA?.isEnabled = false
            }
            viewmodel.getAllPriceCost(selectedcountry!!.selectedCountryNameCode.toString())
            viewmodel.responseGetAllPrice.observe(viewLifecycleOwner, Observer { listPrice->
                if (listPrice.isNotEmpty()){
                    listTopUp?.helperText =""
                    setUpDataInSpinner(listPrice)
                    Log.d("listPrice",listPrice.toString())
                }
            })
        }
        selectedCountryonly()
        PHONES?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("CutPasteId")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    PHONES?.text.toString().isEmpty() -> {
                        LAYOUPHONE?.helperText =
                            getString(R.string.erroremptyfield)
                        BTN_RECARGA?.isEnabled = false
                    }
                    !mConstant.validatelenghnumberphone(PHONES?.text.toString()) -> {
                        LAYOUPHONE?.helperText =
                            getString(R.string.error_longitud_number)
                        BTN_RECARGA?.isEnabled = false
                    }
                    else -> {
                        LAYOUPHONE?.hint = PHONES?.text.toString()
                        LAYOUPHONE?.helperText = ""
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

        TOTAL?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    TOTAL?.text.toString().isEmpty() -> {
                        LAYOUTTOTAL?.helperText =
                            getString(R.string.erroremptyfield)
                        BTN_RECARGA?.isEnabled = false
                    }
                    else -> {
                        LAYOUTTOTAL?.helperText = ""
                        BTN_RECARGA?.isEnabled = true


                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        DESCRIPTION?.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                when {
                    DESCRIPTION?.text.toString().isEmpty() -> {
                        LAYOUTDESCRIPTION?.helperText =
                            getString(R.string.erroremptyfield)
                        BTN_RECARGA?.isEnabled = false
                    }
                    else -> {
                        LAYOUTDESCRIPTION?.helperText = ""
                        BTN_RECARGA?.isEnabled = true
                    }
                }
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })

    }

    @SuppressLint("CutPasteId")
    private fun selectedCountryonly() {
        when {
            PAIS == CODEHAITI -> {
                CHIPMONCASH?.isEnabled = true
                CHIPMONCASH?.isVisible = true
                CHIPLAPOULA?.isVisible = true
                CHIPLAPOULA?.isEnabled = true
                CHIPNATCASH?.isVisible = true
                CHIPNATCASH?.isEnabled = true
                listTopUp?.isVisible = true

            }
            PAIS != CODEHAITI -> {
                CHIPMONCASH?.isVisible = false
                CHIPLAPOULA?.isVisible = false
                CHIPNATCASH?.isVisible = false

            }
            PAIS == CODEHAITI && BALANCEMONCASH == 0 -> {
                CHIPMONCASH?.isVisible = false
                CHIPNATCASH?.isEnabled = true
                CHIPLAPOULA?.isEnabled = true
            }
            PAIS == CODEHAITI && BALANCETOPUP == 0 -> {
                CHIPMONCASH?.isEnabled = true
                CHIPNATCASH?.isEnabled = true
                CHIPLAPOULA?.isEnabled = true
            }
            PAIS == CODEHAITI && BALANCELAPOULA == 0 -> {
                CHIPLAPOULA?.isVisible = false
                CHIPMONCASH?.isEnabled = true
                CHIPNATCASH?.isEnabled = true
            }
            PAIS == CODEHAITI && BALANCENATCASH == 0 -> {
                CHIPNATCASH?.isVisible = false
                CHIPMONCASH?.isEnabled = true
                CHIPLAPOULA?.isEnabled = true
            }
            else -> {
                CHIPMONCASH?.isVisible = false
                CHIPLAPOULA?.isVisible = false
                CHIPNATCASH?.isVisible = false
                listTopUp?.isVisible = true
            }
        }

        typeselected?.setOnCheckedStateChangeListener { group, checkedId ->
            chip1 = typeselected?.findViewById<Chip>(group.checkedChipId)?.text.toString()
            when {
                PAIS == CODEHAITI && chip1 == SERVICEHAITI1 -> {
                    calculatemoncash()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                PAIS == CODEHAITI && chip1 == SERVICEHAITI2 -> {
                    calculatelapoula()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                PAIS == CODEHAITI && chip1 == SERVICEHAITI3 -> {
                    calculatenatcash()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                PAIS == CODEHAITI && chip1 == SERVICEHAITI4 -> {
                   // calculatetopup()
                }
                PAIS == CODECUBA && chip1 == SERVICEGENERAL -> {
                   // calculatetopupcuba()
                }
                PAIS == CODEMEXICO && chip1 == SERVICEGENERAL -> {
                   // calculatetopupmexico()
                }
                PAIS == CODECHILE && chip1 == SERVICEGENERAL -> {
                  //  calculatetopupchile()
                }
                PAIS == CODEPANAMA && chip1 == SERVICEGENERAL -> {
                   // calculatetopuppanama()
                }
                PAIS == CODEBRAZIL && chip1 == SERVICEGENERAL -> {
                   // calculatetopupbrazil()
                }
                PAIS == CODEREPUBLICANDOMINIK && chip1 == SERVICEGENERAL -> {
                   // calculatetopupdominicana()
                }
                PAIS == CODEUSA && chip1 == SERVICEGENERAL -> {
                   // calculatetopupestadosunidos()
                }
            }


        }

    }

    @SuppressLint("CutPasteId")
    private fun selectedCountry() {
        when {
            selectedListener == CODEHAITI -> {
                CHIPMONCASH?.isEnabled = true
                CHIPMONCASH?.isVisible = true
                CHIPLAPOULA?.isEnabled = true
                CHIPLAPOULA?.isVisible = true
                CHIPNATCASH?.isEnabled = true
                CHIPNATCASH?.isVisible = true
                listTopUp?.isVisible = true
            }
            PAIS != CODEHAITI -> {
                CHIPMONCASH?.isVisible = false
                CHIPNATCASH?.isVisible = false
                CHIPLAPOULA?.isVisible = false
            }
            else -> {
                CHIPLAPOULA?.isEnabled = true
                CHIPMONCASH?.isVisible = false
                CHIPLAPOULA?.isVisible = false
                CHIPNATCASH?.isVisible = false
                listTopUp?.isVisible = true
            }
        }
        typeselected?.setOnCheckedStateChangeListener { group, checkedId ->
            chip1 = typeselected?.findViewById<Chip>(group.checkedChipId)?.text.toString()
            when {
                selectedListener == CODEHAITI && chip1 == SERVICEHAITI1 -> {
                    calculatemoncash()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                selectedListener == CODEHAITI && chip1 == SERVICEHAITI4 -> {
                   // calculatetopup()
                }
                selectedListener == CODECUBA && chip1 == SERVICEGENERAL -> {
                    //calculatetopupcuba()
                }
                selectedListener == CODEHAITI && chip1 == SERVICEHAITI2 -> {
                    calculatelapoula()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                selectedListener == CODEHAITI && chip1 == SERVICEHAITI3 -> {
                    calculatenatcash()
                    containerTV?.isVisible =true
                    listTopUp?.isVisible =false
                }
                selectedListener == CODEMEXICO && chip1 == SERVICEGENERAL -> {
                    //calculatetopupmexico()
                }
                selectedListener == CODECHILE && chip1 == SERVICEGENERAL -> {
                    //calculatetopupchile()
                }
                selectedListener == CODEPANAMA && chip1 == SERVICEGENERAL -> {
                   // calculatetopuppanama()
                }
                selectedListener == CODEBRAZIL && chip1 == SERVICEGENERAL -> {
                   // calculatetopupbrazil()
                }
                selectedListener == CODEREPUBLICANDOMINIK && chip1 == SERVICEGENERAL -> {
                   // calculatetopupdominicana()
                }
                selectedListener == CODEUSA && chip1 == SERVICEGENERAL-> {
                   // calculatetopupestadosunidos()
                }
            }


        }


    }

   /* private fun calculatetopupestadosunidos() {

            TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty() && chip1== SERVICEGENERAL) {
                        val countvaluetopupestadosunidos: Double =
                            TOTAL?.text.toString().toDouble() / PRICEUS!!
                        SUBTOTAL?.setText(countvaluetopupestadosunidos.roundToInt().toString())


                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

    }
*/
   /* private fun calculatetopupdominicana() {

        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEGENERAL) {
                        val countvaluetopupdominicana: Double =
                            TOTAL?.text.toString().toDouble() / PRICERD!!
                            SUBTOTAL?.setText(countvaluetopupdominicana.roundToInt().toString())


                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

    }

    private fun calculatetopupbrazil() {

        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEGENERAL) {

                        val countvaluetopupbrasil: Double =
                            TOTAL?.text.toString().toDouble() / PRICEBRAZIL!!
                            SUBTOTAL?.setText(countvaluetopupbrasil.roundToInt().toString())


                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })


    }

    private fun calculatetopuppanama() {
        TOTAL?.doOnTextChanged { txtTotal, _, _, _ ->
            if (txtTotal!!.isNotEmpty() && chip1== SERVICEGENERAL) {
                val countvaluetopuppanama: Double =
                    TOTAL?.text.toString().toDouble() / PRICEPANAMA!!
                SUBTOTAL?.setText(countvaluetopuppanama.roundToInt().toString())
            }
        }

    }*/

  /*  private fun calculatetopupchile() {
            TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEGENERAL) {
                        val countvaluetopupchile: Double =
                            TOTAL?.text.toString().toDouble() / PRICECHILE!!
                            SUBTOTAL?.setText(countvaluetopupchile.roundToInt().toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })


    }

    private fun calculatetopupmexico() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEGENERAL) {
                        val countvaluetopupmexico: Double =
                            TOTAL?.text.toString().toDouble() / PRICEMEXICO!!
                            SUBTOTAL?.setText(countvaluetopupmexico.roundToInt().toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })

    }

    private fun calculatetopupcuba() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEGENERAL) {
                        val countvaluetopupcuba: Double =
                            TOTAL?.text.toString().toDouble() / PRICECUBA!!
                        SUBTOTAL?.setText(countvaluetopupcuba.roundToInt().toString())
                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
    }

    private fun calculatetopup() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEHAITI4) {
                        val countvaluetopup: Double =
                            TOTAL?.text.toString().toDouble() / PRICETOPUPHAITI!!
                        SUBTOTAL?.setText(countvaluetopup.roundToInt().toString())

                    }
                }

                override fun afterTextChanged(p0: Editable?) {
                }

            })
    }*/
    private fun calculatelapoula() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEHAITI2) {
                    val countvaluelapoula: Double =
                        TOTAL?.text.toString().toDouble() / PRICEHAITILAPOULA!!
                    SUBTOTAL?.setText(countvaluelapoula.roundToInt().toString())

                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

    }
    private fun calculatemoncash() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEHAITI1) {
                        val countvaluemoncash: Double =
                            TOTAL?.text.toString()
                                .toDouble() / PRICEMONCASHHAITI!!
                          SUBTOTAL?.setText(countvaluemoncash.roundToInt().toString())

                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

    }

    private fun calculatenatcash() {
        TOTAL?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (TOTAL?.text.toString().isNotEmpty()&& chip1== SERVICEHAITI3) {
                        val countvaluenatcash: Double =
                            TOTAL?.text.toString().toDouble() / PRICEHAITINATCASH!!
                        SUBTOTAL?.setText(countvaluenatcash.roundToInt().toString())

                    }
                }

                override fun afterTextChanged(p0: Editable?) {

                }

            })

    }


    @SuppressLint("CutPasteId")
    fun validateOnclickButton() {
            when {
               PHONES?.text.toString().isEmpty() -> {
                    LAYOUPHONE?.helperText =
                        getString(R.string.erroremptyfield)
                    return
                }
                !mConstant.validatelenghnumberphone(PHONES?.text.toString()) -> {
                    LAYOUPHONE?.helperText =
                        getString(R.string.error_longitud_number)
                    return
                }
                else -> {
                    LAYOUPHONE?.hint = PHONES?.text.toString()
                    LAYOUPHONE?.helperText = ""
                    if(idProductSelected!=null && areaCodeCountry!!.isNotEmpty() && salesPriceSelected!=null){
                        observeData()
                    }else{
                        saleAgents()
                    }

                }
            }

    }


    private fun saleAgents() {

        when (chip1) {
            SERVICEHAITI2 -> {
                observeDataLapoula()
            }
            SERVICEHAITI3 -> {
                observeDataNatcash()
            }
            else -> {
                observeDataMoncash()
            }
        }


    }
    @SuppressLint("SetTextI18n")
    private fun observeDataNatcash() {

        val sales = Sales(
            mAuthProvider.getId()!!,
            FIRSTNAME,
            LASTSTNAME,
            EMAIL,
            ROLE!!,
            chip1!!,
            PHONES?.text.toString(),
            currentDate,
            selectedcountry!!.selectedCountryName,
            selectedcountry!!.selectedCountryNameCode,
            SUBTOTAL?.text.toString(),
            DESCRIPTION?.text.toString(),
            tokenuser!!,
            status = 0,
            0,
            "",
            IMAGE!!
        )
       // val newsaldolanatcash = BALANCENATCASH?.minus(TOTAL?.text.toString().toInt())
        viewmodel.sales(sales)
        viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sale ->
            if (sale.isSuccessful) {
                insertDAtaInRoomDataBase()
                sendEmail()
                sendNotificationToOtherDevice()
                saveNotification()
              /*  mUserProvider.updateNatCash(mAuthProvider.getId(), newsaldolanatcash!!)
                    ?.addOnSuccessListener { }*/
                val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.thanks)
                button.setOnClickListener {
                    startActivity(Intent(requireContext(), InvoiceActivity::class.java))
                    dialog.dismiss()
                    requireActivity().finish()

                }

            } else {
                Toast.makeText(requireContext(), sale.code().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
        /*
        when {
             TOTAL?.text.toString().toInt() > BALANCENATCASH!! -> {
                val view = View.inflate(requireContext(), R.layout.dialognomoney, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                 dialog.setCancelable(false)
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text =
                    getString(R.string.nomoney ) + getString( R.string.yourbalance) + " $ " + BALANCENATCASH
                button.setOnClickListener {
                    dialog.dismiss()
                    clearfield(typeselected)

                }

            }
            else -> {
                val sales = Sales(
                    mAuthProvider.getId()!!,
                    FIRSTNAME,
                    LASTSTNAME,
                    EMAIL,
                    ROLE!!,
                    chip1!!,
                    PHONES?.text.toString(),
                    currentDate,
                    selectedcountry!!.selectedCountryName,
                    selectedcountry!!.selectedCountryNameCode,
                    SUBTOTAL?.text.toString(),
                    DESCRIPTION?.text.toString()
                )
                val newsaldolanatcash = BALANCENATCASH?.minus(TOTAL?.text.toString().toInt())
                viewmodel.sales(sales)
                viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sale ->
                    if (sale.isSuccessful) {
                        insertDAtaInRoomDataBase()
                        sendEmail()
                        sendNotificationToOtherDevice()
                        saveNotification()
                        mUserProvider.updateNatCash(mAuthProvider.getId(), newsaldolanatcash!!)
                            ?.addOnSuccessListener { }
                        val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        val message = view.findViewById<TextView>(R.id.messagenomoney)
                        val button = view.findViewById<Button>(R.id.confirm)
                        message.text = getString(R.string.thanks)
                        button.setOnClickListener {
                            startActivity(Intent(requireContext(), InvoiceActivity::class.java))
                            dialog.dismiss()
                            requireActivity().finish()

                        }

                    } else {
                        Toast.makeText(requireContext(), sale.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }*/

    }

    @SuppressLint("SetTextI18n")
    private fun observeDataLapoula() {
        val sales = Sales(
            mAuthProvider.getId()!!,
            FIRSTNAME,
            LASTSTNAME,
            EMAIL,
            ROLE!!,
            chip1!!,
            PHONES?.text.toString(),
            currentDate,
            selectedcountry!!.selectedCountryName,
            selectedcountry!!.selectedCountryNameCode,
            SUBTOTAL?.text.toString(),
            DESCRIPTION?.text.toString(),
            tokenuser!!,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            IMAGE!!

        )
        // val newsaldolapoula = BALANCELAPOULA?.minus(TOTAL?.text.toString().toInt())
        viewmodel.sales(sales)
        viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sale ->
            if (sale.isSuccessful) {
                insertDAtaInRoomDataBase()
                sendEmail()
                sendNotificationToOtherDevice()
                saveNotification()
                /*mUserProvider.updateLapoula(mAuthProvider.getId(), newsaldolapoula!!)
                    ?.addOnSuccessListener { }*/
                val view = View.inflate(requireContext() , R.layout.dialog_success , null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.thanks)
                button.setOnClickListener {
                    startActivity(Intent(requireContext() , InvoiceActivity::class.java))
                    dialog.dismiss()
                    requireActivity().finish()

                }
            }
        })
      /*  when {
             TOTAL?.text.toString().toInt() > BALANCELAPOULA!! -> {
                val view = View.inflate(requireContext(), R.layout.dialognomoney, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                 dialog.setCancelable(false)
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.nomoney ) + getString( R.string.yourbalance) + " $ " + BALANCELAPOULA
                button.setOnClickListener {
                    dialog.dismiss()
                    clearfield(typeselected)

                }

            }
            else -> {
                val sales = Sales(
                    mAuthProvider.getId()!!,
                    FIRSTNAME,
                    LASTSTNAME,
                    EMAIL,
                    ROLE!!,
                    chip1!!,
                    PHONES?.text.toString(),
                    currentDate,
                    selectedcountry!!.selectedCountryName,
                    selectedcountry!!.selectedCountryNameCode,
                    SUBTOTAL?.text.toString(),
                    DESCRIPTION?.text.toString()
                )
               // val newsaldolapoula = BALANCELAPOULA?.minus(TOTAL?.text.toString().toInt())
                viewmodel.sales(sales)
                viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sale ->
                    if (sale.isSuccessful) {
                        insertDAtaInRoomDataBase()
                        sendEmail()
                        sendNotificationToOtherDevice()
                        saveNotification()
                        mUserProvider.updateLapoula(mAuthProvider.getId(), newsaldolapoula!!)
                            ?.addOnSuccessListener { }
                        val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        val message = view.findViewById<TextView>(R.id.messagenomoney)
                        val button = view.findViewById<Button>(R.id.confirm)
                        message.text = getString(R.string.thanks)
                        button.setOnClickListener {
                            startActivity(Intent(requireContext(), InvoiceActivity::class.java))
                            dialog.dismiss()
                            requireActivity().finish()

                        }

                    } else {
                        Toast.makeText(requireContext(), sale.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }*/

    }

    @SuppressLint("SetTextI18n")
    private fun observeDataMoncash() {
        val sales = Sales(
            mAuthProvider.getId()!!,
            FIRSTNAME,
            LASTSTNAME,
            EMAIL,
            ROLE!!,
            chip1,
            PHONES?.text.toString(),
            currentDate,
            selectedcountry!!.selectedCountryName,
            selectedcountry!!.selectedCountryNameCode,
            SUBTOTAL?.text.toString(),
            DESCRIPTION?.text.toString(),
            tokenuser!!,
            status = 0,
            idProduct = 0,
            salesPrice = "",
            IMAGE!!
        )
        //val newsaldomoncash = BALANCEMONCASH?.minus(TOTAL?.text.toString().toInt())
        viewmodel.sales(sales)
        viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sale ->
            if (sale.isSuccessful) {
                insertDAtaInRoomDataBase()
                sendEmail()
                sendNotificationToOtherDevice()
                saveNotification()
                /*mUserProvider.updateMoncash(mAuthProvider.getId(), newsaldomoncash!!)
                    ?.addOnSuccessListener { }*/
                val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.thanks)
                button.setOnClickListener {
                    startActivity(Intent(requireContext(), InvoiceActivity::class.java))
                    dialog.dismiss()
                    requireActivity().finish()

                }

            } else {
                Toast.makeText(requireContext(), sale.code().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
      /*  when {
             TOTAL?.text.toString().toInt() > BALANCEMONCASH!! -> {
                val view = View.inflate(requireContext(), R.layout.dialognomoney, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                 dialog.setCancelable(false)
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.nomoney ) + getString( R.string.yourbalance) + " $ " + BALANCEMONCASH
                button.setOnClickListener {
                    dialog.dismiss()
                    clearfield(typeselected)

                }

            }
            else -> {
                val sales = Sales(
                    mAuthProvider.getId()!!,
                    FIRSTNAME,
                    LASTSTNAME,
                    EMAIL,
                    ROLE!!,
                    chip1!!,
                    PHONES?.text.toString(),
                    currentDate,
                    selectedcountry!!.selectedCountryName,
                    selectedcountry!!.selectedCountryNameCode,
                    SUBTOTAL?.text.toString(),
                    DESCRIPTION?.text.toString()
                )
                val newsaldomoncash = BALANCEMONCASH?.minus(TOTAL?.text.toString().toInt())
                viewmodel.Sales(sales)
                viewmodel.myResponsesales.observe(viewLifecycleOwner, Observer { sale ->
                    if (sale.isSuccessful) {
                        insertDAtaInRoomDataBase()
                        sendEmail()
                        sendNotificationToOtherDevice()
                        saveNotification()
                        mUserProvider.updateMoncash(mAuthProvider.getId(), newsaldomoncash!!)
                            ?.addOnSuccessListener { }
                        val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        val message = view.findViewById<TextView>(R.id.messagenomoney)
                        val button = view.findViewById<Button>(R.id.confirm)
                        message.text = getString(R.string.thanks)
                        button.setOnClickListener {
                            startActivity(Intent(requireContext(), InvoiceActivity::class.java))
                            dialog.dismiss()
                            requireActivity().finish()

                        }

                    } else {
                        Toast.makeText(requireContext(), sale.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }*/

    }

    private fun clearfield(typeselected: ChipGroup?) {
        PHONES?.setText("")
        TOTAL?.setText("")
        typeselected?.findViewById<Chip>(R.id.moncash)?.isVisible = false

    }

    @SuppressLint("SimpleDateFormat", "CutPasteId", "SetTextI18n")
    private fun observeData() {

        val sales = Sales(
            mAuthProvider.getId()!!,
            FIRSTNAME,
            LASTSTNAME,
            EMAIL,
            ROLE!!,
            "",
            "${areaCodeCountry?.selectedCountryCodeWithPlus}${PHONES?.text.toString()}",
            currentDate,
            selectedcountry!!.selectedCountryName,
            selectedcountry!!.selectedCountryNameCode,
            "",
            "",
            tokenuser!!,
             0,
            idProductSelected!!,
            salesPriceSelected!!,
            IMAGE!!
        )
       // val newsaldotopup = BALANCETOPUP?.minus(TOTAL?.text.toString().toInt())
        viewmodel.sales(sales)
        viewmodel.myResponseSales.observe(viewLifecycleOwner, Observer { sals ->
            if (sals.isSuccessful) {
               // insertDAtaInRoomDataBase()
                sendEmail()
                sendNotificationToOtherDevice()
                saveNotification()
                //mUserProvider.updateTopup(mAuthProvider.getId(), newsaldotopup!!)?.isSuccessful
                val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.show()
                dialog.setCancelable(false)
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text = getString(R.string.thanks)
                button.setOnClickListener {
                    val intent =Intent(requireContext(),HomeActivity::class.java)
                    startActivity(intent)
                    dialog.dismiss()
                    requireActivity().finish()
                }

            } else {
                Toast.makeText(requireContext(), sals.code().toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
       /* when {
            TOTAL?.text.toString().toInt() > BALANCETOPUP!! -> {
                val view = View.inflate(requireContext(), R.layout.dialognomoney, null)
                val builder = AlertDialog.Builder(requireContext())
                builder.setView(view)
                val dialog = builder.create()
                dialog.setCancelable(false)
                dialog.show()
                dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                val message = view.findViewById<TextView>(R.id.messagenomoney)
                val button = view.findViewById<Button>(R.id.confirm)
                message.text =getString(R.string.nomoney ) + getString( R.string.yourbalance) + " $ " + BALANCETOPUP
                button.setOnClickListener {
                    dialog.dismiss()
                    clearfieldTOPUP()

                }
            }
            else -> {
                val sales = Sales(
                    mAuthProvider.getId()!!,
                    FIRSTNAME,
                    LASTSTNAME,
                    EMAIL,
                    ROLE!!,
                    chip1!!,
                    PHONES?.text.toString(),
                    currentDate,
                    selectedcountry!!.selectedCountryName,
                    selectedcountry!!.selectedCountryNameCode,
                    SUBTOTAL?.text.toString(),
                    DESCRIPTION?.text.toString()
                )
                val newsaldotopup = BALANCETOPUP?.minus(TOTAL?.text.toString().toInt())
                viewmodel.Sales(sales)
                viewmodel.myResponsesales.observe(viewLifecycleOwner, Observer { sals ->
                    if (sals.isSuccessful) {
                        insertDAtaInRoomDataBase()
                        sendEmail()
                        sendNotificationToOtherDevice()
                        saveNotification()
                        mUserProvider.updateTopup(mAuthProvider.getId(), newsaldotopup!!)?.isSuccessful
                        val view = View.inflate(requireContext(), R.layout.dialog_success, null)
                        val builder = AlertDialog.Builder(requireContext())
                        builder.setView(view)
                        val dialog = builder.create()
                        dialog.show()
                        dialog.setCancelable(false)
                        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
                        val message = view.findViewById<TextView>(R.id.messagenomoney)
                        val button = view.findViewById<Button>(R.id.confirm)
                        message.text = getString(R.string.thanks)
                        button.setOnClickListener {
                            val intent =Intent(requireContext(),InvoiceActivity::class.java)
                            startActivity(intent)
                            dialog.dismiss()
                            requireActivity().finish()
                        }

                    } else {
                        Toast.makeText(requireContext(), sals.code().toString(), Toast.LENGTH_SHORT)
                            .show()
                    }
                })
            }
        }*/

    }

    private fun clearfieldTOPUP() {
        PHONES?.setText("")
        TOTAL?.setText("")
    }

    private fun clearfieldTOPUPIgualacero(typeselected: ChipGroup?) {
        PHONES?.setText("")
        TOTAL?.setText("")
    }

    @SuppressLint("SimpleDateFormat", "CutPasteId")
    fun getDataUser() {
        userviewmodel.getOnlyUser(mAuthProvider.getId().toString())
        userviewmodel.ResposeUsers.observe(viewLifecycleOwner, Observer {
            if (it.isSuccessful) {
                FIRSTNAME = it.body()?.firstname
                LASTSTNAME = it.body()?.lastname
                EMAIL = it.body()?.email
                ROLE = it.body()?.role
                BALANCETOPUP = it.body()?.soltopup?.toInt()!!
                BALANCEMONCASH = it.body()?.soldmoncash?.toInt()!!
                BALANCENATCASH = it.body()?.soldnatcash?.toInt()
                BALANCELAPOULA = it.body()?.soldlapoula?.toInt()
                IMAGE =it.body()?.image
            } else {
                Toast.makeText(requireContext(), it.code().toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun getPriceCountry() {
        countrypricesviewmodel.getCountryPrice()
        countrypricesviewmodel.myResponseGetCountryPrice.observe(viewLifecycleOwner) {country->
            if (country.isSuccessful) {
                PRICERD = country.body()?.priceRD
                PRICEBRAZIL = country.body()?.pricebrasil
                PRICECHILE = country.body()?.pricechile
                PRICECUBA = country.body()?.pricecuba
                PRICEMEXICO = country.body()?.pricemexico
                PRICEMONCASHHAITI = country.body()?.pricemoncashhaiti
                PRICETOPUPHAITI = country.body()?.pricetopuphaiti
                PRICEUS = country.body()?.priceus
                PRICEPANAMA = country.body()?.pricepanama
                PRICEHAITILAPOULA = country.body()?.pricelapoulahaiti
                PRICEHAITINATCASH = country.body()?.pricenatcashhaiti


            } else {
                Toast.makeText(requireContext(), country.code().toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun insertDAtaInRoomDataBase() {
        val savedata = chip1?.let {
            UsersEntity(
                0,
                selectedcountry?.selectedCountryName!!,
                PAIS!!,
                FIRSTNAME!!,
                LASTSTNAME!!,
                EMAIL!!,
                PHONES?.text.toString(),
                ROLE!!,
                it ,
                currentDate,
                SUBTOTAL?.text.toString(),
                DESCRIPTION?.text.toString()
            )
        }
        if (savedata != null) {
            roomviewmodel.addUsers(savedata)
        }
    }

    private fun sendEmail() {
        val mEmail = "t43031313@gmail.com"
        val mSubject: String = SUBJECT
        val mMessage: String = "NOMBRE: $FIRSTNAME \n" +
                "APELLIDO: $LASTSTNAME \n" +
                "EMAIL: $EMAIL\n" +
                "ROLE: $ROLE \n" +
                "PAIS: ${selectedcountry!!.selectedCountryName}\n" +
                "TIPO DE RECARGA: $chip1 \n" +
                "MONTO: ${SUBTOTAL?.text.toString()} \n" +
                "TELEFONO: ${PHONES?.text.toString()} \n" +
                "DESCRIPTION ${DESCRIPTION?.text.toString()}"
        val javaMailAPI = JavaMailAPI(requireContext(), mEmail, mSubject, mMessage)
        javaMailAPI.execute()
    }

    companion object {
        var FIRSTNAME: String? = null
        var LASTSTNAME: String? = null
        var EMAIL: String? = null
        var ROLE: Int? = null
        var IMAGE: String?=null
        var BALANCETOPUP: Int? = null
        var BALANCEMONCASH: Int? = null
        var BALANCELAPOULA: Int? = null
        var BALANCENATCASH: Int? = null
        var selectedListener: String? = null
        var chip1: String? = null
        var PAIS: String? = null
        var PRICERD: Float? = null
        var PRICEBRAZIL: Float? = null
        var PRICECHILE: Float? = null
        var PRICECUBA: Float? = null
        var PRICEMEXICO: Float? = null
        var PRICEMONCASHHAITI: Float? = null
        var PRICETOPUPHAITI: Float? =null
        var PRICEHAITILAPOULA: Float? = null
        var PRICEHAITINATCASH: Float? = null
        var PRICEUS: Float? = null
        var PRICEPANAMA: Float? = null

        @SuppressLint("SimpleDateFormat")
        var sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

    }

    private fun sendNotification(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstanceFCM.notificationAPI.postNotification(notification)
                if (response.isSuccessful) {
                } else {
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
        val role = when (ROLE) {
            1 -> {
                getString(R.string.agent)
            }
            else -> {
                getString(R.string.master)

            }
        }
        val saveData = SaveNotification(
            mAuthProvider.getId().toString(),
            TITLENOTIFICATION, "$EL $role  $FIRSTNAME $MESSAGENOTIFICATION $chip1 $MESSAGETOTALNOTIFICATION ${SUBTOTAL?.text.toString()}" +
                    " $FOR ${selectedcountry?.selectedCountryName} ", currentDate,PHONES?.text.toString()
        )
        viewmodelsavenotification.saveNotification(saveData)
        viewmodelsavenotification.myResponsesavenotification.observe(
            viewLifecycleOwner,
            Observer { savedata ->
                if (savedata.isSuccessful) {

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
                            val role = when (ROLE) {
                                1 -> {
                                    getString(R.string.agent)
                                }
                                else -> {
                                    getString(R.string.master)

                                }
                            }
                            PushNotification(

                                NotificationData(
                                    TITLENOTIFICATION,
                                    "$EL $role  $FIRSTNAME $MESSAGENOTIFICATION $chip1 $MESSAGETOTALNOTIFICATION ${SUBTOTAL?.text.toString()}" +
                                            " $FOR ${selectedcountry?.selectedCountryName} "
                                ),
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

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}



