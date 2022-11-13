
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.repository

import android.util.Log
import de.niborblog.wetterstationsapp.data.DataOrException
import de.niborblog.wetterstationsapp.model.Forecast.WetterForecast
import de.niborblog.wetterstationsapp.network.WeatherAPI
import javax.inject.Inject

class WetterRepository @Inject constructor(private val api: WeatherAPI) {
    /**
     *
     * WeatherAPI
     *
     */

    suspend fun getWeatherForecast(cityQuery: String, lang: String = "en"): DataOrException<WetterForecast, Boolean, Exception> {
        val response = try {
            api.getWeatherForecast(query = cityQuery, lang = lang)
        }catch (e: Exception) {
            Log.d("ForecastException","getWeatherForecast: $e")
            return DataOrException(e = e)
        }
        Log.d("Forecast","getWeatherForecast: $response")
        return DataOrException(data = response)
    }

}