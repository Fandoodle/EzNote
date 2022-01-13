package com.irfanapp.eznoteapp

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_editprofile.*
import kotlinx.android.synthetic.main.activity_register.*
import java.io.ByteArrayOutputStream

class EditProfileActivity : AppCompatActivity() {
    companion object{
        const val REQUEST_CAMERA = 100
    }

    private lateinit var imageUri : Uri
    private lateinit var auth: FirebaseAuth

    var database : FirebaseDatabase? = null
    var databaseReference : DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        imageView10.setOnClickListener {
            intentCamera()
        }

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance("https://covid-info-38247-default-rtdb.asia-southeast1.firebasedatabase.app/")
        databaseReference = database?.reference!!.child("profile")

        val user = auth.currentUser

        if (user != null){
            if (user.photoUrl != null){
                Picasso.get().load(user.photoUrl).into(imageView10)
            }else{
                Picasso.get().load("https://picsum.photos/id/1002/4312/2868").into(imageView10)
            }
            editprofile()
        }
    }

    private fun editprofile() {
        val user = auth.currentUser
        val userreference = databaseReference?.child(user?.uid!!)
        button6.setOnClickListener {
            val image = when{
                ::imageUri.isInitialized -> imageUri
                user?.photoUrl == null -> Uri.parse("https://picsum.photos/id/1002/4312/2868")
                else -> user.photoUrl
            }
            val userData = User(etNama.text.toString(), etUser.text.toString(), etCountry.text.toString() )

            userreference?.updateChildren(userData.getMap())!!.addOnSuccessListener {
                Toast.makeText(this, "User data updated!", Toast.LENGTH_SHORT).show()
                val intentprofile = Intent(this@EditProfileActivity, ProfileActivity::class.java)
                startActivity(intentprofile)
            }.addOnFailureListener{
            }

            UserProfileChangeRequest.Builder()
                .setPhotoUri(image)
                .build().also{
                    user?.updateProfile(it)?.addOnCompleteListener {
                        if (it.isSuccessful){
                        }else{
                            Toast.makeText(this@EditProfileActivity, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }
    }

    private fun intentCamera() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { intent ->
            this@EditProfileActivity.packageManager?.let {
                intent.resolveActivity(it).also {
                    startActivityForResult(intent, REQUEST_CAMERA)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK){
            val imgBitmap = data?.extras?.get("data") as Bitmap
            uploadImage(imgBitmap)
        }
    }

    private fun uploadImage(imgBitmap: Bitmap) {
        val baos = ByteArrayOutputStream()
        val ref = FirebaseStorage.getInstance().reference.child("img/${FirebaseAuth.getInstance().currentUser?.uid}")

        imgBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val image = baos.toByteArray()

        ref.putBytes(image)
            .addOnCompleteListener{
                if (it.isSuccessful){
                    ref.downloadUrl.addOnCompleteListener {
                        it.result?.let {
                            imageUri = it
                            imageView10.setImageBitmap(imgBitmap)
                        }
                    }
                }
            }
    }


}