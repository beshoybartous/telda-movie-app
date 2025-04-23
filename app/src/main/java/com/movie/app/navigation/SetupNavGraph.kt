package com.movie.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.movie.app.navigation.Screen.Companion.MOVIE_ID_ARGUMENT


@Composable
fun SetupNavGraph(navController: NavHostController,modifier: Modifier= Modifier) {
    NavHost(
        modifier=modifier,
        navController = navController, startDestination = Screen.MoviesHomeScreen
            .route, builder = {
            composable(route = Screen.MoviesHomeScreen.route) {

            }
            composable(
                route = Screen.MovieDetailsScreen.route, arguments = listOf(
                    navArgument(MOVIE_ID_ARGUMENT) {
                        type = NavType.StringType
                    })
            ) {

            }


        })

}