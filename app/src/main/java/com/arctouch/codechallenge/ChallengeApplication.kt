package com.arctouch.codechallenge

import android.app.Application
import android.content.Intent
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbRepository
import com.arctouch.codechallenge.data.Cache
import com.arctouch.codechallenge.home.HomeActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

class ChallengeApplication: Application()  {

    override fun onCreate() {
        super.onCreate()

        val repository = TmdbRepository(TmdbApi.create())

        repository.getGenres()
                .subscribe {
                    Cache.cacheGenres(it)
                }
    }
}