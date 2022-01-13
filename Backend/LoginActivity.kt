package com.irfanapp.eznoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val ImageView = findViewById<Button>(R.id.buttonlog)

        auth = FirebaseAuth.getInstance()
        val currentuser = auth.currentUser
        if(currentuser !=null){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        ImageView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegLogin.setOnClickListener {
            startActivity(Intent(this,RegisterActivity::class.java))
            overridePendingTransition(R.anim.slide_from_right,R.anim.slide_to_left)
        }

        login()
    }
    private fun login () {
        buttonlog.setOnClickListener{

            if (TextUtils.isEmpty(emailinput.text.toString())){
                emailinput.setError("Please enter email.")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passinput.text.toString())) {
                passinput.setError("Please enter password.")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(emailinput.text.toString(), passinput.text.toString())
                .addOnCompleteListener{
                   if (it.isSuccessful){
                       startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                       finish()
                   } else {
                       Toast.makeText(this@LoginActivity, "Login gagal, silahkan coba lagi.", Toast.LENGTH_LONG).show()
                   }
                }

        }
    }

}