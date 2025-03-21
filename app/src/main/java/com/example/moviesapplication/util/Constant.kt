package com.example.moviesapplication.util

import kotlin.properties.Delegates

object Constant {

  object Variables {
    var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, _ ->
    }
  }

  const val AUTH_FAILURE_CODE = 401
  const val AUTH_URL_ERROR = ".medznmore.com/oauth/token"
  const val SESSION_EXPIRED = "Your session has been expired"
  const val SOMETHING_WENT_WRONG = "Something went wrong"
  const val GENERAL_ERROR_CODE = -1
  const val REASON_FAILURE = "Failure"
}