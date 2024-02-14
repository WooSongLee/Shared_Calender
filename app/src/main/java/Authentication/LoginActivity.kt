package Authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.wosong.shared_calender.MainActivity
import com.wosong.shared_calender.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val logInBtn = findViewById<Button>(R.id.loginBtn)

        logInBtn.setOnClickListener {

            val email = findViewById<EditText>(R.id.emailArea).text.toString()
            val password = findViewById<EditText>(R.id.passwordArea).text.toString()

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "logIn failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }//end of longIn Btn

        val joinText = findViewById<TextView>(R.id.joinText)
        //JoinText를 클릭하면, JoinActivity로 넘어가도록 처리
        joinText.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }
}