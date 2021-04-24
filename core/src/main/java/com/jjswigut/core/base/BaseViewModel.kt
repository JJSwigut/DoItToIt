package com.jjswigut.core.base

import androidx.lifecycle.ViewModel
import androidx.navigation.NavDirections
import com.jjswigut.core.utils.NavCommand
import com.jjswigut.core.utils.SingleLiveEvent

abstract class BaseViewModel : ViewModel() {

    val navCommand = SingleLiveEvent<NavCommand>()

    fun navigate(directions: NavDirections) {
        navCommand.postValue(NavCommand.To(directions))
    }
}