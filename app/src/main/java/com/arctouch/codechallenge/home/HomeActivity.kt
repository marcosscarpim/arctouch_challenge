package com.arctouch.codechallenge.home

import android.arch.lifecycle.Observer
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.arctouch.codechallenge.R
import com.arctouch.codechallenge.details.DetailsActivity
import com.arctouch.codechallenge.model.Movie
import kotlinx.android.synthetic.main.home_activity.*
import org.koin.android.viewmodel.ext.android.viewModel


class HomeActivity : AppCompatActivity() {

    private val viewModel: HomeViewModel by viewModel()

    private var adapter: HomeAdapter = HomeAdapter { openDetails(it) }

    private val pageObserver: Observer<List<Movie>> = Observer { movies ->
        movies?.let { updateUI(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_activity)

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
