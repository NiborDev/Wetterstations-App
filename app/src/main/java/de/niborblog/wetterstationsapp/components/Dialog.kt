/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.*
import de.niborblog.wetterstationsapp.Bluetooth.BluetoothReceiver

var discoveredDevices = mutableStateListOf<BluetoothDevice>()

var isNotEmpty = mutableStateOf(false)

@SuppressLint("MissingPermission")
@Composable
fun DevicePairDialog(onClose: () -> Unit) {
    val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    //set BluetoothReceiver
    val receiver = BluetoothReceiver()
    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    registerReceiver(LocalContext.current, receiver, filter, RECEIVER_EXPORTED)
    //Scan devices that are not paired
    bluetoothAdapter.startDiscovery()

    //show Dialog to show WetterStation Devices
    //TODO: Make the device Checkable and add a Connect Button to Connect with device
    AlertDialog(
        onDismissRequest = {},
        text = {
            if (isNotEmpty.value){
                BluetoothDevices()
            }else {
                Text(text = "Suche nach WetterStationen...")
            }

        }, buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = onClose) {
                    Text(text = "Schließen")
                }
                Button(
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = Color.Green),
                    enabled = isNotEmpty.value,
                onClick = {/* TODO: */ }) {
                Text(text = "Verbinden")
            }
            }
        })
    if (discoveredDevices.isNotEmpty()){
        isNotEmpty.value = true
    }
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDevices() {
    val deviceList = remember { discoveredDevices }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp)
    ) {
        items(deviceList) { device ->
            Text(text = "${device.name} -- ${device.address}")
            Log.d("BluetoothReceiver", device.name)
        }
    }
}
