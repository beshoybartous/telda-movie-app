package com.movie.app.navigation

sealed class Screen(
    val route: String
){
    data object MoviesHomeScreen: Screen("movies_home")
    data object MovieDetailsScreen: Screen("movie_details/{${MOVIE_ID_ARGUMENT}}"){
        fun setMovieId(id: Int):String{
            return "movie_details/$id"
        }
    }

    companion object{
        const val MOVIE_ID_ARGUMENT ="movieId"
    }
}