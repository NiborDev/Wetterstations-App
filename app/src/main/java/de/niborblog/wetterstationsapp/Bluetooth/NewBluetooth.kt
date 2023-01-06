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
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.components.discoveredDevices
import java.time.LocalDateTime
import java.util.*


// Declare variables for Bluetooth objects and state
private var bluetoothAdapter: BluetoothAdapter? = null
private var bluetoothLeScanner: BluetoothLeScanner? = null
private var bluetoothGatt: BluetoothGatt? = null
private var bluetoothDevice: BluetoothDevice? = null
private var temperatureCharacteristic: BluetoothGattCharacteristic? = null
private var humidityCharacteristic: BluetoothGattCharacteristic? = null
private var coCharacteristic: BluetoothGattCharacteristic? = null
private var dateTimeCharacteristic: BluetoothGattCharacteristic? = null
private var isConnected = false
private var isTemperatureNotifyEnabled = false
private var isHumidityNotifyEnabled = false
private var isCoNotifyEnabled = false
private var isWriting = false

private val serviceUuid = "280e14dd-6d9f-48db-9282-948e27efea5a"
private val temperatureCharacteristicUuid = "99448029-6429-4271-aff0-abe68bc84697"
private val humidityCharacteristicUuid = "26b616ef-063e-42ec-984f-e49556831f4e"
private val coCharacteristicUuid = "1b01902e-20df-4cb8-aba1-a1258ecb91f7"

private val dateTimeCharacteristicUuid = "4fda653a-d5c0-4721-baa3-d658a01ec0f5"

private val CLIENT_CHARACTERISTIC_CONFIG_UUID = "00002902-0000-1000-8000-00805f9b34fb"

fun initializeBluetooth(context: Context) {
    val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    bluetoothAdapter = bluetoothManager.adapter
    bluetoothLeScanner = bluetoothAdapter?.bluetoothLeScanner
}

@SuppressLint("MissingPermission")
fun startScanning(context: Context, viewModel: HomeModel) {
    val scanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            result?.let { station ->
                if (station.device.name == "WetterStation by M&R") {
                    if (!discoveredDevices.contains(station.device)){
                        Log.d("BluetoothLE","BATCH: Add ${station.device.name} to discoveredDevices List")
                        discoveredDevices.add(station.device)
                    }
                    bluetoothDevice = station.device
                    stopScanning()
                    autoConnectToDevice(context, viewModel = viewModel)
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
fun autoConnectToDevice(context: Context, viewModel: HomeModel) {
    if (bluetoothDevice == null) {
        return
    }
    bluetoothGatt = bluetoothDevice?.connectGatt(context, false, gattCallback(viewModel = viewModel))
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
    coCharacteristic = null
    dateTimeCharacteristic = null
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
        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID)
    )
    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    bluetoothGatt?.writeDescriptor(descriptor)
    isTemperatureNotifyEnabled = areNotificationsEnabled(temperatureCharacteristic!!)
}

// Enable notifications for the humidity characteristic
@SuppressLint("MissingPermission")
fun enableHumidityNotify() {
    if (humidityCharacteristic == null) {
        return
    }
    bluetoothGatt?.setCharacteristicNotification(humidityCharacteristic, true)
    val descriptor = humidityCharacteristic?.getDescriptor(
        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID)
    )
    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    bluetoothGatt?.writeDescriptor(descriptor)
    isHumidityNotifyEnabled = areNotificationsEnabled(humidityCharacteristic!!)
}

// Enable notifications for the co characteristic
@SuppressLint("MissingPermission")
fun enableCoNotify() {
    if (coCharacteristic == null) {
        return
    }
    bluetoothGatt?.setCharacteristicNotification(coCharacteristic, true)
    val descriptor = coCharacteristic?.getDescriptor(
        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID)
    )
    descriptor?.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    bluetoothGatt?.writeDescriptor(descriptor)
    isCoNotifyEnabled = areNotificationsEnabled(coCharacteristic!!)
}

private fun areNotificationsEnabled(characteristic: BluetoothGattCharacteristic): Boolean {
    // Hole den Descriptor für die Benachrichtigungen
    val descriptor = characteristic.getDescriptor(
        UUID.fromString(CLIENT_CHARACTERISTIC_CONFIG_UUID)
    )

    // Überprüfe den Wert des Descriptors
    return descriptor != null && Arrays.equals(
        descriptor.value,
        BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
    )
}


