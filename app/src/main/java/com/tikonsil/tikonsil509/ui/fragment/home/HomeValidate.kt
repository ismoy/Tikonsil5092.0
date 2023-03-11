package com.tikonsil.tikonsil509.ui.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
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
import com.bumptech.glide.Glide
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.data.adapter.LastSaleAdapter
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokenProvider
import com.tikonsil.tikonsil509.data.remote.provider.TokensAdminProvider
import com.tikonsil.tikonsil509.domain.repository.lastsales.LastSalesRepository
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModel
import com.tikonsil.tikonsil509.presentation.lastsales.LastSalesViewModelProvider
import com.tikonsil.tikonsil509.presentation.savestatus.StatusUserViewModel
import com.tikonsil.tikonsil509.ui.activity.login.LoginActivity
import com.tikonsil.tikonsil509.utils.constants.UtilsView
import de.hdodenhof.circleimageview.CircleImageView
import kotlin.math.roundToInt

/** * Created by ISMOY BELIZAIRE on 26/04/2022. */
abstract class HomeValidate<VB:ViewBinding,VM:ViewModel>:Fragment() {
 protected lateinit var binding:VB
 protected lateinit var mAuthProvider: AuthProvider
 protected lateinit var navController: NavController
 private  lateinit var mviewmodellastsales:LastSalesViewModel
 private lateinit var lastSaleAdapter: LastSaleAdapter
 private lateinit var linearLayoutManager: LinearLayoutManager
 private var shimmerFrameLayout: ShimmerFrameLayout?=null
 private var noDataFound:ImageView?=null
 private var shimmerFrameLayoutwelcome: ShimmerFrameLayout?=null
 private var usernamewel:TextView?=null
 private var recycler:RecyclerView?=null
 private var saldotopup:TextView?=null
 private var saldomoncash:TextView?=null
 private var saldonatcash:TextView?=null
 private var saldolapoula:TextView?=null
 private var cardviewtopup:CardView?=null
 private var cardviewmoncash:CardView?=null
 private var cardviewnatcash:CardView?=null
 private var cardviewlapoula:CardView?=null
 private var balance:TextView?=null
 private lateinit var image_home:CircleImageView
 private  var relativebalance:RelativeLayout?=null
 protected lateinit var userTokenProvider:TokenProvider
 private lateinit var mTokensAdminProvider: TokensAdminProvider
 protected  val mviewmodelstatususer by lazy { ViewModelProvider(requireActivity())[StatusUserViewModel::class.java] }
 private val viewmodel by  lazy { ViewModelProvider(this)[UserViewModel::class.java]}
 override fun onCreateView(
  inflater: LayoutInflater,
  container: ViewGroup?,
  savedInstanceState: Bundle?
 ): View? {
  binding = getFragmentBinding(inflater, container)

  val repositorylastsales = LastSalesRepository()
  val factorylastsales = LastSalesViewModelProvider(repositorylastsales)
  mviewmodellastsales = ViewModelProvider(requireActivity(),factorylastsales)[LastSalesViewModel::class.java]
  mAuthProvider = AuthProvider()
  userTokenProvider = TokenProvider()
  mTokensAdminProvider = TokensAdminProvider()
  lastSaleAdapter = LastSaleAdapter(requireContext())
  linearLayoutManager = LinearLayoutManager(requireContext())
  recycler =binding.root.findViewById(R.id.recyclerviewultimaventa)
  shimmerFrameLayout =binding.root.findViewById(R.id.shimmer)
  shimmerFrameLayoutwelcome =binding.root.findViewById(R.id.shimmerwelcome)
  usernamewel = binding.root.findViewById(R.id.usernamewel)
  saldotopup =binding.root.findViewById(R.id.saldotopup)
  saldomoncash = binding.root.findViewById(R.id.saldomoncash)

  noDataFound =binding.root.findViewById(R.id.noDataFound)
  relativebalance =binding.root.findViewById(R.id.relativebalance)
  balance=binding.root.findViewById(R.id.totalbalance)
  image_home = binding.root.findViewById(R.id.image_home)
  return binding.root
 }
 @SuppressLint("SetTextI18n")
 fun showDataInView(){
  viewmodel.getOnlyUser(mAuthProvider.getId().toString())
  viewmodel.ResposeUsers.observe(viewLifecycleOwner, Observer { response->
   if (response.isSuccessful){
    if (response.body()?.status==0){
     ShowdialogIncativeAccount()
    }
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
     if (image!=null){
      Glide.with(requireActivity()).load(image).into(image_home)
     }
     if (role==2){
      relativebalance?.visibility =View.VISIBLE
      val balanceNumber = (topUpsold?.toDouble()?.times(100.0))?.roundToInt()?.div(100.0)
      balance?.text = (balanceNumber.toString())
      UtilsView.setValueSharedPreferences(requireActivity(),"totalBalance",balanceNumber.toString())
     }
    }
    shimmerFrameLayoutwelcome?.stopShimmer()
    shimmerFrameLayoutwelcome?.isGone=true
    usernamewel!!.isVisible = true
    binding.root.findViewById<CircleImageView>(R.id.image).isVisible = true
   }
  })
 }
 private fun ShowdialogIncativeAccount() {
  val view = View.inflate(requireContext(), R.layout.dialoginactiveaccount, null)
  val builder = AlertDialog.Builder(requireContext())
  builder.setView(view)
  val dialog = builder.create()
  dialog.show()
  dialog.setCancelable(false)
  dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
  val button = view.findViewById<Button>(R.id.confirmnoactive)
  button.setOnClickListener {
   startActivity(Intent(requireActivity(),LoginActivity::class.java))
   requireActivity().finish()
  }
 }
 fun observeData(){

  mviewmodellastsales.getLastSales(mAuthProvider.getId().toString()).observe(requireActivity()) {
   if (it==null){
    shimmerFrameLayout?.stopShimmer()
    shimmerFrameLayout?.isVisible=false
    shimmerFrameLayout?.isGone =true
    recycler?.isGone=false
   }else {
    shimmerFrameLayout?.stopShimmer()
    shimmerFrameLayout?.isGone =true
    recycler?.isGone=false
    lastSaleAdapter.setsaleListData(it)
    setupRecyclerview()
   }

  }
  mviewmodellastsales.isSnapShotExist.observe(viewLifecycleOwner, Observer { isExist->
   if (!isExist){
    shimmerFrameLayout?.stopShimmer()
    shimmerFrameLayout?.isGone =true
    noDataFound?.isGone =false
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

  fun getTokenUser() {
  userTokenProvider.getToken(mAuthProvider.getId().toString()).addListenerForSingleValueEvent(object :
   ValueEventListener {
   override fun onDataChange(snapshot: DataSnapshot) {
    if (snapshot.exists()){
     val token=snapshot.child("token").value.toString()
     UtilsView.setValueSharedPreferences(requireActivity(),"tokenUsers",token)

    }
   }
   override fun onCancelled(error: DatabaseError) {
   }

  })
 }

 fun getTokenAdmin(){
  mTokensAdminProvider.getToken()?.addListenerForSingleValueEvent(object :ValueEventListener{
   override fun onDataChange(snapshot: DataSnapshot) {
   if (snapshot.exists()){
    for (ds in snapshot.children){
     val tokenAdmin =ds.child("token").value.toString()
     UtilsView.setValueSharedPreferences(requireActivity(),"tokenAdmin",tokenAdmin)
    }

   }
   }

   override fun onCancelled(error: DatabaseError) {
   }

  })
 }

}