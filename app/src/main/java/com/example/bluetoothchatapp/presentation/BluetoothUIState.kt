package com.example.bluetoothchatapp.presentation

import com.example.bluetoothchatapp.domain.chat.BluetoothDevice

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList(),
    val isConnected: Boolean = false,
    val isConnecting: Boolean=false,
    val error: String?=null,

)