@SuppressLint("MissingPermission")
fun sendData(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
    if (characteristic != null && gatt != null){
        if (dateTimeCharacteristic != null){
            val dayOfMonth = LocalDateTime.now().dayOfMonth
            val month = LocalDateTime.now().monthValue
            val year = LocalDateTime.now().year
            val minute = LocalDateTime.now().minute
            val hour = LocalDateTime.now().hour
            val date = "$dayOfMonth.$month.$year $hour:$minute"
            //send Date and Time as String

            characteristic.setValue(date)
            gatt.writeCharacteristic(characteristic)
            Log.d("SendDataOverBLE", date)
        }
    }
}

// Callback for handling events related to the Bluetooth GATT
class gattCallback(private val viewModel: HomeModel) : BluetoothGattCallback() {
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
            coCharacteristic = null
            dateTimeCharacteristic = null
            isTemperatureNotifyEnabled = false
            isHumidityNotifyEnabled = false
            isCoNotifyEnabled = false
        }
    }

    override fun onCharacteristicWrite(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?,
        status: Int
    ) {
        Log.v("WRITE_CHARAC", "onCharacteristicWrite: $status")
        isWriting = false
    }

    override fun onDescriptorWrite(
        gatt: BluetoothGatt?,
        descriptor: BluetoothGattDescriptor?,
        status: Int
    ) {
        Log.v("WRITE_DESCRIPTOR", "onDescriptorWrite: $status")
        isWriting = false
    }

    @SuppressLint("MissingPermission")
    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        if (status != BluetoothGatt.GATT_SUCCESS) {
            return
        }
        val service = gatt?.getService(UUID.fromString(serviceUuid))
        if (service == null) {
            Log.i("BLE_SERVICE", "Kein Service Gefunden!")
            return
        }
        temperatureCharacteristic = service.getCharacteristic(UUID.fromString(temperatureCharacteristicUuid))
        humidityCharacteristic = service.getCharacteristic(UUID.fromString(humidityCharacteristicUuid))
        dateTimeCharacteristic = service.getCharacteristic(UUID.fromString(dateTimeCharacteristicUuid))
        coCharacteristic = service.getCharacteristic(UUID.fromString(coCharacteristicUuid))

        //TODO: Logic evtl. anpassen!
        do {
            if(!isHumidityNotifyEnabled){
                enableHumidityNotify()
            }
            if (!isTemperatureNotifyEnabled){
                enableTemperatureNotify()
            }
            if (!isCoNotifyEnabled){
                enableCoNotify()
            }
            Log.i("NOTIFY_ENABLED", "CoNotify: $isCoNotifyEnabled TempNotify: $isTemperatureNotifyEnabled HumidityNotify: $isHumidityNotifyEnabled")
        }while (!isHumidityNotifyEnabled && !isTemperatureNotifyEnabled && !isCoNotifyEnabled)


        sendData(gatt= gatt, characteristic = dateTimeCharacteristic)



    }

    override fun onCharacteristicChanged(
        gatt: BluetoothGatt?,
        characteristic: BluetoothGattCharacteristic?
    ) {
        super.onCharacteristicChanged(gatt, characteristic)
        if (characteristic != null) {
            Log.i("CHARAC_DATA","${characteristic.uuid}")
        }
        if (characteristic?.uuid == UUID.fromString(humidityCharacteristicUuid)) {
            Log.d("CHARACTERISTIC_UUID", "HUMIDITY")
            // Update the humidity value on the UI
            if (characteristic != null) {
                val data = characteristic.value
                val dataString = String(data)
                viewModel.humidityData.value = dataString
                Log.d("CHARACTERISTIC_DATA_HUMIDITY", "UUID: $humidityCharacteristicUuid data: $dataString")
            }
        }
        if (characteristic?.uuid == UUID.fromString(temperatureCharacteristicUuid)) {
            Log.d("CHARACTERISTIC_UUID", "TEMP")
            // Update the temperature value on the UI
            if (characteristic != null) {
                val data = characteristic.value
                val dataString = String(data)
                viewModel.tempData.value = dataString
                Log.d("CHARACTERISTIC_DATA_TEMP", "UUID: $temperatureCharacteristicUuid data: $dataString")
            }
        }
        if (characteristic?.uuid == UUID.fromString(coCharacteristicUuid)){
            Log.d("CHARACTERISTIC_UUID", "CO")
            // Update the co value on the UI
            if (characteristic != null){
                val data = characteristic.value
                val dataString = String(data)
                viewModel.coData.value = dataString
                Log.d("CHARACTERISTIC_DATA_CO", "UUID: $coCharacteristicUuid data: $dataString")
            }
        }
        sendData(gatt=gatt, characteristic= dateTimeCharacteristic)
    }
}