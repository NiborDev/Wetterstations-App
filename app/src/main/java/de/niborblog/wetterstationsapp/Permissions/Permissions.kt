
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.Role.Companion.Button
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState


//TODO: Design überarbeiten!!
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun BluetoothPermissions(
    multiplePermissionsList: List<String>
) {
    var showPermDialog by remember { mutableStateOf(false) }

    val multiplePermissionsState =
        rememberMultiplePermissionsState(permissions = multiplePermissionsList)
    if (multiplePermissionsState.allPermissionsGranted) {
        Log.d("PERMISSIONS","All PermissionsGranted!")

    } else {
        do {
            PermissionRequestDialog {
                multiplePermissionsState.launchMultiplePermissionRequest()
                showPermDialog = false
            }
        }while (showPermDialog)
    }
}
