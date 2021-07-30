package com.adevinta.randomusers.singleuser.factory

import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.common.utils.mapToUser
import com.adevinta.randomusers.singleuser.repository.SingleUserRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class SingleUserFactoryImpl(
    private val repository: SingleUserRepository
) : SingleUserFactory {

    override val user: MutableLiveData<User> by lazy {
        MutableLiveData<User>()
    }

    override val errorString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }


    override fun getUserFromDatabase(uuid: String): Disposable {
        return repository.getUserFromDatabase(uuid)
            .map { it.mapToUser() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> user.value = data },
                { error -> errorString.value = error.message }
            )

    }
}