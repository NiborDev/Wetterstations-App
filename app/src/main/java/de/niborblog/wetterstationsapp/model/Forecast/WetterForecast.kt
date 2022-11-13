
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.model.Forecast

data class WetterForecast(
    val alerts: Alerts,
    val current: Current,
    val forecast: Forecast,
    val location: Location
)