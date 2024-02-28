package com.wosong.shared_calender

import Model.UserModel
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MyInfoActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        getNickname()

    }
    fun getNickname(){
        database = Firebase.database.reference
        val uid = Firebase.auth.currentUser?.uid

        if(uid != null){
            val postReference = database.child("userModel").child(uid)

            postReference.addListenerForSingleValueEvent(object : ValueEventListener {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userModel = snapshot.getValue(UserModel::class.java)
                    userModel?.let {
                        val nickname = userModel.nickname
                        val nicknameArea = findViewById<TextView>(R.id.nicknameArea)
                        nicknameArea.text = nickname
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
        }
    }//end of getNickname()
}