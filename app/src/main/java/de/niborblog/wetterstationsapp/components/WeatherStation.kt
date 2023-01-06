/**************************************************************************************************
 * Copyright (c) 2023.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import de.niborblog.wetterstationsapp.Bluetooth.startScanning
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.dataStore
import de.niborblog.wetterstationsapp.utils.formatDecimal
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InDoor(
    viewModel: HomeModel
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.Start
        ) {
            Heading(viewModel = viewModel)
            DataUI(viewModel = viewModel)
        }
    }
}

@Composable
fun DataUI(
    viewModel: HomeModel
) {
    //get Datastore read preference for Temperature
    val datastore = LocalContext.current.dataStore
    val prefs by remember { datastore.data }.collectAsState(initial = null)
    val temUnitKey = stringPreferencesKey("ddTempUnit")
    var tempUnit: String = ""
    prefs?.get(temUnitKey)?.also { tempUnit = it }

    var unit = ""
    if (tempUnit.equals("0")){ //°C ausgewählt
        if (viewModel.tempData.value.isNotEmpty()) {
            viewModel.tempData.value = viewModel.tempData.value.toDouble().roundToInt().toString()
        }
        unit = "°C"
    }else {
        if (viewModel.tempData.value.isNotEmpty()) {
            viewModel.tempData.value = ( (viewModel.tempData.value.toDouble()*9/5) + 32).roundToInt().toString()
        }
        unit = "°F"
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WeatherStationDataItem(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = R.drawable.temp),
            title = "Temperatur",
            value = viewModel.tempData,
            unit = unit
        )
        WeatherStationDataItem(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.humidity),
            title = "Luftfeuchtigkeit",
            value = viewModel.humidityData,
            unit = "%"
        )
        WeatherStationDataItem(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.humidity),
            title = "CO",
            value = viewModel.coData,
            unit = "PPM"
        )
    }

}

@Composable
fun WeatherStationDataItem(
    modifier: Modifier = Modifier,
    painter: Painter,
    title: String,
    value: MutableState<String>,
    unit: String = ""
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painter,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onPrimary
        )
        Text(
            modifier = Modifier.padding(start = 4.dp),
            text = title,
            fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = FontWeight.Thin
        )
        Text(
            text = if (value.value.isNotEmpty()) "${value.value}$unit" else "N/A",
            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Heading(viewModel: HomeModel) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.padding(top = 4.dp),
            text = "WetterStations Daten",
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
        )
        AssistChip(
            onClick = { startScanning(context, viewModel = viewModel) },
            label = { Text("Sync") },
            leadingIcon = {
                Icon(
                    Icons.Filled.Refresh,
                    contentDescription = "Localized description",
                    Modifier.size(AssistChipDefaults.IconSize),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        )
    }
}