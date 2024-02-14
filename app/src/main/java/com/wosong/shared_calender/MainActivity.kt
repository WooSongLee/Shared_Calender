package com.wosong.shared_calender

import Authentication.LoginActivity
import Model.GroupMakingModel
import Model.UserModel
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.MessagePattern.ApostropheMode
import android.icu.text.MessagePattern.validateArgumentName
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



private val groupList = mutableListOf<GroupMakingModel>()
class MainActivity : AppCompatActivity() {

    lateinit var mainAdapter : MainAdapter
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private var groupCount : Int = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {


        database = Firebase.database.reference
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logout = findViewById<Button>(R.id.logoutBtn)
        logout.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(this@MainActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        val createBtn = findViewById<Button>(R.id.createGroupBtn)
        createBtn.setOnClickListener {
            showCreateDialog()
        }
        val listView = findViewById<ListView>(R.id.mainlistview)



        mainAdapter = MainAdapter(baseContext, groupList)
        listView.adapter = mainAdapter
        updateUserData()



    }
    fun updateUserData(){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                groupList.clear()
                for(dataModel in dataSnapshot.children){
                   val group = dataModel.getValue(GroupMakingModel::class.java)
                    groupList.add(group!!)
                }

                mainAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        database.child("groupList").addValueEventListener(postListener)

    }
    fun showCreateDialog() {
        val mDialog = LayoutInflater.from(this).inflate(R.layout.create_group_dialog, null)
        val mBulider = AlertDialog.Builder(this)
            .setView(mDialog)
            .setTitle("Creating Group")

        val mAlertDialog = mBulider.show()
        //dialog size adjustment
        val window = mAlertDialog.window
        window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)

        val btn = mAlertDialog.findViewById<Button>(R.id.createBtn)
        btn!!.setOnClickListener {
            // Fetching groupCount from database
            database.child("dataCount").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    groupCount = dataSnapshot.getValue(Int::class.java) ?: 0 // Default value is 0 if groupCount is null

                    val groupName = mAlertDialog.findViewById<EditText>(R.id.groupNameArea)!!.text.toString()
                    // Storing group data
                    val newGroup = GroupMakingModel(groupName,groupCount)
                    database.child("groupList").push().setValue(newGroup)
                        .addOnSuccessListener {
                            //update groupList
                            database.child("dataCount").setValue(groupCount+1) // Incrementing groupCount

                            mainAdapter.notifyDataSetChanged()
                            mAlertDialog.dismiss()
                        }
                        .addOnFailureListener { e ->
                            // Failed to store group data
                            // Handle the error
                            Toast.makeText(this@MainActivity, "Failed to create group: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle the error
                    Toast.makeText(this@MainActivity, "Database error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }//end of Btn

        val infobtn = findViewById<Button>(R.id.myInfoBtn)
        infobtn.setOnClickListener {
            val intent = Intent(this, MyInfoActivity::class.java)
            startActivity(intent)
        }
    }


}