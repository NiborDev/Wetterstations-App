/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.os.Handler
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.*
import de.niborblog.wetterstationsapp.Bluetooth.connectToDevice
import de.niborblog.wetterstationsapp.Bluetooth.scanLeDevice
import de.niborblog.wetterstationsapp.utils.Constants.SCAN_PERIOD

val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
var discoveredDevices = mutableStateListOf<BluetoothDevice>()

var isNotEmpty = mutableStateOf(false)


@SuppressLint("MissingPermission")
@Composable
fun DevicePairDialog(onClose: () -> Unit) {

    //set BluetoothReceiver
    /*val receiver = BluetoothReceiver()
    val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
    registerReceiver(LocalContext.current, receiver, filter, RECEIVER_EXPORTED)
    //Scan devices that are not paired

    bluetoothAdapter.startDiscovery()*/

    //new Method
    scanLeDevice()

    //show Dialog to show WetterStation Devices
    //TODO: Make the device Checkable and add a Connect Button to Connect with device
    AlertDialog(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        onDismissRequest = {},
        text = {
            Column(modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center) {
                Text(text = "WetterStationen Verbinden\n", fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)
                Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                if (isNotEmpty.value){
                    BluetoothDevices()
                }else {
                    Text(modifier = Modifier.fillMaxHeight(),text = "Suche nach WetterStationen...")
                }
            }

        }, buttons = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
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
    var selectIndex by remember{mutableStateOf(0)}
    val onItemClick = {index: Int -> selectIndex = index}
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(deviceList) { index, device ->
            DeviceItem(device = device, index = index, selected = selectIndex == index, onClick = onItemClick )

        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(device: BluetoothDevice, index: Int, selected: Boolean, onClick: (Int) -> Unit){
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(35.dp)
        .clickable {
            Log.d(
                "DeviceItemClicked",
                "DeviceIndex:$index - DeviceName: ${device.name}"
            )
            bluetoothAdapter.cancelDiscovery()
            connectToDevice(device, context = context)
        }
    ) {
        Text(text = "${device.name}")
        Text(text = "MAC: ${device.address}")
    }
    Log.d("BluetoothReceiver", device.name)
}
