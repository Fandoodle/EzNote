package com.irfanapp.eznoteapp

import android.content.Intent
import android.os.Bundle

import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_profile.*

import android.net.Uri

import com.google.firebase.storage.FirebaseStorage

import android.widget.Toast


import com.google.firebase.storage.StorageReference

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.provider.MediaStore

import com.squareup.picasso.Picasso
import java.util.*
import com.google.firebase.database.DatabaseError

import com.google.firebase.database.DataSnapshot

import com.google.firebase.database.ValueEventListener

import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import java.io.IOException
import com.firebase.ui.storage.images.FirebaseImageLoader

import com.bumptech.glide.Glide
import com.google.firebase.auth.UserProfileChangeRequest
import java.io.ByteArrayOutputStream
import androidx.annotation.NonNull
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_profile.button3
import kotlinx.android.synthetic.main.activity_profile.button4
import kotlinx.android.synthetic.main.activity_profile.button5
import kotlinx.android.synthetic.main.activity_profile.textView14


class ProfileActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://login-register-2b2ec-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = database?.reference!!.child("profile")


        val MainBtn = findViewById<Button>(R.id.button6)
        MainBtn.setOnClickListener {
            val toMainActivity = Intent(this, MainActivity::class.java)
            startActivity(toMainActivity)
        }
        val TextView = findViewById<Button>(R.id.button2)
        TextView.setOnClickListener {
            val intent= Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        val TextView2 = findViewById<Button>(R.id.button9)
        TextView2.setOnClickListener {
            val intent= Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }
        val user = auth.currentUser

        if (user != null){
            if (user.photoUrl != null){
                Picasso.get().load(user.photoUrl).into(logo)
            }else{
                Picasso.get().load("https://picsum.photos/id/1002/4312/2868").into(logo)
            }
            loadProfile()
        }


    }

    private fun loadProfile(){

        val user = auth.currentUser
        val userRef = databaseReference?.child(user?.uid!!)
        button4.text = user?.email

        userRef?.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                textView14.text = snapshot.child("Nama").value.toString()
                button5.text = snapshot.child("User").value.toString()
                button3.text = snapshot.child("Country").value.toString()

            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        button2.setOnClickListener{
            auth.signOut()
            startActivity(Intent(this@ProfileActivity, LoginActivity::class.java))
            finish()
        }

    }

}