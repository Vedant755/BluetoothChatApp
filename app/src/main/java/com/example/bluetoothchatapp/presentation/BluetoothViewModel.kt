package com.example.bluetoothchatapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bluetoothchatapp.domain.chat.BluetoothController
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class BluetoothViewModel @Inject constructor(
    private val bluetoothController: BluetoothController
):ViewModel() {

    private val _state = MutableStateFlow(BluetoothUIState())
    val state = combine(
        bluetoothController.scannedDevices,
        bluetoothController.pairedDevices,
        _state
    ){ scannedDevices,pairedDevices, state ->
        state.copy(
            scannedDevices = scannedDevices,
            pairedDevices=pairedDevices
        )
        //stateIn just converts the normal flow into stateflow and caches the latest value
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000),_state.value)

    fun startScan(){
        bluetoothController.startDiscovery()
    }
    fun stopScan(){
        bluetoothController.stopDiscovery()
    }
}