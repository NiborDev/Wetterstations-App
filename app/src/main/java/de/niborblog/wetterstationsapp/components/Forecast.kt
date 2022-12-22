
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.components

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import de.niborblog.wetterstationsapp.utils.formatDate
import de.niborblog.wetterstationsapp.utils.formatDecimal
import de.niborblog.wetterstationsapp.utils.getJsonDataFromAsset
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Locale
import java.time.format.TextStyle;

/**
 *
 * This Component shows the Today's Weather
 *
 * */

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayForecast(
    forecast: WetterForecast
) {
    /**
     * set Information variables
     */
    val city = forecast.location.name;
    val region = forecast.location.region;
    val country = forecast.location.country;
    val day = LocalDateTime.ofEpochSecond(1595363833, 0, ZoneOffset.UTC).dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN); //DateTime(forecast.forecast.forecastday[0].date).dayOfWeek()
    val lastUpdated = formatDate(forecast.current.last_updated_epoch)

    /**
     * Set wetter variables
     */
    //TODO: give the user a settings Page where he can change the Temp format
    val temp = formatDecimal(forecast.current.temp_c)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .padding(start = 2.dp, end = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.secondary,
        ),
        elevation = CardDefaults.cardElevation(),
        onClick = {
            // TODO: Do show detailed Data
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Text(
                text = day,
                modifier = Modifier
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "$city - $region ($country)",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "/",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Text(
                    text = "API Updated: $lastUpdated",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            /**
             * Zeige Daten an (Temperatur, Wetter)
             */
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                /**
                 * Zeigt vom Mikrocontroller gemessene Daten an
                 */
                Column(
                    modifier = Modifier
                        .padding(3.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /**
                     * TODO Icon hinzufügen
                     */
                    Text(
                        text = "Indoor temperature",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "24°C", //TODO: get Data from Mikrocontroller
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                /**
                 * Zeigt mit hilfe der WeatherAPI die aktuelle Außentemperatur und das Wetterbericht an
                 */
                Column(
                    modifier = Modifier
                        .padding(3.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    /**
                     * TODO Icon hinzufügen
                     */
                    Text(
                        text = "Outdoor temperature",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "$temp°C",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
    }
}


@Composable
fun OutDoor(
    weather: WetterForecast,
) {
    /**
     * Set wetter variables
     */
    //TODO: give the user a settings Page where he can change the Temp format
    val temp = formatDecimal(weather.current.temp_c)
    val weatherText = weather.current.condition.text
    val wind_dir = weather.current.wind_dir
    val wind = formatDecimal(weather.current.wind_kph) //wind in km/h
    val tempFeelsLike = formatDecimal(weather.current.feelslike_c)

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
                    contentDescription = "svg",
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
                text = "$temp°C",
                color = MaterialTheme.colorScheme.secondary,
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(8.dp)
            )
        }
        Text(
        text = "Das Wetter ist zurzeit $weatherText, die Temperatur beträgt $temp°C bei einem Wind aus $wind_dir von $wind km/h. Die Tempeartur fühlt sich wie: $tempFeelsLike°C an.",
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
        )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun InDoor(
){
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(BottomSheetValue.Collapsed)
    )
    val coroutineScope = rememberCoroutineScope()
    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                Text(text = "Hello from sheet")
            }
        }, sheetPeekHeight = 320.dp
    ) {

    }
}