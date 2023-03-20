package com.example.bluetoothchatapp.data.chat

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.example.bluetoothchatapp.domain.chat.BluetoothController
import com.example.bluetoothchatapp.domain.chat.BluetoothDevice
import com.example.bluetoothchatapp.domain.chat.BluetoothDeviceDomain
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@SuppressLint("MissingPermission")
class AndroidBluetoothController(
    private val context: Context
): BluetoothController {

    private val bluetoothManager by lazy {
        context.getSystemService(BluetoothManager::class.java)
    }
    // this below consists of all the functionalities that are passed such as scanned,paired and etc
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter//null check as the device won't support bluetooth
    }

    private val _scannedDevices = MutableStateFlow<List<BluetoothDeviceDomain>>(emptyList())
    override val scannedDevices: StateFlow<List<BluetoothDevice>>
        get() = _scannedDevices.asStateFlow()
//here we use first private as it prevents from change and then it gets accessed out loud in the public
    private val _pairedDevices = MutableStateFlow<List<BluetoothDeviceDomain>> (emptyList () )
    override val pairedDevices: StateFlow<List<BluetoothDevice>>
        get() = _pairedDevices.asStateFlow()

    private val foundDeviceReceiver=FoundDeviceReceiver{device->
        _scannedDevices.update {devices->
                val newDevice= device.toBluetoothClassDomain()
                if (newDevice in devices){
                    devices
                }else{
                    devices+newDevice
                }
        }

    }

    init {
        updatePairedDevices()
    }
    override fun startDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }

        context.registerReceiver(//here we call the broadcastreceiver we defined in foundeviceReceiver
            foundDeviceReceiver,
            IntentFilter(android.bluetooth.BluetoothDevice.ACTION_FOUND)
        )


        updatePairedDevices()
        bluetoothAdapter?.startDiscovery()
    }

    override fun stopDiscovery() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_SCAN)){
            return
        }
        bluetoothAdapter?.cancelDiscovery()
    }

    override fun release() {
        context.unregisterReceiver(foundDeviceReceiver)
    }


    private fun updatePairedDevices(){ //it just updates the paired devices present in mobile.
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)){
            return
        }
        bluetoothAdapter
            ?.bondedDevices
            ?.map {  it.toBluetoothClassDomain()  }
            ?.also {devices->
                _pairedDevices.update { devices } }
    }

    private fun hasPermission(permission: String): Boolean{
        return context.checkSelfPermission((permission))==PackageManager.PERMISSION_GRANTED
    }
}