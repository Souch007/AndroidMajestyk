package com.majestykapps.arch.presentation.common

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    open var disposables: CompositeDisposable = CompositeDisposable()

    override fun onCleared() {
        disposables.clear()
    }
}