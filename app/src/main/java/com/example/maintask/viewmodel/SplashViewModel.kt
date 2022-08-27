package com.example.maintask.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SplashViewModel: ViewModel() {
    private val _databaseWasInitialized = MutableLiveData<Boolean>()
    val databaseWasInitialized: LiveData<Boolean>
        get() = _databaseWasInitialized

    private val _firebaseData = MutableLiveData<Boolean>()
    val firebaseData: LiveData<Boolean>
        get() = _firebaseData

    private val _hasAnimationEnded = MutableLiveData<Boolean>()
    val hasAnimationEnded: LiveData<Boolean>
        get() = _hasAnimationEnded

    fun initDatabase(block: () -> Unit) {
        viewModelScope.launch {
            _databaseWasInitialized.value = false
            block()
            _databaseWasInitialized.value = true
        }
    }

    fun launchFirebaseData(block: () -> Unit) {
        viewModelScope.launch {
            _firebaseData.value = false
            block()
            _firebaseData.value = true
        }
    }

    fun setHasAnimationEnded(hasEnded: Boolean) {
        this._hasAnimationEnded.value = hasEnded
    }
}