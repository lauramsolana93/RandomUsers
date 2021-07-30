package com.adevinta.randomusers.allusers.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adevinta.randomusers.allusers.factory.AllUsersFactory
import com.adevinta.randomusers.allusers.model.Delete
import com.adevinta.randomusers.allusers.model.Info
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.model.UsersResult
import com.adevinta.randomusers.common.data.networkmodels.UsersResultsResponse
import com.adevinta.randomusers.common.utils.Resource
import com.adevinta.randomusers.common.utils.mapToUser
import kotlinx.coroutines.launch
import retrofit2.Response

class AllUsersViewModel(
    private val factory: AllUsersFactory
) : ViewModel() {

    val usersResult = MutableLiveData<Resource<UsersResult>>()
    val savedInDatabase = MutableLiveData<Resource<Boolean>>()
    val deleteDatabase: MutableLiveData<Boolean> by lazy {
        factory.delete
    }
    val usersDatabase: MutableLiveData<UsersResult> by lazy {
        factory.users
    }
    val error: MutableLiveData<String> by lazy {
        factory.errorString
    }
    private val deletedUsers: MutableLiveData<List<Delete>> by lazy {
        factory.deletedUsers
    }

    init {
        factory.deletedUsers()
    }


    fun getAllUsersByPage(page: Int) {
        usersResult.postValue(Resource.Loading())

        viewModelScope.launch {
            try {
                val response = factory.getPagedUsersFormNetwork(page)
                usersResult.postValue(handleResponse(response))
            } catch (t: Throwable) {
                usersResult.postValue(Resource.Error("Couldn't get users"))
            }
        }
    }

    private fun handleResponse(response: Response<UsersResultsResponse>): Resource<UsersResult> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                val resultsList = resultResponse.resultsUsers
                val users = arrayListOf<User>()

                deletedUsers.value?.let { deletedUsers ->
                    if (deletedUsers.isNotEmpty()) {
                        deletedUsers.forEach { deleteUuid ->
                            resultsList.forEach { userResponse ->
                                if (deleteUuid.uuid != userResponse.loginResponse.uuid) {
                                    users.add(userResponse.mapToUser())
                                }
                            }
                        }
                    } else {
                        resultsList.forEach { userResponse ->
                            users.add(userResponse.mapToUser())
                        }
                    }

                }

                val info = Info(resultResponse.info.page)
                val usersResult = UsersResult(users, info)
                return Resource.Success(usersResult)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveUsers(users: List<User>) {
        factory.saveUserToDataBase(users)
    }

    fun deleteUsers(user: User) {
        var delete = Delete(user.uuid)
        try {
            factory.deleteUsersFromDataBase(user)
            factory.addDeleteUsers(delete)
        } catch (t: Throwable) {
            Log.e("AllUsersViewModel", "Error delete user")
        }

    }

    fun getAllUsersFromDataBase(page: Int) {
        try {
            factory.getPagedUsersFromDatabase(page = page)

        } catch (t: Throwable) {
            Log.e("AllUsersViewModel", "Error get users from database")
        }
    }
}