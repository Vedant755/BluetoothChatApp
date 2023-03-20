package com.example.bluetoothchatapp.domain.chat

import kotlinx.coroutines.flow.StateFlow

//this is the heart of our app
interface BluetoothController {
    val  scannedDevices: StateFlow<List<BluetoothDevice>>
    val pairedDevices: StateFlow<List<BluetoothDevice>>

    fun startDiscovery() //start looking for devices in the environment
    fun stopDiscovery()

    fun release()//free up all the memoryff
}