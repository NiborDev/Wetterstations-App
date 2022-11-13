
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.utils

import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Format Date to German Format
 */
fun formatDate(timestamp: Int): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy, HH:mm")
    val date = java.util.Date(timestamp.toLong() * 1000)

    return sdf.format(date)
}

/**
 * Format Decimal Number
 */
fun formatDecimal(number: Double): String {
    val df = DecimalFormat("##")
    return df.format(number)
}