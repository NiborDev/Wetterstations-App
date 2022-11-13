
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.utils

import android.content.Context
import android.util.Log
import java.io.IOException

/**
 * Load Json data from Local file
 * Read the data and save it to a String: jsonString
 *
 * @param context
 * @param filename
 *
 * @return jsonString
 *
 */
fun getJsonDataFromAsset(context: Context, filename: String): String? {
    val jsonString: String
    try {
        jsonString = context.assets.open(filename).bufferedReader().use { it.readLine() }
    }catch (ioException: IOException) {
        Log.e("JsonLoader", ioException.message.toString())
        ioException.printStackTrace()
        return null
    }
    return jsonString
}