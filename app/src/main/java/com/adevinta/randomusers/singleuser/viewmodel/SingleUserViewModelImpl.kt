package com.adevinta.randomusers.singleuser.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.singleuser.factory.SingleUserFactory

class SingleUserViewModelImpl(
    private val factory: SingleUserFactory,
) : SingleUserViewModel, ViewModel() {

    override val user: MutableLiveData<User> by lazy {
        factory.user
    }
    override val error: MutableLiveData<String> by lazy {
        factory.errorString
    }

    override fun getUserFromDataBase(uuid: String) {
        factory.getUserFromDatabase(uuid)
    }


}