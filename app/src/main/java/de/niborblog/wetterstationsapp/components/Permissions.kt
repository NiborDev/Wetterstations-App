/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.Role.Companion.Button
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState


//TODO: Design überarbeiten!!
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothPermissions(
    multiplePermissionsState: MultiplePermissionsState
) {
    if (multiplePermissionsState.allPermissionsGranted) {
        Text(text = "Alle Berechtigungen akzeptiert!")

    } else {
        Column {
            val textToShow = if (multiplePermissionsState.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Die Folgende Berechtigungen sind notwenig, um die WetterStations Proplemlos einzurichten!"
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Es sind Bluetooth, GPS berechtigungen erforderlich\n" +
                        "Bitte Akzeptieren sie die Berechtigungen!"
            }
            Text(textToShow)
            Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
                Text("Berechtigungen Anfragen")
            }
        }
    }
}
