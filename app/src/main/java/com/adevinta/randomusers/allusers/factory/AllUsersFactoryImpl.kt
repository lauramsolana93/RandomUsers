package com.adevinta.randomusers.allusers.factory

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.Delete
import com.adevinta.randomusers.allusers.model.Info
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.model.UsersResult
import com.adevinta.randomusers.allusers.repository.AllUsersRepository
import com.adevinta.randomusers.common.data.databasemodels.entity.UserEntity
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import com.adevinta.randomusers.common.utils.mapToDeleteEntity
import com.adevinta.randomusers.common.utils.mapToListOfDelete
import com.adevinta.randomusers.common.utils.mapToUserEntity
import com.adevinta.randomusers.common.utils.mapToUserList
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response

class AllUsersFactoryImpl(
    private val repository: AllUsersRepository
) : AllUsersFactory {

    override val users: MutableLiveData<UsersResult> by lazy {
        MutableLiveData<UsersResult>()
    }

    override val errorString: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    override val delete: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }

    override val deletedUsers: MutableLiveData<List<Delete>> by lazy {
        MutableLiveData<List<Delete>>()
    }

    val compositeDisposable = CompositeDisposable()


    override suspend fun getPagedUsersFormNetwork(page: Int): Response<UsersResultsResponse> {
        return repository.getPagedUsersFormNetwork(page)
    }

    override fun saveUserToDataBase(users: List<User>): Disposable {
        val userEntityList = ArrayList<UserEntity>()
        users.forEach {
            userEntityList.add(it.mapToUserEntity())
        }
        return repository.saveUsersToDatabase(userEntityList)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete {
                Log.e("DATABASE_ERROR", "${it.message}")
                true
            }.doOnComplete {
                Log.e("DATABASE_COMPLETE", "DONE")
            }
            .subscribe()

    }

    override fun getPagedUsersFromDatabase(page: Int): Disposable {
        return repository.getPagedUsersFromDatabase(page)
            .map { it.mapToUserList() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> users.value = UsersResult(data, Info(page + 1)) },
                { error -> errorString.value = error.message }
            )

    }

    override fun deleteUsersFromDataBase(user: User): Disposable {
        val user = user.mapToUserEntity()
        return repository.deleteUserFromDatabase(user)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete { throwable ->
                errorString.value = throwable.message
                true
            }
            .doOnComplete {
                delete.value = true
            }
            .subscribe()

    }

    override fun deletedUsers(): Disposable {
        return repository.getDeleteUsers()
            .map { it.mapToListOfDelete() }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { data -> deletedUsers.value = data },
                { error -> errorString.value = error.message }
            )
    }

    override fun addDeleteUsers(del: Delete): Disposable {
        val deleteEntity = del.mapToDeleteEntity()
        return repository.addDeleteUsersToDataBase(deleteEntity)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .onErrorComplete { throwable ->
                errorString.value = throwable.message
                true
            }
            .doOnComplete {

            }
            .subscribe()
    }
}