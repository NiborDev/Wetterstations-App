/**************************************************************************************************
 * Copyright (c) 2023.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Screens.Settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.sp
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.DropDownPref
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.ramcosta.composedestinations.annotation.Destination
import de.niborblog.wetterstationsapp.components.PrivacyPolicyDialog
import de.niborblog.wetterstationsapp.components.TermsAndConditionsDialog
import de.niborblog.wetterstationsapp.dataStore

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun SettingsScreen() {
    Scaffold(
        topBar = {
            SettingsTopBar()
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            MainPreferencesUI()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar() {
    TopAppBar(
        title = {
            Text(
                text = "Einstellungen",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    )
}

@Composable
fun MainPreferencesUI() {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    val entwicklerString = buildAnnotatedString {
        append("App: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Robin Fey\n")
        }
        append("MicroController: ")
        append("Muhammed Demir & ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
            append("Robin Fey")
        }
    }
    PrefsScreen(dataStore = LocalContext.current.dataStore) {
        prefsGroup("Haupteinstellungen") {
            prefsItem {
                TextPref(
                    title = "Temperatur Einheit",
                    summary = "Hier können sie einstellen, ob die Temperatur in °C oder F angezeigt werden soll."
                )
            }
            prefsItem {
                DropDownPref(
                    key = "ddTempUnit",
                    title = "Temperatur Einheit",
                    useSelectedAsSummary = true,
                    entries = mapOf(
                        "0" to "°C",
                        "1" to "°F",
                    )
                )
            }
        }
        prefsItem {
            TextPref(
                title = "Datenschutz",
                summary = "Hier finden sie einige rechtliche Dokumente"
            )
        }
        prefsItem {
            TextPref(
                modifier = Modifier.clickable { showPrivacyDialog = true },
                title = "Datenschutzerklärung",
                onClick = { showPrivacyDialog = true }
            )
            TextPref(
                modifier = Modifier.clickable { showTermsDialog = true },
                title = "Terms & Conditions",
                onClick = { showTermsDialog = true }
            )
        }
        prefsItem {
            TextPref(
                title = "Info & Über",
                summary = "© 2022-2023 Robin Fey"
            )
        }
        prefsItem {
            TextPref(
                title = "Entwickler",
                summary = "v.0.7"
            ){
                Text(text = entwicklerString)
            }
            TextPref(
                modifier = Modifier.clickable { uriHandler.openUri("https://niborblog.de") },
                title = "Webseite",
                summary = "",
                onClick = {
                    uriHandler.openUri("https://niborblog.de")
                }){
                Text(text = "niborblog.de")
            }
        }
    }
    if (showPrivacyDialog) {
        PrivacyPolicyDialog(onClose = { showPrivacyDialog = false })
    }
    if (showTermsDialog) {
        TermsAndConditionsDialog(onClose = { showTermsDialog = false })
    }
}
