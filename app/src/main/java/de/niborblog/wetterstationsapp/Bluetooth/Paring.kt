/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.companion.CompanionDeviceManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@SuppressLint("UnrememberedMutableState", "MissingPermission")
@Composable
fun CheckBluetoothStatus() {
    // Get a reference to the BluetoothAdapter
    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    // Use mutableStateOf to create a mutable state variable to track the Bluetooth status
    var bluetoothStatus by mutableStateOf(bluetoothAdapter?.isEnabled)

    // Use the BluetoothAdapter.ACTION_STATE_CHANGED broadcast receiver to update the Bluetooth status
    // when it changes
    val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR)
            if (state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_OFF) {
                bluetoothStatus = (state == BluetoothAdapter.STATE_ON)
            }
        }
    }

    // Register the broadcast receiver to listen for Bluetooth state changes
    val filter = IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
    LocalContext.current.registerReceiver(broadcastReceiver, filter)

    // Use the Bluetooth status to update the UI
    if (bluetoothStatus != true) {
        // Bluetooth is disabled -> Request to Enable it
        EnableBluetooth()
    }
}

@SuppressLint("MissingPermission")
@Composable
fun EnableBluetooth() {
    val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    LocalContext.current.startActivity(enableBtIntent)
}

