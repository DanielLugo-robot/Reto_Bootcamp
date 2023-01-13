package com.example.retobootcampmobilesophos2022.view.prefs

import android.content.Context

class Prefs (val context : Context){

    val SHARED_NAME = "Mydtb"
    val SHARED_USER_EMAIL = "useremail"
    val SHARED_USER_ACCESS = "useraccess"
    val SHARED_USER_NAME = "username"
    val storage = context.getSharedPreferences(SHARED_NAME, 0)

    fun saveEmail(email : String){
        storage.edit().putString(SHARED_USER_EMAIL, email).apply()
    }

    fun getEmail():String{
        return storage.getString(SHARED_USER_EMAIL, "")!!
    }

    fun saveAccess(access : Boolean){
        storage.edit().putBoolean(SHARED_USER_ACCESS, access).apply()
    }

    fun getAccess():Boolean{
        return storage.getBoolean(SHARED_USER_ACCESS, false)!!
    }

    fun saveName(name : String){
        storage.edit().putString(SHARED_USER_NAME, name).apply()
    }

    fun getUserName():String{
        return storage.getString(SHARED_USER_NAME, "")!!
    }

}