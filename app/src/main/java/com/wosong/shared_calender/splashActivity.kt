package com.wosong.shared_calender

import Authentication.LoginActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.google.firebase.auth.FirebaseAuth

class splashActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        Handler().postDelayed(Runnable {
            val main = Intent(this@splashActivity,MainActivity::class.java)
            val login = Intent(this@splashActivity,LoginActivity::class.java)

            if(currentUser != null){
                //로그인 되어있을 경우
                startActivity(main)
            }else{
                //로그인 되어있지 않을 경우
                startActivity(login)
            }
            finish()
        },3000)
    }


}