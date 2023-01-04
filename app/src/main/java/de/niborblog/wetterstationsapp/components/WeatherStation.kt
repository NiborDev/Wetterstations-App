/**************************************************************************************************
 * Copyright (c) 2023.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.ViewModel
import de.niborblog.wetterstationsapp.Bluetooth.startScanning
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.Screens.home.HomeModel

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
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        WeatherStationDataItem(
            modifier = Modifier.padding(4.dp),
            painter = painterResource(id = R.drawable.temp),
            title = "Temperatur",
            value = viewModel.tempData, //TODO werte abändern
            unit = "°C"
        )
        WeatherStationDataItem(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.humidity),
            title = "Luftfeuchtigkeit",
            value = viewModel.humidityData, //TODO werte abändern
            unit = "%"
        )
        WeatherStationDataItem(
            modifier = Modifier.padding(8.dp),
            painter = painterResource(id = R.drawable.humidity),
            title = "CO",
            value = viewModel.coData, //TODO werte abändern
            unit = "PPM"
        )
    }

}
//TODO: Composable für Daten erstellen!

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