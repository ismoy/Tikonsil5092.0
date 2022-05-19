package com.tikonsil.tikonsil509.ui.fragment.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.textfield.TextInputEditText
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.adapter.LastSaleAdapter
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokenProvider
import com.tikonsil.tikonsil509.domain.repository.home.UsersRepository
import com.tikonsil.tikonsil509.domain.repository.lastsales.LastSalesRepository
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.home.UserViewModelFactory
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModel
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModelProvider
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODEBRAZIL
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODECHILE
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODEHAITI
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODEMEXICO
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODEPANAMA
import com.tikonsil.tikonsil509.utils.ConstantCodeCountry.CODEREPUBLICANDOMINIK
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYBRAZIL
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYCHILE
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYHAITI
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYMEXICO
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYPANAMA
import com.tikonsil.tikonsil509.utils.ConstantCurrencyCountry.CURRENCYREPUBLICANDOMINK
import de.hdodenhof.circleimageview.CircleImageView

/** * Created by ISMOY BELIZAIRE on 26/04/2022. */
abstract class HomeValidate<VB:ViewBinding,VM:ViewModel>:Fragment() {
 protected lateinit var binding:VB
 protected lateinit var viewmodel: UserViewModel
 protected lateinit var mAuthProvider: AuthProvider
 protected lateinit var navController: NavController
 private  lateinit var mviewmodellastsales:LastSalesViewModel
 private lateinit var lastSaleAdapter: LastSaleAdapter
 private lateinit var linearLayoutManager: LinearLayoutManager
 protected var shimmerFrameLayout: ShimmerFrameLayout?=null
 protected var shimmerFrameLayoutwelcome: ShimmerFrameLayout?=null
 protected var usernamewel:TextView?=null
 protected  var recycler:RecyclerView?=null
 protected lateinit var mTokenProvider: TokenProvider
 protected var saldotopup:TextView?=null
 protected var saldomoncash:TextView?=null
 protected var saldonatcash:TextView?=null
 protected var saldolapoula:TextView?=null
 protected var cardviewtopup:CardView?=null
 protected var cardviewmoncash:CardView?=null
 protected var cardviewnatcash:CardView?=null
 protected var cardviewlapoula:CardView?=null

 override fun onCreateView(
  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  binding = getFragmentBinding(inflater, container)
  val repository = UsersRepository()
  val factory = UserViewModelFactory(repository)
  viewmodel = ViewModelProvider(requireActivity(),factory)[UserViewModel::class.java]

  val repositorylastsales = LastSalesRepository()
  val factorylastsales = LastSalesViewModelProvider(repositorylastsales)
  mviewmodellastsales = ViewModelProvider(requireActivity(),factorylastsales)[LastSalesViewModel::class.java]
  mAuthProvider = AuthProvider()
  lastSaleAdapter = LastSaleAdapter(requireContext())
  linearLayoutManager = LinearLayoutManager(requireContext())
  recycler =binding.root.findViewById(R.id.recyclerviewultimaventa)
  shimmerFrameLayout =binding.root.findViewById(R.id.shimmer)
  shimmerFrameLayoutwelcome =binding.root.findViewById(R.id.shimmerwelcome)
  usernamewel = binding.root.findViewById(R.id.usernamewel)
  saldotopup =binding.root.findViewById(R.id.saldotopup)
  saldomoncash = binding.root.findViewById(R.id.saldomoncash)
  saldonatcash = binding.root.findViewById(R.id.saldonatcash)
  saldolapoula = binding.root.findViewById(R.id.saldolapoula)
  cardviewtopup = binding.root.findViewById(R.id.cardViewtopup)
  cardviewmoncash = binding.root.findViewById(R.id.cardViewmoncash)
  cardviewnatcash = binding.root.findViewById(R.id.cardViewnatcash)
  cardviewlapoula = binding.root.findViewById(R.id.cardViewlapoula)
  mTokenProvider = TokenProvider()
  return binding.root
 }


