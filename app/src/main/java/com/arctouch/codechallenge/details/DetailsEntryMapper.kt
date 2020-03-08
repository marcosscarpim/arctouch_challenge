package com.arctouch.codechallenge.details

import android.text.TextUtils
import com.arctouch.codechallenge.model.Movie

/**
 * Maps the relationship between api data [Movie] and [DetailsEntry]
 */
class DetailsEntryMapper {

    /**
     * Maps a [Movie] to [DetailsEntry]
     *
     * @param movie the [Movie]
     * @return the converted [DetailsEntry]
     */
    fun toEntry(movie: Movie): DetailsEntry {
        val genreList = movie.genres?.map { it.name }
        return DetailsEntry(
                name = movie.title,
                posterPath = movie.posterPath,
                backdropPath = movie.backdropPath,
                genreList = genreList?.let { TextUtils.join(", ", it) },
                releaseDate = movie.releaseDate,
                overview = movie.overview
        )
    }
}