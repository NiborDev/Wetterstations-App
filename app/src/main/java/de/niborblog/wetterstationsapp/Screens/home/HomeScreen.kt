
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import de.niborblog.wetterstationsapp.Bluetooth.startScanning
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.components.AppBar
import de.niborblog.wetterstationsapp.components.InDoor
import de.niborblog.wetterstationsapp.components.OutDoor
import de.niborblog.wetterstationsapp.data.DataOrException
import de.niborblog.wetterstationsapp.dataStore
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.*


@SuppressLint("UnrememberedMutableState")
@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeModel = hiltViewModel(),
    city: String,
    lang: String,
) {

    /**
     * Connect to WetterStation
     */
    startScanning(LocalContext.current, viewModel = viewModel)
    /**
     * Lade Wetter Daten
     */
    val weather = produceState<DataOrException<WetterForecast, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = viewModel.getWeatherForecastData(city = city, lang= lang)
    }.value

    if (weather.loading == true) {
        //Daten laden noch
    }else if (weather.data != null){
        //Wetter daten geladen
        //zeige Content an
        val day = LocalDateTime.ofEpochSecond(weather.data!!.location.localtime_epoch.toLong(), 0, ZoneOffset.UTC).dayOfWeek.getDisplayName(
            TextStyle.FULL, Locale.GERMAN);
        val dayOfMonth = LocalDateTime.ofEpochSecond(weather.data!!.location.localtime_epoch.toLong(), 0, ZoneOffset.UTC).dayOfMonth
        val month = LocalDateTime.ofEpochSecond(weather.data!!.location.localtime_epoch.toLong(), 0, ZoneOffset.UTC).monthValue
        val year = LocalDateTime.ofEpochSecond(weather.data!!.location.localtime_epoch.toLong(), 0, ZoneOffset.UTC).year
        val date = "$day, $dayOfMonth.$month.$year"
        MainContent(weatherData = weather.data!!, city= city, dateString = date, viewModel = viewModel, navigator = navigator)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navigator: DestinationsNavigator,
    weatherData: WetterForecast,
    city: String,
    dateString: String,
    viewModel: HomeModel
    ) {

    Scaffold(
        topBar = {
            AppBar(city = city, date = dateString, navigator = navigator)
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            item {
                /** show Today's Weather */
                OutDoor(weatherData)
            }
            item {
                /** show Wetterstation Data */
                InDoor(viewModel= viewModel)
            }
            item {
                /** TODO: show more Details from API */
            }
        }
    }
}