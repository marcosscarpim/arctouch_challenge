package com.arctouch.codechallenge

import android.app.Application
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbRepository
import com.arctouch.codechallenge.data.Cache
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class ChallengeApplication: Application()  {

    private val repository: TmdbRepository by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@ChallengeApplication)
            modules(KoinModules().challengeModules)
        }

        repository.getGenres()
                .subscribe {
                    Cache.cacheGenres(it)
                }
    }
}