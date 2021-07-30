package com.adevinta.randomusers.singleuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.singleuser.factory.SingleUserFactory

class SingleUserViewModel(
    private val factory: SingleUserFactory,
) : ViewModel() {

    val user: MutableLiveData<User> by lazy {
        factory.user
    }
    val error: MutableLiveData<String> by lazy {
        factory.errorString
    }

    fun getUserFromDataBase(uuid: String) {
        factory.getUserFromDatabase(uuid)
    }


}