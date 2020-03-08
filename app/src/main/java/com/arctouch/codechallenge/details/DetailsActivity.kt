package com.arctouch.codechallenge.details

import android.arch.lifecycle.Observer
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.widget.ImageView
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.databinding.DetailsActivityBinding
import com.arctouch.codechallenge.util.MovieImageUrlBuilder
import com.bumptech.glide.Glide

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

        val movieId = intent.getLongExtra(MOVIE_ID_EXTRA_NAME, 0)

        // TODO improve view model call to follow Android guidelines
        viewModel = DetailsViewModel(repository)
        viewModel.getDetails(movieId).observe(this, detailsObserver)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    private fun updateUI(entry: DetailsEntry?) {
        binding.entry = entry
        entry?.posterPath?.let { loadImageToView(it, binding.posterImageView) }
        entry?.backdropPath?.let { loadImageToView(it, binding.backdropImageView) }
    }

    private fun loadImageToView(imagePath: String, view: ImageView) {
        val movieImageUrlBuilder = MovieImageUrlBuilder()
        Glide.with(this)
            .load(imagePath.let { movieImageUrlBuilder.buildPosterUrl(it) })
            .into(view)
    }

    companion object {
        const val MOVIE_ID_EXTRA_NAME = "movie_id"
    }
}