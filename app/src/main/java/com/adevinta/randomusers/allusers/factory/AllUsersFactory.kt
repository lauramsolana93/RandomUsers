package com.adevinta.randomusers.allusers.factory

import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.Delete
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.model.UsersResult
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import io.reactivex.disposables.Disposable
import retrofit2.Response

interface AllUsersFactory {

    val users: MutableLiveData<UsersResult>
    val errorString: MutableLiveData<String>
    val delete: MutableLiveData<Boolean>
    val deletedUsers: MutableLiveData<List<Delete>>

    suspend fun getPagedUsersFormNetwork(page: Int): Response<UsersResultsResponse>
    fun saveUserToDataBase(users: List<User>): Disposable
    fun getPagedUsersFromDatabase(page: Int): Disposable
    fun deleteUsersFromDataBase(user: User): Disposable
    fun deletedUsers(): Disposable
    fun addDeleteUsers(delete: Delete): Disposable
}