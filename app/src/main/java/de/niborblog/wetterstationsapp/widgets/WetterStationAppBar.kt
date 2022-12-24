
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import de.niborblog.wetterstationsapp.Bluetooth.BluetoothReceiver
import de.niborblog.wetterstationsapp.Bluetooth.CheckBluetoothStatus
import de.niborblog.wetterstationsapp.R

/**
 *
 * This is a custom AppBar Component
 * In this component we defined Colors for the AppBar and set the ScrollBehavior to pin the AppBar while Scrolling
 *
 * @param title
 * @param icon
 * @param onNavigationButtonClicked
 *
 * */

@Composable
fun AppBar(
    title: String = "WetterStations App",
    city: String = "Haiger",
    date: String = "Today, 08.12.2022",
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(70.dp)
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight)
                    ) {append("$city\n")}
                    withStyle(style = SpanStyle(
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = MaterialTheme.typography.bodyMedium.fontSize)
                    ) {append("$date")}
                },
                modifier = Modifier
                    .padding(start = 8.dp)
            )

            Row() {
                AddNewDeviceButton(context = LocalContext.current)
                IconButton(
                    onClick = { }
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.menu),
                        contentDescription = "Open Menu",
                        modifier = Modifier
                            .size(size = 32.dp))
                }
            }
        }
    }

}

@SuppressLint("MissingPermission")
@Composable
fun AddNewDeviceButton(context: Context) {
    var showCustomDialog by remember { mutableStateOf(false) }
    IconButton(
        onClick = {
            showCustomDialog = true
        }
    ) {
        Image(
            painter = painterResource(id = R.drawable.plus ),
            contentDescription = "Add Location",
            modifier = Modifier
                .size(size = 32.dp))
    }
    if (showCustomDialog) {
        DevicePairDialog(onClose = { showCustomDialog = false })
    }
}
