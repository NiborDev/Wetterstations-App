
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.navigation

import androidx.compose.runtime.Composable
import com.ramcosta.composedestinations.DestinationsNavHost
import de.niborblog.wetterstationsapp.Screens.NavGraphs


/**
 * This WetterSNavigation Composable is used to navigate throw Pages
 * It is the Host for all Pages, this is why its loaded on MainActivity
 *
 * */

@Composable
fun WetterSNavigation() {
    DestinationsNavHost(navGraph = NavGraphs.root)
}