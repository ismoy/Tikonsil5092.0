package com.tikonsil.tikonsil509.ui.activity.home

import android.os.Bundle
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.tikonsil.tikonsil509.R
import com.tikonsil.tikonsil509.databinding.ActivityHomeBinding
import com.tikonsil.tikonsil509.presentation.home.UserViewModel
import com.tikonsil.tikonsil509.ui.base.BaseActivity

class HomeActivity : BaseActivity<UserViewModel, ActivityHomeBinding>(),
    NavigationView.OnNavigationItemSelectedListener {
    lateinit var appBarConfiguration: AppBarConfiguration
    lateinit var bottomNavigationView: BottomNavigationView
    lateinit var drawerLayout: DrawerLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //BottomNavigationView
        navController = findNavController(R.id.container_fragment)
        bottomNavigationView = binding.bottomNavigationView
        setupBottomNavigation()
        //DrawerLayout
        drawerLayout = binding.drawerLayout
        //NavigationUpButton
        appBarConfiguration = AppBarConfiguration(navController.graph, drawerLayout)
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout)
        NavigationUI.setupWithNavController(binding.navView, navController)

        val navigationview: NavigationView = binding.navView
        navigationview.setNavigationItemSelectedListener(this)
        showDataInView()

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, appBarConfiguration)
    }


    private fun setupBottomNavigation() {
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_sendRechargeFragment -> navController.navigate(R.id.action_homeFragment_to_sendRechargeFragment)
            R.id.nav_historySalesFragment -> navController.navigate(R.id.action_homeFragment_to_historySalesFragment)
            R.id.nav_saleWithOtherAgentFragment -> navController.navigate(R.id.action_homeFragment_to_saleWithOtherAgentFragment)
            R.id.nav_registerReferencesFragment -> navController.navigate(R.id.action_homeFragment_to_registerReferencesFragment)
            R.id.nav_policyAndPrivacyFragment -> navController.navigate(R.id.action_homeFragment_to_policyAndPrivacyFragment)
            R.id.nav_requestRechargeFragment->navController.navigate(R.id.action_homeFragment_to_requestRechargeFragment)
            R.id.nav_whatsapp->sendMesageWhatsapp()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun getActivityBinding() = ActivityHomeBinding.inflate(layoutInflater)
    override fun getViewModel() = UserViewModel::class.java


}