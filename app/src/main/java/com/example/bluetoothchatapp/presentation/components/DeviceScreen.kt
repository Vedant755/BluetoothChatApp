package com.example.bluetoothchatapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.bluetoothchatapp.presentation.BluetoothUIState

@Composable
fun DeviceScreen(
    state: BluetoothUIState,
    onStartScan: ()->Unit,
    onStopScan: ()->Unit
){
    Column(//there is only lazy column and the buttons at the bottom
        modifier = Modifier.fillMaxSize()
    ) {
        BluetoothDeviceList(pairedDevices = state.pairedDevices,
            scannedDevices = state.scannedDevices, onClick = {}
        , modifier = Modifier.fillMaxWidth().weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Button(onClick = onStartScan) {
                Text(text = "Start Scan")
            }
            Button(onClick = onStopScan) {
                Text(text = "StopScan")
            }
        }
    }
}

