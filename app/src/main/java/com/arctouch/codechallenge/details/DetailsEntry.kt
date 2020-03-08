package com.arctouch.codechallenge.details

import android.graphics.drawable.Drawable
import android.media.Image

/**
 * Entry item for [DetailsActivity].
 *
 * @param name the name of the movie
 * @param posterImage the poster image
 * @param backdropImage the backdrop image
 * @param genreList the list of movie genres
 * @param releaseDate the release date
 */
data class DetailsEntry(
        val name: String,
        val posterPath: String?,
        val backdropPath: String?,
        val genreList: String?,
        val overview: String?,
        val releaseDate: String?
)