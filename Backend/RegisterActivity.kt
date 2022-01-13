package com.irfanapp.eznoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://login-register-2b2ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = database?.reference!!.child("profile")
        btnLogRegister.setOnClickListener {
            onBackPressed()
        }
        register()
    }

    private fun register(){
        buttonreg.setOnClickListener{

            if (TextUtils.isEmpty(etUser.text.toString())){
                etUser.setError("Tolong isi Username anda!")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(etEmail.text.toString())){
                etEmail.setError("Tolong isi Email anda!")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(etPassword.text.toString())){
                etPassword.setError("Tolong isi Password anda!")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(etNama.text.toString())){
                etNama.setError("Tolong isi Nama anda!")
                return@setOnClickListener
            }else if (TextUtils.isEmpty(etCountry.text.toString())){
                etCountry.setError("Tolong Asal anda!")
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener() {
                    if(it.isSuccessful){
                        val currentUser = auth.currentUser
                        val currentUserDb = databaseReference?.child((currentUser?.uid!!))
                        currentUserDb?.child("Nama")?.setValue(etNama.text.toString())
                        currentUserDb?.child("Country")?.setValue(etCountry.text.toString())
                        currentUserDb?.child("User")?.setValue(etUser.text.toString())
                        currentUserDb?.child("Email")?.setValue(etEmail.text.toString())
                        currentUserDb?.child("Password")?.setValue(etPassword.text.toString())

                        Toast.makeText(this@RegisterActivity, "Registrasi Berhasil.", Toast.LENGTH_LONG).show()
                        finish()
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registrasi gagal, silahkan coba lagi.", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left,R.anim.slide_to_right)
    }

}
