/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp


import android.app.Activity
import android.app.Application
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import de.niborblog.wetterstationsapp.Bluetooth.BluetoothBroadcastReceiver
import de.niborblog.wetterstationsapp.components.BluetoothPermissions
import de.niborblog.wetterstationsapp.navigation.WetterSNavigation
import de.niborblog.wetterstationsapp.ui.theme.WetterstationsAppTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        var takeResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
                ActivityResultCallback { result ->
                    if (result.resultCode == RESULT_OK) {
                        Toast.makeText(applicationContext, "Bluetooth ON", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(applicationContext, "Bluetooth OFF", Toast.LENGTH_LONG)
                            .show()
                    }
                })
        setContent()
        {
            WetterStationsApp()
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WetterStationsApp() {
    WetterstationsAppTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column {
                val multipleBluetoothPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    rememberMultiplePermissionsState(
                        permissions = listOf(
                            android.Manifest.permission.BLUETOOTH,
                            android.Manifest.permission.BLUETOOTH_ADMIN,
                            android.Manifest.permission.BLUETOOTH_SCAN,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.BLUETOOTH_CONNECT,
                        )
                    )
                } else {
                    rememberMultiplePermissionsState(
                        permissions = listOf(
                            android.Manifest.permission.BLUETOOTH,
                            android.Manifest.permission.BLUETOOTH_ADMIN,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION,
                            )
                    )                }
                BluetoothPermissions(multiplePermissionsState = multipleBluetoothPermissionState )

                WetterSNavigation()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WetterstationsAppTheme {

    }
}

