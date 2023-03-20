package com.example.bluetoothchatapp

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.hilt.navigation.compose.hiltViewModel
import com.example.bluetoothchatapp.presentation.BluetoothViewModel
import com.example.bluetoothchatapp.presentation.components.DeviceScreen
import com.example.bluetoothchatapp.ui.theme.BluetoothChatAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val bluetoothManager by lazy {
        applicationContext.getSystemService(BluetoothManager::class.java)
    }
    // this below consists of all the functionalities that are passed such as scanned,paired and etc
    private val bluetoothAdapter by lazy {
        bluetoothManager?.adapter//null check as the device won't support bluetooth
    }

    private val isBluetoothEnabled: Boolean
        get() = bluetoothAdapter?.isEnabled==true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val enableBluetoothLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){
            //not needed
        }

        val permissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
        ){perms->
            val canEnableBluetooth=if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
                perms[Manifest.permission.BLUETOOTH_CONNECT]==true
            }else true

            if (canEnableBluetooth&&!isBluetoothEnabled){
                enableBluetoothLauncher.launch(
                    Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                )
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S){
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT,
                )
            )
        }
        setContent {
            BluetoothChatAppTheme {
                val viewModel=hiltViewModel<BluetoothViewModel>()
                val state by viewModel.state.collectAsState()





                Surface(
                    color = MaterialTheme.colors.background
                ) {
                    DeviceScreen(
                        state = state,
                        onStartScan = viewModel::startScan,
                        onStopScan = viewModel::stopScan
                    )
                }
            }
        }
    }
}

