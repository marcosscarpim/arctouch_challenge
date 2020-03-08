package com.arctouch.codechallenge.home

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.model.Movie
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * View Model to provide movies pagination.
 */
class HomeViewModel(private val api: TmdbApi) {

    private val entries: MutableLiveData<List<Movie>> by lazy {
        MutableLiveData<List<Movie>>().also {
            loadFirstPages()
        }
    }

    private var disposables: CompositeDisposable = CompositeDisposable()

    private var page: Long = 2

    private var isPageLoadLocked = false

    /**
     * Starts API to acquire next page.
     */
    fun getNextPage() {
        if (!isPageLoadLocked) {
            page++
            isPageLoadLocked = true
            val d = getPage(page)
            disposables.add(d)
        }
    }

    /**
     * Get observable to listen to API updates. This also starts the first two pages load.
     */
    fun getEntriesObservable() = entries

    private fun loadFirstPages() {
        val d1 = getPage(1)
        val d2 = getPage(2)
        disposables.addAll(d1, d2)
    }

    private fun getPage(page: Long) =
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, page, TmdbApi.DEFAULT_REGION)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val moviesWithGenres = it.results.map { movie ->
                    movie.copy(genres = Cache.genres.filter { movie.genreIds?.contains(it.id) == true })
                }
                entries.value = moviesWithGenres
                isPageLoadLocked = false
            }

    /**
     * Dispose the subscriptions to API to avoid leaks.
     */
    fun dispose() {
        disposables.clear()
    }
}