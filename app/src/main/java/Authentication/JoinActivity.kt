package Authentication

import Model.UserModel
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.wosong.shared_calender.MainActivity
import com.wosong.shared_calender.R
private lateinit var auth: FirebaseAuth
private lateinit var database: DatabaseReference
private lateinit var userModel : UserModel
class JoinActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        auth = Firebase.auth
        database = Firebase.database.reference

        val JoinBtn = findViewById<Button>(R.id.JoinBtn)
        JoinBtn.setOnClickListener {
            val nickname = findViewById<TextInputEditText>(R.id.JoinNameArea).text.toString()
            val email = findViewById<TextInputEditText>(R.id.JoinEmailArea).text.toString()
            val password = findViewById<TextInputEditText>(R.id.JoinPasswordArea).text.toString()
            val passwordCheckArea = findViewById<TextInputEditText>(R.id.passwordCheckArea).text.toString()
            if(password != passwordCheckArea){
                Toast.makeText(this, "checking your password and password check", Toast.LENGTH_SHORT).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            userModel = UserModel(nickname,email,password)
                            database.child("userModel").child(auth.uid.toString()).setValue(userModel)

                            Toast.makeText(this, "Join success", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this,MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "Join failed", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }//end of JoinBtn
    }
}