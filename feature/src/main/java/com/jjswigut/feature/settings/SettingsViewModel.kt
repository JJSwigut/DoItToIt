package com.jjswigut.feature.settings

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val prefs: SharedPreferences
) : ViewModel() {

    private val prefsEditor = prefs.edit()

    fun saveDarkModePref(isChecked: Boolean) {
        prefsEditor.putBoolean("darkmode", isChecked)
        prefsEditor.apply()
    }

    fun getDarkModePref(): Boolean {
        return prefs.getBoolean("darkmode", false)
    }
}