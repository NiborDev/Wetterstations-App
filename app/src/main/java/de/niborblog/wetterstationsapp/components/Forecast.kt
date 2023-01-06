
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.preference.PreferenceDataStore
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jamal.composeprefs3.ui.LocalPrefsDataStore
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.dataStore
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import de.niborblog.wetterstationsapp.utils.formatDecimal
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.observeOn


/**
 *
 * This Component shows the Today's Weather
 *
 * */

@Composable
fun OutDoor(
    weather: WetterForecast,
) {
    //get Datastore read preference for Temperature
    val datastore = LocalContext.current.dataStore
    val prefs by remember { datastore.data }.collectAsState(initial = null)
    val temUnitKey = stringPreferencesKey("ddTempUnit")
    var tempUnit: String = ""
    prefs?.get(temUnitKey)?.also { tempUnit = it }

    /**
     * Set wetter variables
     */
    //TODO: give the user a settings Page where he can change the Temp format

    var temp = ""
    var unit = ""
    var tempFeelsLike = ""
    if (tempUnit.equals("0")){ //°C ausgewählt
        temp = formatDecimal(weather.current.temp_c)
        tempFeelsLike = formatDecimal(weather.current.feelslike_c)
        unit = "°C"
    }else {
        temp = formatDecimal(weather.current.temp_f)
        tempFeelsLike = formatDecimal(weather.current.feelslike_f)
        unit = "°F"
    }

    val weatherText = weather.current.condition.text
    val wind_dir = weather.current.wind_dir
    val wind = formatDecimal(weather.current.wind_kph) //wind in km/h

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .width(220.dp)
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.sun),
                    contentDescription = "Weather",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                )
                Spacer(
                    modifier = Modifier
                        .height(height = 4.dp))
                Text(
                    text = weatherText,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .width(135.dp)
                )
            }
            Text(
                text = "$temp$unit",
                color = MaterialTheme.colorScheme.secondary,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
        text = "Das Wetter ist zurzeit $weatherText, die Temperatur beträgt $temp$unit bei einem Wind aus $wind_dir von $wind km/h. Die Tempeartur fühlt sich wie: $tempFeelsLike$unit an.",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}