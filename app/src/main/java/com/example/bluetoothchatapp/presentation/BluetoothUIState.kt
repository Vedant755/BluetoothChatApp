package com.example.bluetoothchatapp.presentation

import com.example.bluetoothchatapp.domain.chat.BluetoothDevice

data class BluetoothUIState(
    val scannedDevices: List<BluetoothDevice> = emptyList(),
    val pairedDevices: List<BluetoothDevice> = emptyList()

)
