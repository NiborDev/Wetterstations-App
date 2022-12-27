/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp


import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint
import de.niborblog.wetterstationsapp.Bluetooth.CheckBluetoothStatus
import de.niborblog.wetterstationsapp.components.BluetoothPermissions
import de.niborblog.wetterstationsapp.navigation.WetterSNavigation
import de.niborblog.wetterstationsapp.ui.theme.WetterstationsAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val multiplePermissionsList =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            listOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                android.Manifest.permission.BLUETOOTH_CONNECT,
                android.Manifest.permission.BLUETOOTH_SCAN
            )
        } else {
            listOf(
                android.Manifest.permission.BLUETOOTH,
                android.Manifest.permission.BLUETOOTH_ADMIN,
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION,
            )
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //show Animation
        installSplashScreen()
        setContent()
        {
            WetterStationsApp(multiplePermissionsList = multiplePermissionsList)
        }

    }
}


@SuppressLint("MissingPermission")
@Composable
fun WetterStationsApp(
    multiplePermissionsList: List<String>
) {
    WetterstationsAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                //Request Bluetooth Berechtigungen
                BluetoothPermissions(multiplePermissionsList = multiplePermissionsList)
                //TODO: Check if Location Enabled
                CheckBluetoothStatus()

                WetterSNavigation()
            }
        }
    }
}

