package com.tikonsil.tikonsil509.ui.activity.splashscreen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.tikonsil.tikonsil509.data.remote.provider.AuthProvider
import com.tikonsil.tikonsil509.data.remote.provider.UserProvider
import com.tikonsil.tikonsil509.ui.activity.home.HomeActivity
import com.tikonsil.tikonsil509.ui.activity.login.LoginActivity

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {
    var mAuthProvider: AuthProvider? = null
    var mUserProvider: UserProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mAuthProvider = AuthProvider()
        mUserProvider = UserProvider()

        checkuserexixt()

    }

    private fun checkuserexixt() {
      if (mAuthProvider?.existSession() == true){
         startTimerHome()
      }else{
          startTimer()
      }
    }

    private fun startTimerHome() {
        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
                val intent= Intent(applicationContext,HomeActivity::class.java).apply {  }
                startActivity(intent)
                finish()
            }

        }.start()

    }

    private fun startTimer() {
        object :CountDownTimer(2000,1000){
            override fun onTick(p0: Long) {

            }

            override fun onFinish() {
               val intent= Intent(applicationContext,LoginActivity::class.java).apply {  }
                startActivity(intent)
                finish()
            }

        }.start()
    }
}