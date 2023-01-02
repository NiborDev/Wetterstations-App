/**************************************************************************************************
 * Copyright (c) 2023.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.util.Log
import java.util.*

// Declare variables for Bluetooth objects and state
private var bluetoothAdapter: BluetoothAdapter? = null
private var bluetoothLeScanner: BluetoothLeScanner? = null
private var bluetoothGatt: BluetoothGatt? = null
private var bluetoothDevice: BluetoothDevice? = null
private var temperatureCharacteristic: BluetoothGattCharacteristic? = null
private var humidityCharacteristic: BluetoothGattCharacteristic? = null
private var isConnected = false
private var isTemperatureNotifyEnabled = false
private var isHumidityNotifyEnabled = false

private val serviceUuid = "280e14dd-6d9f-48db-9282-948e27efea5a"
private val temperatureCharacteristicUuid = "99448029-6429-4271-aff0-abe68bc84697"
private val humidityCharacteristicUuid = "26b616ef-063e-42ec-984f-e49556831f4e"

fun initializeBluetooth(context: Context) {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothAdapter = bluetoothManager.adapter
    bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
}

@SuppressLint("MissingPermission")
fun startScanning(context: Context) {
    val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let { station ->
                if (station.device.name == "WetterStation by M&R") {
                    bluetoothDevice = station.device
                    stopScanning()
                    autoConnectToDevice(context)
                }
            }
        }
    }
    bluetoothLeScanner?.startScan(scanCallback)
}

// Stop scanning for devices
@SuppressLint("MissingPermission")
fun stopScanning() {
    bluetoothLeScanner?.stopScan(scanCallback)
}

// Connect to the device
@SuppressLint("MissingPermission")
fun autoConnectToDevice(context: Context) {
    if (bluetoothDevice == null) {
        return
    }
    bluetoothGatt = bluetoothDevice?.connectGatt(context, false, gattCallback)
}

@SuppressLint("MissingPermission")
fun disconnectFromDevice() {
    if (bluetoothGatt == null) {
        return
    }
    bluetoothGatt?.disconnect()
    bluetoothGatt?.close()
    bluetoothGatt = null
    bluetoothDevice = null
    temperatureCharacteristic = null
    humidityCharacteristic = null
    isConnected = false
}

// Enable notifications for the temperature characteristic
@SuppressLint("MissingPermission")
fun enableTemperatureNotify() {
    if (temperatureCharacteristic == null) {
        return
    }
    bluetoothGatt?.setCharacteristicNotification(temperatureCharacteristic, true)
    val descriptor = temperatureCharacteristic?.getDescriptor(
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    )
    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    bluetoothGatt?.writeDescriptor(descriptor)
    isTemperatureNotifyEnabled = true
}

// Enable notifications for the humidity characteristic
@SuppressLint("MissingPermission")
fun enableHumidityNotify() {
    if (humidityCharacteristic == null) {
        return
    }
    bluetoothGatt?.setCharacteristicNotification(humidityCharacteristic, true)
    val descriptor = humidityCharacteristic?.getDescriptor(
        UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
    )
    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    bluetoothGatt?.writeDescriptor(descriptor)
    isHumidityNotifyEnabled = true
}

// Callback for handling events related to the Bluetooth GATT
private val gattCallback = object : BluetoothGattCallback() {
    @SuppressLint("MissingPermission")
    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)
        if (newState == BluetoothGatt.STATE_CONNECTED) {
            Log.i("AutoBluetoothConnect", "Connected!")
            isConnected = true
            gatt?.discoverServices()
        } else if (newState == BluetoothGatt.STATE_DISCONNECTED) {
            Log.i("AutoBluetoothConnect", "Disconnected!")
            isConnected = false
            temperatureCharacteristic = null
            humidityCharacteristic = null
            isTemperatureNotifyEnabled = false
            isHumidityNotifyEnabled = false
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        val service = gatt?.getService(UUID.fromString(serviceUuid))
        if (service == null) {
            return
        }
        temperatureCharacteristic =
            service.getCharacteristic(UUID.fromString(temperatureCharacteristicUuid))
        Log.i("CHARACTERISTIC_DATA", "Characteristic: $temperatureCharacteristicUuid")
        humidityCharacteristic = service.getCharacteristic(UUID.fromString(humidityCharacteristicUuid))
        Log.i("CHARACTERISTIC_DATA", "Characteristic: $humidityCharacteristicUuid")
        enableTemperatureNotify()
        enableHumidityNotify()

    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        super.onCharacteristicChanged(gatt, characteristic)
        if (characteristic?.uuid == UUID.fromString(temperatureCharacteristicUuid)) {
            // Update the temperature value on the UI
            if (characteristic != null) {
                val data = characteristic.value
                val dataString = String(data)
                //TODO: UI Designen und entsprechend updaten
                Log.d("CHARACTERISTIC_DATA", "UUID: $temperatureCharacteristicUuid data: $dataString")
            }
        }
        if (characteristic?.uuid == UUID.fromString(humidityCharacteristicUuid)) {
            // Update the humidity value on the UI
            if (characteristic != null) {
                val data = characteristic.value
                val dataString = String(data)
                //TODO: UI Designen und entsprechend updaten
                Log.d("CHARACTERISTIC_DATA", "UUID: $humidityCharacteristicUuid data: $dataString")
            }
        }
    }
}