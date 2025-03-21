package com.example.moviesapplication.network

import java.net.UnknownHostException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
  crossinline query: () -> Flow<ResultType>,
  crossinline fetch: suspend () -> RequestType,
  crossinline saveFetchResult: suspend (RequestType) -> Unit,
  crossinline shouldFetch: (ResultType) -> Boolean = { true }
) = flow {
  val data = query().first()

  val flow = if (shouldFetch(data)) {
    emit(Resource.Loading(data))

    try {
      saveFetchResult(fetch())
      query().map { Resource.Success(it) }
    } catch (throwable: Throwable) {
      if (throwable is UnknownHostException) {
        query().map { Resource.Success(it) }
      } else {
        query().map { Resource.Error(throwable = throwable, data = it) }
      }
    }
  } else {
    query().map { Resource.Success(it) }
  }

  emitAll(flow)

}