 @SuppressLint("SetTextI18n")
 fun showDataInView(){
  viewmodel.getOnlyUser(mAuthProvider.getId().toString())
  viewmodel.ResposeUsers.observe(viewLifecycleOwner, Observer { response->
   if (response.isSuccessful){
    val balance =view?.findViewById<TextView>(R.id.totalbalance)
    val topUpsold =response.body()?.soltopup
    val topMonCashsold =response.body()?.soldmoncash
    val lapoulasold =response.body()?.soldlapoula
    val natcashsold =response.body()?.soldnatcash
    val totalbalance = topUpsold?.plus(topMonCashsold!!)?.plus(lapoulasold!!)?.plus(natcashsold!!)
    response.body()?.apply {
     usernamewel?.text =firstname
     saldotopup?.text = soltopup.toString()
     saldomoncash?.text = soldmoncash.toString()
     saldonatcash?.text =soldnatcash.toString()
     saldolapoula?.text =soldlapoula.toString()
    }
    response.body()?.countrycode.apply {
     when {
      equals(CODECHILE) -> {
       balance?.text = "$CURRENCYCHILE $totalbalance"
      }
      equals(CODEHAITI) -> {
       balance?.text = "$CURRENCYCHILE $totalbalance"
      }
      equals(CODEREPUBLICANDOMINIK) -> {
       balance?.text = "$CURRENCYREPUBLICANDOMINK $totalbalance"
      }
      equals(CODEPANAMA) -> {
       balance?.text = "$CURRENCYPANAMA $totalbalance"
      }
      equals(CODEMEXICO) -> {
       balance?.text = "$CURRENCYMEXICO $totalbalance"
      }
      equals(CODEBRAZIL) -> {
       balance?.text = "$CURRENCYBRAZIL $totalbalance"
      }
     }
    }
    shimmerFrameLayoutwelcome?.stopShimmer()
    shimmerFrameLayoutwelcome?.isGone=true
    usernamewel!!.isVisible = true
    binding.root.findViewById<CircleImageView>(R.id.image).isVisible = true
    binding.root.findViewById<RelativeLayout>(R.id.relativebalance).isGone = false
    binding.root.findViewById<ScrollView>(R.id.scrollviewcard).isVisible = true



   }else{
    Toast.makeText(requireContext(), response.code(), Toast.LENGTH_SHORT).show()
   }
  })
 }


 fun observeData(){
  mviewmodellastsales.getLastSales(mAuthProvider.getId().toString()).observe(requireActivity(),
   Observer {
    if (it.isNotEmpty()){
     lastSaleAdapter.setsaleListData(it)
     setupRecyclerview()
     shimmerFrameLayout?.stopShimmer()
     shimmerFrameLayout?.isGone=true
     recycler?.isGone=false
     binding.root.findViewById<TextView>(R.id.nodata).isGone=true
    }else{
     binding.root.findViewById<TextView>(R.id.nodata).isGone=false
     shimmerFrameLayout?.isGone=true
    }
   })
 }
 abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB

 abstract fun getViewModel():Class<VM>
 private fun setupRecyclerview(){
  recycler!!.adapter = lastSaleAdapter
  recycler!!.layoutManager = linearLayoutManager
  recycler!!.hasFixedSize()
 }
 fun gotosendrecharge(){
  cardviewtopup?.setOnClickListener {
   navController.navigate(R.id.action_homeFragment_to_sendRechargeFragment)
  }
 }
 fun gotosendrecharge2(){
  cardviewmoncash?.setOnClickListener {
   navController.navigate(R.id.action_homeFragment_to_sendRechargeFragment)
  }
 }
 fun gotosendrecharge3(){
  cardviewnatcash?.setOnClickListener {
   navController.navigate(R.id.action_homeFragment_to_sendRechargeFragment)
  }
 }
 fun gotosendrecharge4(){
  cardviewlapoula?.setOnClickListener {
   navController.navigate(R.id.action_homeFragment_to_sendRechargeFragment)
  }
 }

}