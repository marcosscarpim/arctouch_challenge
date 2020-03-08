package com.arctouch.codechallenge.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * View Model to provide movie details information.
 *
 * @param api the [TmdbApi]
 * @param movieId the id of the movie to request information
 */
class DetailsViewModel(private val repository: TmdbRepository): ViewModel() {

    private val entry: MutableLiveData<DetailsEntry> by lazy {
        MutableLiveData<DetailsEntry>().also {
            loadDetails()
        }
    }

    private var disposable: Disposable? = null

    private var movieId: Long = -1

    /**
     * Starts API to acquire movie detailed information.
     *
     * @param id the movie id to load
     *
     * @return a [LiveData] with [DetailsEntry]
     */
    fun getDetails(id: Long): LiveData<DetailsEntry> {
        movieId = id
        return entry
    }

    private fun loadDetails() {
        disposable = repository.getMovieDetails(movieId)
                .subscribe { entry.value = DetailsEntryMapper().toEntry(it) }
    }

    /**
     * Dispose the subscriptions to API to avoid leaks.
     */
    fun dispose() {
        disposable?.dispose()
    }
}