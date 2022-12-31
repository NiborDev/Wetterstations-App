/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.net.wifi.ScanResult
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.*
import de.niborblog.wetterstationsapp.Bluetooth.connectToDevice
import de.niborblog.wetterstationsapp.Bluetooth.scanLeDevice
import de.niborblog.wetterstationsapp.Wifi.scanNetworks

val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
val bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
var discoveredDevices = mutableStateListOf<BluetoothDevice>()

var isNotEmpty = mutableStateOf(false)

var discNetworks = mutableStateListOf<ScanResult>()
var isNetworksEmpty = mutableStateOf(false)


@SuppressLint("MissingPermission")
@Composable
fun DevicePairDialog(onClose: () -> Unit, openSettingsDialog: () -> Unit) {

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
                    BluetoothDevices(close = onClose, openSettingsDialog = openSettingsDialog)
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
            }
        })
    if (discoveredDevices.isNotEmpty()){
        isNotEmpty.value = true
    }
}

@Composable
fun WifiSettingsDialog(onClose: () -> Unit) {
    //TODO: SCann Wifi Geräte und deren SSID

    scanNetworks(context = LocalContext.current)

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
                Text(text = "WetterStationen Einrichten\n", fontSize = MaterialTheme.typography.titleMedium.fontSize, fontWeight = MaterialTheme.typography.titleMedium.fontWeight)
                Divider(modifier = Modifier.padding(top = 8.dp, bottom = 16.dp))
                if (isNetworksEmpty.value){
                    //TODO: List Networks
                    WiFiNetworks(onClose = onClose)
                }else {
                    Text(modifier = Modifier.fillMaxHeight(),text = "Suche nach Netzwerke...")
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
            }
        })
    if (discNetworks.isNotEmpty()){
        isNetworksEmpty.value = true
    }
}

@Composable
fun WiFiNetworks(onClose: () -> Unit) {
    val networkList = remember {
        discNetworks
    }.sortedBy { -it.level }
    var selectedIndex by remember {
        mutableStateOf(0)
    }
    val onItemClick = {index: Int -> selectedIndex = index}
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(networkList) { index, network ->
            NetworkItem(network = network, index = index, selected = selectedIndex == index, onClick = onItemClick, onClose = onClose )
            Divider()
        }
    }
}

@Composable
fun NetworkItem(
    network: ScanResult,
    index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier
        .fillMaxWidth()
        .height(35.dp)
        .clickable {
            Log.d(
                "NetworkItemClicked",
                "NetworkIndex:$index - NetworkSSID: ${network.SSID}"
            )
            //TODO: Send SSID to Arduino
            //TODO: Request Network Password (Change UI to insert Network Password)
        }
    ) {
        Text(text = network.SSID)
    }
    Log.d("NetworkList", network.SSID)
}

@SuppressLint("MissingPermission")
@Composable
fun BluetoothDevices(close: () -> Unit, openSettingsDialog: () -> Unit) {
    val deviceList = remember { discoveredDevices }
    var selectIndex by remember{mutableStateOf(0)}
    val onItemClick = {index: Int -> selectIndex = index}
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        itemsIndexed(deviceList) { index, device ->
            DeviceItem(device = device, index = index, selected = selectIndex == index, onClick = onItemClick, onClose = close, openSettingsDialog = openSettingsDialog )
            Divider()
        }
    }
}

@SuppressLint("MissingPermission")
@Composable
fun DeviceItem(
    device: BluetoothDevice,
    index: Int,
    selected: Boolean,
    onClick: (Int) -> Unit,
    onClose: () -> Unit,
    openSettingsDialog: () -> Unit
){
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
            connectToDevice(
                device,
                context = context,
                onClose = onClose,
                openSettingsDialog = openSettingsDialog
            )
        }
    ) {
        Text(text = "${device.name}")
        Text(text = "MAC: ${device.address}")
    }
    Log.d("BluetoothReceiver", device.name)
}
