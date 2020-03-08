package com.arctouch.codechallenge.base

import android.support.v7.app.AppCompatActivity
import com.arctouch.codechallenge.api.TmdbApi
import com.arctouch.codechallenge.api.TmdbRepository

abstract class BaseActivity : AppCompatActivity() {

    protected val repository = TmdbRepository(TmdbApi.create())
}
