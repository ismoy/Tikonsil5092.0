package com.tikonsil.tikonsil509.ui.fragment.request_recharge

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.BANRESERVAS
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.CUENTARUT
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.GOOGLEPAY
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.MERCADOPAGO
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.METHODSELECTED
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.PAYPAL
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.PL
import com.tikonsil.tikonsil509.utils.service.ConstantGeneral.ZELLE

/** * Created by ISMOY BELIZAIRE on 17/05/2022. */
abstract class RequestRechargeValidate<VB:ViewBinding,VM:ViewModel> :Fragment(){
    protected lateinit var binding: VB
    protected var paymentselected: ChipGroup? = null
    protected var cuentarut:Chip?=null
    protected var paypal:Chip?=null
    protected var zelle:Chip?=null
    protected var pl:Chip?=null
    protected var banreserva:Chip?=null
    protected var googlepay:Chip?=null
    protected var mercadopago:Chip?=null
    protected var cardView4:CardView?=null
    protected var cardgooglepay:CardView?=null
    protected var cardpaypal:CardView?=null
    protected var cardmercadopago:CardView?=null
    protected var accountnumberrut:TextView?=null
    protected var cardviewzelle:CardView?=null
    protected var cardviewbanreservas:CardView?=null
    protected var cardviewpls:CardView?=null
    protected var account_banreservas:TextView?=null
    protected var account_zelle:TextView?=null
    protected var code_pl:TextView?=null
    protected var email_pl:TextView?=null
    protected lateinit var navController: NavController
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = getFragmentBinding(inflater, container)
        paymentselected = binding.root.findViewById(R.id.groupchippayment)
        cuentarut = binding.root.findViewById(R.id.cuentarut)
        paypal = binding.root.findViewById(R.id.paypal)
        zelle = binding.root.findViewById(R.id.zelle)
        pl = binding.root.findViewById(R.id.pl_)
        banreserva = binding.root.findViewById(R.id.bandereserva)
        googlepay = binding.root.findViewById(R.id.googlepay)
        mercadopago = binding.root.findViewById(R.id.mercadopago)
        cardView4 =binding.root.findViewById(R.id.cardviewbancoestado)
        cardgooglepay = binding.root.findViewById(R.id.cardviewgooglepay)
        cardpaypal = binding.root.findViewById(R.id.cardviewpaypal)
        cardmercadopago = binding.root.findViewById(R.id.cardviewmercadopago)
        accountnumberrut = binding.root.findViewById(R.id.account_number)
        cardviewzelle =binding.root.findViewById(R.id.cardviewzelle)
        cardviewbanreservas = binding.root.findViewById(R.id.cardviewbanreservas)
        cardviewpls =binding.root.findViewById(R.id.cardviewpls)
        account_banreservas = binding.root.findViewById(R.id.account_banreservas)
        account_zelle = binding.root.findViewById(R.id.account_zelle)
        code_pl = binding.root.findViewById(R.id.code_pl)
        email_pl = binding.root.findViewById(R.id.email_pl)
        return binding.root
    }

    fun selectemethodpayment(){
        paymentselected?.setOnCheckedStateChangeListener { group, checkedId ->
            METHODSELECTED = paymentselected?.findViewById<Chip>(group.checkedChipId)?.text.toString()
                when(METHODSELECTED){
                    CUENTARUT->{
                        cardView4?.isGone =false
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone=true
                        cardviewbanreservas?.isGone=true
                        cardviewpls?.isGone=true
                    }
                    PAYPAL->{
                        cardpaypal?.isGone=false
                        cardView4?.isGone=true
                        cardgooglepay?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone=true
                        cardviewbanreservas?.isGone=true
                        cardviewpls?.isGone=true
                    }
                    GOOGLEPAY->{
                        cardgooglepay?.isGone=false
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone=true
                        cardviewbanreservas?.isGone=true
                        cardviewpls?.isGone=true
                    }
                    MERCADOPAGO->{
                        cardmercadopago?.isGone=false
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardviewzelle?.isGone=true
                        cardviewbanreservas?.isGone=true
                        cardviewpls?.isGone=true
                    }
                    ZELLE->{
                        cardviewzelle?.isGone=false
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardviewbanreservas?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewpls?.isGone=true
                    }
                    BANRESERVAS->{
                        cardviewbanreservas?.isGone=false
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone =true
                        cardviewpls?.isGone=true
                    }
                    PL->{
                        cardviewpls?.isGone=false
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone =true
                        cardviewbanreservas?.isGone=true
                    }
                    else->{
                        cardView4?.isGone=true
                        cardpaypal?.isGone=true
                        cardgooglepay?.isGone=true
                        cardmercadopago?.isGone=true
                        cardviewzelle?.isGone =true
                        cardviewbanreservas?.isGone=true
                        cardviewpls?.isGone=true
                    }
                }



        }

    }

    fun copyNumberAccount(){
        val clipboard =  requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TextView",accountnumberrut?.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.account_number), Toast.LENGTH_SHORT).show()
    }
    fun copyNumberAccountReserva(){
        val clipboard =  requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TextView",account_banreservas?.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.account_number), Toast.LENGTH_SHORT).show()
    }
    fun copyNumberAccountZelle(){
        val clipboard =  requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TextView",account_zelle?.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.account_number), Toast.LENGTH_SHORT).show()
    }
    fun copyNumberAccountPL(){
        val clipboard =  requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TextView",code_pl?.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.account_number), Toast.LENGTH_SHORT).show()
    }
    fun copyEmailAccountPL(){
        val clipboard =  requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("TextView",email_pl?.text.toString())
        clipboard.setPrimaryClip(clip)
        Toast.makeText(requireContext(), getString(R.string.account_number), Toast.LENGTH_SHORT).show()
    }
    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}