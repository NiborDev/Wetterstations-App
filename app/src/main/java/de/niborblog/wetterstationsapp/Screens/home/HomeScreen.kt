
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import de.niborblog.wetterstationsapp.Screens.home.HomeModel
import de.niborblog.wetterstationsapp.components.AppBar
import de.niborblog.wetterstationsapp.components.OutDoor
import de.niborblog.wetterstationsapp.data.DataOrException
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    viewModel: HomeModel = hiltViewModel(),
    city: String,
    lang: String
) {
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
        MainContent(weatherData = weather.data!!)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    weatherData: WetterForecast
    ) {

    Scaffold(
        topBar = {
            AppBar()
        }
    ) { innerPadding ->
        LazyColumn(
            contentPadding = innerPadding,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ){
            item {
                /** TODO: show Today's Weather */
                OutDoor(weatherData)
            }
            item {
                /** TODO: show Wetterstation Data */
                /** TODO: on Card Click -> show detailed Data */
            }
            item {
                /** TODO: show Week Forecast */
            }
        }
    }
}