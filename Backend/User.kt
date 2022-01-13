package com.irfanapp.eznoteapp

import com.google.firebase.database.Exclude

data class User (var username : String?,var Nama : String?,var Country : String?){
    @Exclude
    fun getMap(): Map<String, Any?> {
        return mapOf(
            "User" to username,
            "Nama" to Nama,
            "Country" to Country,
        )
    }
}