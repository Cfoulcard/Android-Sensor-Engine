package com.ui.sensors.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.androidsensorengine.utils.Constants.AMBIENT_PREFS
import com.androidsensorengine.utils.Constants.ATMOSPHERE_PREFS
import com.androidsensorengine.utils.Constants.BATTERY_PREFS
import com.androidsensorengine.utils.Constants.GPS_PREFS
import com.androidsensorengine.utils.Constants.LIGHT_PREFS
import com.androidsensorengine.utils.Constants.MOIST_HUMID_PREFS
import com.androidsensorengine.utils.Constants.SOUND_PREFS
import com.androidsensorengine.utils.Constants.SYSTEM_PREFS
import com.preferences.AppSharedPrefs

class HomeScreenViewModel: ViewModel() {

    private val _soundCondition = MutableLiveData<Boolean>()
    val soundCondition: LiveData<Boolean> get() = _soundCondition

    private val _atmosphereCondition = MutableLiveData<Boolean>()
    val atmosphereCondition: LiveData<Boolean> get() = _atmosphereCondition

    private val _moistHumidCondition = MutableLiveData<Boolean>()
    val moistHumidCondition: LiveData<Boolean> get() = _moistHumidCondition

    private val _gpsCondition = MutableLiveData<Boolean>()
    val gpsCondition: LiveData<Boolean> get() = _gpsCondition

    private val _lightCondition = MutableLiveData<Boolean>()
    val lightCondition: LiveData<Boolean> get() = _lightCondition

    private val _systemCondition = MutableLiveData<Boolean>()
    val systemCondition: LiveData<Boolean> get() = _systemCondition

    private val _batteryCondition = MutableLiveData<Boolean>()
    val batteryCondition: LiveData<Boolean> get() = _batteryCondition

    private val _ambientCondition = MutableLiveData<Boolean>()
    val ambientCondition: LiveData<Boolean> get() = _ambientCondition

    init {
        // Initialize conditions
        _soundCondition.value = AppSharedPrefs().getCondition(SOUND_PREFS)
        _atmosphereCondition.value = AppSharedPrefs().getCondition(ATMOSPHERE_PREFS)
        _moistHumidCondition.value = AppSharedPrefs().getCondition(MOIST_HUMID_PREFS)
        _gpsCondition.value = AppSharedPrefs().getCondition(GPS_PREFS)
        _lightCondition.value = AppSharedPrefs().getCondition(LIGHT_PREFS)
        _systemCondition.value = AppSharedPrefs().getCondition(SYSTEM_PREFS)
        _batteryCondition.value = AppSharedPrefs().getCondition(BATTERY_PREFS)
        _ambientCondition.value = AppSharedPrefs().getCondition(AMBIENT_PREFS)
    }

    fun getNewPreferencesValues() {
        _soundCondition.value = AppSharedPrefs().getCondition(SOUND_PREFS)
        _atmosphereCondition.value = AppSharedPrefs().getCondition(ATMOSPHERE_PREFS)
        _moistHumidCondition.value = AppSharedPrefs().getCondition(MOIST_HUMID_PREFS)
        _gpsCondition.value = AppSharedPrefs().getCondition(GPS_PREFS)
        _lightCondition.value = AppSharedPrefs().getCondition(LIGHT_PREFS)
        _systemCondition.value = AppSharedPrefs().getCondition(SYSTEM_PREFS)
        _batteryCondition.value = AppSharedPrefs().getCondition(BATTERY_PREFS)
        _ambientCondition.value = AppSharedPrefs().getCondition(AMBIENT_PREFS)
    }
}