/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Wifi

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.WifiManager
import android.util.Log
import androidx.core.content.ContextCompat.RECEIVER_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import de.niborblog.wetterstationsapp.components.discNetworks
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun scanNetworks(context: Context) {
    val wifiManager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager

    val wifiScanReceiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION){
                val scanResults = wifiManager.scanResults
                if (!scanResults.isEmpty()){
                    scanResults.forEach { network ->
                        if (network.SSID.isEmpty() || !network.SSID.equals("")){
                            val exist = discNetworks.find { it.SSID == network.SSID }
                            if (exist == null){ //Network doesnt exist
                                discNetworks.add(network)
                                Log.i("NetworkAdd","Network: ${network.SSID} added")
                            }else {
                                //Network exist
                                Log.i("NetworkAdd","Network exist")
                            }
                        }
                    }
                }
            }
        }
    }

    //Check if WIfi Enabled
    if (!wifiManager.isWifiEnabled()) {
        // Request that the user enable WiFi
        wifiManager.setWifiEnabled(true)
    }

    CoroutineScope(Dispatchers.Default).launch {
        //search for Networks
        wifiManager.startScan()

        delay(5000)

        wifiManager.disconnect()
    }



    val filter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
    registerReceiver(context,wifiScanReceiver, filter, RECEIVER_EXPORTED)
}