package com.arctouch.codechallenge.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbRepository
import io.reactivex.disposables.Disposable

/**
 * View Model to provide movie details information.
 *
 * @param repository the [TmdbRepository]
 * @param mapper maps the relationship between [Movie] and [DetailsEntry]
 */
class DetailsViewModel(
        private val repository: TmdbRepository,
        private val mapper: DetailsEntryMapper
): ViewModel() {

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
                .subscribe { entry.value = mapper.toEntry(it) }
    }

    /**
     * Dispose the subscriptions to API to avoid leaks.
     */
    fun dispose() {
        disposable?.dispose()
    }
}