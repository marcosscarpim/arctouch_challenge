package com.arctouch.codechallenge

import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbRepository
import com.arctouch.codechallenge.details.DetailsEntryMapper
import com.arctouch.codechallenge.details.DetailsViewModel
import com.arctouch.codechallenge.home.HomeViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

class KoinModules {

    val challengeModules = module {
        single { TmdbApi.create() }
        single { TmdbRepository(get()) }

        viewModel { DetailsViewModel(get(), get()) }
        viewModel { HomeViewModel(get()) }

        factory { DetailsEntryMapper() }
    }
}