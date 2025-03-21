package com.example.moviesapplication

import android.app.Application
import android.content.Context
import com.example.moviesapplication.util.Util.startNetworkCallback
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoviesApplication: Application() {

  companion object {
    lateinit var context: Context
  }

  override fun onCreate() {
    super.onCreate()

    context = this
    /*
    * Initiate Internet Availability check
    * */
    startNetworkCallback()
  }
}