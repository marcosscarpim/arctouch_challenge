package com.arctouch.codechallenge.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.base.BaseActivity
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.model.Movie
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.home_activity.*


class HomeActivity : BaseActivity() {

    private lateinit var viewModel: HomeViewModel

    private var adapter: HomeAdapter = HomeAdapter { openDetails(it) }

    private val pageObserver: Observer<List<Movie>> = Observer { movies ->
        movies?.let { updateUI(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

        // TODO improve view model call to follow Android guidelines
        viewModel = HomeViewModel(api)
        viewModel.getEntriesObservable().observe(this, pageObserver)

        recyclerView.adapter = adapter
        addScrollListener()
    }

    private fun addScrollListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.getNextPage()
                    progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private fun updateUI(movies: List<Movie>) {
        adapter.addMovieList(movies)
        progressBar.visibility = View.GONE
    }

    private fun openDetails(movieId: Int) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            putExtra(DetailsActivity.MOVIE_ID_EXTRA_NAME, movieId.toLong())
        }
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }
}
