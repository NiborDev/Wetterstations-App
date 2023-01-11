
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.stringPreferencesKey
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.dataStore
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import de.niborblog.wetterstationsapp.utils.formatDecimal


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

@Composable
fun weaterDetails(weather: WetterForecast) {
    //get Datastore read preference for Temperature
    val datastore = LocalContext.current.dataStore
    val prefs by remember { datastore.data }.collectAsState(initial = null)
    val temUnitKey = stringPreferencesKey("ddTempUnit")
    var tempUnit: String = ""
    prefs?.get(temUnitKey)?.also { tempUnit = it }

    //weather Data from API
    var unit = ""
    var maxTemp = 0.0
    var humidity = weather.current.humidity
    var rainChance = weather.forecast.forecastday[0].day.daily_chance_of_rain
    var precip = weather.current.precip_mm
    var maxWind = weather.forecast.forecastday[0].day.maxwind_kph
    var uvIndex = weather.forecast.forecastday[0].day.uv

    if (tempUnit.equals("0")) { //°C ausgewählt
        maxTemp = weather.forecast.forecastday[0].day.maxtemp_c
        unit = "°C"
    } else {
        maxTemp = weather.forecast.forecastday[0].day.maxtemp_f
        unit = "°F"
    }

    Column(
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Wetter Informationen Außen",
            fontSize = MaterialTheme.typography.headlineSmall.fontSize,
            fontWeight = MaterialTheme.typography.headlineSmall.fontWeight,
            modifier = Modifier.padding(15.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailCard(
                title = "Max Temp",
                painter = painterResource(id = R.drawable.thermomax_fill),
                value = maxTemp.toString(),
                unit = unit
            )
            DetailCard(
                title = "Luftfeuchtigkeit",
                painter = painterResource(id = R.drawable.humidity),
                value = humidity.toString(),
                unit = "%"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailCard(
                title = "Max Windstärke",
                painter = painterResource(id = R.drawable.wind),
                value = maxWind.toString(),
                unit = "km/h"
            )
            DetailCard(
                title = "Regenwahrscheinlichkeit",
                painter = painterResource(id = R.drawable.rain),
                value = rainChance.toString(),
                unit = "%"
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(88.dp)
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            DetailCard(
                title = "UV Index",
                painter = painterResource(id = R.drawable.sun),
                value = uvIndex.toString(),
                unit = ""
            )
            DetailCard(
                title = "Niederschlag",
                painter = painterResource(id = R.drawable.percip),
                value = precip.toString(),
                unit = "mm"
            )
        }

    }
}

@Composable
fun DetailCard(title: String, painter: Painter, value: String, unit: String) {
    Card(
        modifier = Modifier
            .fillMaxHeight()
            .width(200.dp)
    ) {
        Column(modifier = Modifier.padding(15.dp)) {
            Text(
                text = title,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Icon(
                    painter = painter,
                    contentDescription = "title",
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "$value$unit",
                    modifier = Modifier.padding(start = 8.dp),
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize
                )
            }
        }
    }
}