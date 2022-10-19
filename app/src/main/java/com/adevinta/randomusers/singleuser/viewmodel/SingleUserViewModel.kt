package com.adevinta.randomusers.singleuser.viewmodel

import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.User

interface SingleUserViewModel {

    val user: MutableLiveData<User>
    val error: MutableLiveData<String>

    fun getUserFromDataBase(uuid: String)
}