package com.amrabed.tripplan.data.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amrabed.tripplan.auth.Authenticator
import com.amrabed.tripplan.core.Role
import com.amrabed.tripplan.core.UserProfile
import com.amrabed.tripplan.data.repositories.UserRepository
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {
    var loading = MutableLiveData(false)
    var selectedUser = MutableLiveData<UserProfile>()
    val currentUser by lazy {
        MutableLiveData<UserProfile>().apply {
            if (Authenticator.user != null) {
                UserRepository.get(Authenticator.user!!.uid) { document, error ->
                    if (error != null) {
                        Log.w(UserViewModel::class.java.simpleName, error.toString())
                        return@get
                    }
                    value = if (document != null && document.exists()) {
                        document.toObject<UserProfile>()
                    } else {
                        val user = UserProfile(Authenticator.user!!)
                        UserRepository.create(user)
                        user
                    }
                }
            }
        }
    }

    internal val allUsers by lazy {
        MutableLiveData<MutableList<UserProfile>>().apply {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    loading.postValue(true)
                    UserRepository.read { snapshot, error ->
                        loading.postValue(false)

                        if (error != null) {
                            Log.w(TAG, error.toString())
                            return@read
                        }
                        postValue(snapshot!!.documents.map { d -> d.toObject<UserProfile>()!! }
                            .toMutableList())
                    }
                } catch (error: Exception) {
                    loading.postValue(true)
                }
            }
        }
    }

    fun add(user: UserProfile) {
        val currentUser = Authenticator.user!!
        val password = user.name!!.split(' ')[0] + "123456"
        Authenticator.createUser(user.email!!, password) { task ->
            if (task.isSuccessful) {
                user.id = task.result?.user!!.uid
                UserRepository.create(user)
            } else {
                Log.w(TAG, task.exception.toString())
            }
            Authenticator.setCurrentUser(currentUser)
        }
    }

    fun update(user: UserProfile) = UserRepository.update(user)
    fun delete(user: UserProfile) = UserRepository.delete(user)
    fun select(user: UserProfile) {
        selectedUser.value = user
    }

    fun isAdmin() = currentUser.value != null && currentUser.value!!.role == Role.ADMIN
    fun isManager() = currentUser.value != null && currentUser.value!!.role > Role.USER
    fun getRoles() = Role.values().filter { it <= currentUser.value?.role ?: Role.USER }
    fun hasHigherRole() = currentUser.value != null && selectedUser.value != null &&
            currentUser.value!!.role >= selectedUser.value!!.role

    fun updateRole(role: Role) {
        if (selectedUser.value != null) {
            selectedUser.value!!.role = role
            update(selectedUser.value!!)
        }
    }

    fun getUserId() = selectedUser.value?.id ?: currentUser.value?.id ?: Authenticator.user!!.uid

    companion object {
        private val TAG = UserViewModel::class.java.simpleName
    }
}