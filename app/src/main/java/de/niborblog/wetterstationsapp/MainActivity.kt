/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp


import android.annotation.SuppressLint
import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.AndroidEntryPoint
import de.niborblog.wetterstationsapp.Bluetooth.*
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.components.BluetoothPermissions
import de.niborblog.wetterstationsapp.navigation.WetterSNavigation
import de.niborblog.wetterstationsapp.ui.theme.WetterstationsAppTheme

@SuppressLint("StaticFieldLeak")
var context: Context? = null;

//create Preference DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings" )


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
            context = LocalContext.current
            WetterStationsApp(multiplePermissionsList = multiplePermissionsList)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromDevice()
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
                //Bluetooth configurations
                CheckBluetoothStatus()
                initializeBluetooth(LocalContext.current)

                //Show Screen
                WetterSNavigation()
            }
        }
    }
}

