
/**************************************************************************************************
 * Copyright (c) 2022.                                                                            *
 * NiborBloG - niborblog.de (Robin Fey)                                                           *
 **************************************************************************************************/

package de.niborblog.wetterstationsapp.Screens.Splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import de.niborblog.wetterstationsapp.R
import de.niborblog.wetterstationsapp.Screens.destinations.HomeScreenDestination
import de.niborblog.wetterstationsapp.Screens.destinations.SplashScreenDestination


@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(
    navigator: DestinationsNavigator,
    city: String? = "Haiger", //setze default Standort
    lang: String? = "de"
) {

    /**
     * Zeige SplashScreen
     */
    splash(navigator, city=city.toString(), lang= lang.toString())

}

@Composable
fun splash(
    navigator: DestinationsNavigator,
    city: String,
    lang: String,
    ) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.wetter_station_logo))
        val logoAnimationState =
            animateLottieCompositionAsState(composition = composition)
        LottieAnimation(
            composition = composition,
            progress = { logoAnimationState.progress }
        )
            if (logoAnimationState.isAtEnd && logoAnimationState.isPlaying) {
                navigator.popBackStack(SplashScreenDestination, true)
                navigator.navigate(HomeScreenDestination(city=city, lang= lang))
            }


    }
}