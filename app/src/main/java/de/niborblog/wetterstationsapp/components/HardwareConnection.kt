/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.*
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import de.niborblog.wetterstationsapp.Bluetooth.BluetoothBroadcastReceiver

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
fun BluetoothConnection(context: Context) {
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    //Scanne Geräte in der Nähe
    Log.i("BluetoothDevices", "Start Scan Devices")
    val discoveryStarted = bluetoothAdapter?.startDiscovery()

    if (discoveryStarted == true) {
        Log.i("BluetoothDevices", "Scan Devices successfully")
        val blBroadcastReceiver = BluetoothBroadcastReceiver()
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(context,blBroadcastReceiver, filter,RECEIVER_NOT_EXPORTED)
    }else{
        Log.i("BluetoothDevices", "Scan Devices Failture")

    }

}