package com.arctouch.codechallenge.details

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.util.Log
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.databinding.DetailsActivityBinding
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

/**
 * Activity to display detailed movies' information.
 */
class DetailsActivity : BaseActivity() {

    private lateinit var binding: DetailsActivityBinding

    private lateinit var viewModel: DetailsViewModel

    private val detailsObserver: Observer<DetailsEntry> = Observer {
        updateUI(it)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.details_activity)

        val movieId = intent.getIntExtra(MOVIE_ID_EXTRA_NAME, 0)
        viewModel = DetailsViewModel(api, movieId.toLong(), DetailsEntryMapper())

        viewModel.getDetails().observe(this, detailsObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    private fun updateUI(entry: DetailsEntry?) {
        binding.entry = entry
        val movieImageUrlBuilder = MovieImageUrlBuilder()
        Glide.with(this)
                .load(entry?.posterPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .into(binding.posterImageView)
        Glide.with(this)
                .load(entry?.backdropPath?.let { movieImageUrlBuilder.buildPosterUrl(it) })
                .into(binding.backdropImageView)
    }

    companion object {
        const val MOVIE_ID_EXTRA_NAME = "movie_id"
    }
}