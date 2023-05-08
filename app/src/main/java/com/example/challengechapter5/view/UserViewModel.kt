package com.example.challengechapter5.view

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.challengechapter5.room.User
import com.example.challengechapter5.room.UserDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UserViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var userProfile : MutableLiveData<User>

    init {
        userProfile = MutableLiveData()
    }

    fun getDataUserProfile() : MutableLiveData<User>{
        return userProfile
    }

    fun getDataUser(email : String){
        GlobalScope.launch {
            val userDao = UserDatabase.getDatabase(getApplication())!!.userDao()
            val listUser = userDao.getProfileUser(email)
            userProfile.postValue(listUser)
        }
    }

    fun insertDataUser(user: User){
        val userDao = UserDatabase.getDatabase(getApplication())!!.userDao()
        userDao?.insertProfileUser(user)
    }

    fun updateDataUser(user: User){
        val userDao = UserDatabase.getDatabase(getApplication())!!.userDao()
        userDao?.updateProfileUser(user)
    }

}