package com.adevinta.randomusers.allusers.viewmodel

import androidx.lifecycle.MutableLiveData
import com.adevinta.randomusers.allusers.model.User
import com.adevinta.randomusers.allusers.model.UsersResult
import com.adevinta.randomusers.common.utils.Resource

interface AllUsersViewModel {

    val usersResult : MutableLiveData<Resource<UsersResult>>
    val savedInDatabase : MutableLiveData<Resource<Boolean>>
    val deleteDatabase: MutableLiveData<Boolean>
    val usersDatabase: MutableLiveData<UsersResult>
    val error: MutableLiveData<String>

    fun getAllUsersByPage(page: Int)

    fun saveUsers(users: List<User>)

    fun deleteUsers(user: User)

    fun getAllUsersFromDataBase(page: Int)
}