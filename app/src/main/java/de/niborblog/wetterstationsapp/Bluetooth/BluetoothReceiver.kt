
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.toMutableStateList
import de.niborblog.wetterstationsapp.components.discoveredDevices
import de.niborblog.wetterstationsapp.utils.Constants.HARDWARE_NAME
import java.util.*

class BluetoothReceiver() : BroadcastReceiver() {


    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        if (BluetoothDevice.ACTION_FOUND == intent.action) {
            // Get the BluetoothDevice object from the Intent
            val device = intent.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
            // Add WetterStation Device to discoverd List
            if (device!!.name == HARDWARE_NAME){
                //check if Device already exist, so it was not added again
                if (!discoveredDevices.contains(device)){
                    discoveredDevices.add(device)
                }
            }
        }
    }
}
