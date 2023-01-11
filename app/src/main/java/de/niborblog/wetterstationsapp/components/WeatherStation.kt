/**************************************************************************************************
 * Copyright (c) 2023.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.stringPreferencesKey
import de.niborblog.wetterstationsapp.Bluetooth.startScanning
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.dataStore
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InDoor(
    viewModel: HomeModel
) {
    val isExpanded = remember {
        mutableStateOf(false)
    }
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
            if (isExpanded.value){
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "CO Konzentrationen Überblick")
                    Text(text = "0-9 ppm: Keine Gefahr\n9-35ppm Niedrige Gefahr\n35-200 ppm: Mittlere Gefahr\n200-400 ppm: Hohe Gefahr\n>400 ppm: Sehr hohe Gefahr")
                    Icon(painter = painterResource(id = R.drawable.circle_chevron_up), contentDescription = "expand", modifier = Modifier.clickable { isExpanded.value = false  })
                }
            }else{
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(painter = painterResource(id = R.drawable.circle_chevron_down), contentDescription = "expand", modifier = Modifier.clickable { isExpanded.value = true  })
                }
            }

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
    val value: String = viewModel.tempData.value
    val tempF: String = viewModel.tempDataF.value
    val uiTemp = viewModel.uiTemp.value
    if (tempUnit.equals("0")) { //°C ausgewählt
        if (value.isNotEmpty()) {
            viewModel.uiTemp.value = value.toDouble().roundToInt().toString()
        }
        unit = "°C"
    } else {
        if (tempF.isNotEmpty()) {
            //Fahrenheit value nehmen
            viewModel.uiTemp.value = tempF.toDouble().roundToInt().toString()
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
            value = viewModel.uiTemp,
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