
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Screens.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import de.niborblog.wetterstationsapp.data.DataOrException
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import de.niborblog.wetterstationsapp.repository.WetterRepository
import javax.inject.Inject
@HiltViewModel
class HomeModel @Inject constructor(private val repository: WetterRepository) : ViewModel() {
    val humidityData = mutableStateOf("");
    val tempData = mutableStateOf("");
    val tempDataF = mutableStateOf("");
    val uiTemp = mutableStateOf("");
    val coData = mutableStateOf("");
    suspend fun getWeatherForecastData(city: String, lang: String = "en"): DataOrException<WetterForecast, Boolean, Exception> {
        return repository.getWeatherForecast(cityQuery = city, lang = lang)
    }

}