/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import de.niborblog.wetterstationsapp.components.*
import de.niborblog.wetterstationsapp.utils.Constants
import java.util.*

var error = false

val scanCallback = object : ScanCallback() {
    @SuppressLint("MissingPermission")
    override fun onScanResult(callbackType: Int, result: ScanResult) {
        val device = result.device
        // Add the device to the list of devices
        if (device.name != null) {
            if (!discoveredDevices.contains(device)){
                Log.d("BluetoothLE","Direct: Add ${device.name} to discoveredDevices List")
                discoveredDevices.add(device)
            }
        }
    }
    @SuppressLint("MissingPermission")
    override fun onBatchScanResults(results: List<ScanResult>) {
        for (result in results) {
            val device = result.device
            // Add the device to the list of devices
            if (device.name != null) {
                if (!discoveredDevices.contains(device)){
                    Log.d("BluetoothLE","BATCH: Add ${device.name} to discoveredDevices List")
                    discoveredDevices.add(device)
                }
            }
        }
    }
    override fun onScanFailed(errorCode: Int) {
        // Handle scan failure
        Log.e("BluetoothLE", "Scan Failed: $errorCode")
    }
}

val bluetoothGattCallback = object : BluetoothGattCallback() {
    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            // The device is connected, discover its services
            Log.i("BluetoothGatt", "Device connected - search Services")
            gatt.discoverServices()
            error = false
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            // The device is disconnected
            Log.i("BluetoothGatt", "Device disconnected")
            error = true
        }
    }
    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // The services are discovered, read a characteristic value
            Log.i("BluetoothGatt", "Device Services are discovered ${gatt.services[0].uuid}")
            val service = gatt.getService(UUID.fromString("280e14dd-6d9f-48db-9282-948e27efea5a"))
            val characteristic = service.getCharacteristic(UUID.fromString("99448029-6429-4271-aff0-abe68bc84697"))
            gatt.readCharacteristic(characteristic)
            error = false
        } else {
            // There was an error discovering the services
            Log.e("BluetoothGatt", "Services discovering error!")
            error = true
        }
    }

    override fun onCharacteristicRead(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic, status: Int) {
        if (status == BluetoothGatt.GATT_SUCCESS) {
            // The characteristic value was read, update the UI
            Log.i("BluetoothGatt", "Characteristic value was read")
            val value = characteristic.value
            error = false
        } else {
            // There was an error reading the characteristic value
            Log.e("BluetoothGatt", "Reading characteristic error!")
            error = true
        }
    }
}

private var scanning = false
private val handler = Handler()



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

@SuppressLint("MissingPermission")
fun scanLeDevice() {
    val scanFilter = ScanFilter.Builder().build()
    val scanSettings = ScanSettings.Builder().build()
    if(!scanning){
        handler.postDelayed({
            Log.d("BluetoothLE","Scanning stoped after time_period")
            scanning = false
            bluetoothLeScanner.stopScan(scanCallback)
        }, Constants.SCAN_PERIOD)
        Log.d("BluetoothLE","Scanning running...")
        scanning = true
        bluetoothLeScanner.startScan(listOf(scanFilter),scanSettings, scanCallback)
    }else{
        Log.d("BluetoothLE","Scanning stoped")
        scanning = false
        bluetoothLeScanner.stopScan(scanCallback)
    }
}

@SuppressLint("MissingPermission")
fun connectToDevice(
    device: BluetoothDevice,
    context: Context,
    openSettingsDialog: () -> Unit,
    onClose: () -> Unit
) {
    device.connectGatt(context, false, bluetoothGattCallback)
    if (!error){
        openSettingsDialog.invoke()
        onClose.invoke()
    }else {
        Toast.makeText(context,"Fehler beim Verbinden mit der WetterStation!",Toast.LENGTH_LONG).show()
    }
}
