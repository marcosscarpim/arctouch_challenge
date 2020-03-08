package com.arctouch.codechallenge.details

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.arctouch.codechallenge.api.TmdbApi
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class DetailsViewModel(
        private val api: TmdbApi,
        private val movieId: Long,
        private val mapper: DetailsEntryMapper
): ViewModel() {

    private val entry: MutableLiveData<DetailsEntry> by lazy {
        MutableLiveData<DetailsEntry>().also {
            loadDetails()
        }
    }

    private var disposable: Disposable? = null

    /**
     * Starts API to acquire movie detailed information.
     *
     * @return a [LiveData] with [DetailsEntry]
     */
    fun getDetails(): LiveData<DetailsEntry> = entry

    private fun loadDetails() {
        disposable = api.movie(movieId, TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { entry.value = mapper.toEntry(it) }
    }

    fun dispose() {
        disposable?.dispose()
    }
}