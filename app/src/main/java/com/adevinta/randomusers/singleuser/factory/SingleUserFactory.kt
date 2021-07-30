package com.adevinta.randomusers.singleuser.factory

import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.User
import io.reactivex.disposables.Disposable

interface SingleUserFactory {

    val user: MutableLiveData<User>
    val errorString: MutableLiveData<String>
    fun getUserFromDatabase(uuid: String): Disposable
}