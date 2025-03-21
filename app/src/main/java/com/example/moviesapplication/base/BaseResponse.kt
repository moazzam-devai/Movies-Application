package com.example.moviesapplication.base

import com.example.moviesapplication.network.NoConnectivityException
import com.example.moviesapplication.network.Resource
import com.example.moviesapplication.util.Constant.AUTH_FAILURE_CODE
import com.example.moviesapplication.util.Constant.AUTH_URL_ERROR
import com.example.moviesapplication.util.Constant.GENERAL_ERROR_CODE
import com.example.moviesapplication.util.Constant.REASON_FAILURE
import com.example.moviesapplication.util.Constant.SESSION_EXPIRED
import com.example.moviesapplication.util.Constant.SOMETHING_WENT_WRONG
import com.google.gson.Gson
import retrofit2.Response
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


/*
* Using Resource helper class return success data or error with messages
* */
abstract class BaseResponse {

    private var gSon: Gson = Gson()
    /*
   * suspend is a kotlin coroutine modifier. When put with a function it shows that this code block will run in a coroutine a separate thread
   * and UI related operations can be performed during this execution. Long running operations like getting data from network can be fetch using this
   * */

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()

            return if (response.code() == AUTH_FAILURE_CODE) {
                if (response.toString().contains(AUTH_URL_ERROR)){
                    responseErrorToJson(response)
                } else{
                    //EventBus.getDefault().post(UnauthorizedEvent())
                    error(throwable = Any() as Throwable, SESSION_EXPIRED, BaseErrorResponse(AUTH_FAILURE_CODE, SESSION_EXPIRED, REASON_FAILURE))
                }
            } else {
                responseErrorToJson(response)
            }
        } catch (networkException: NoConnectivityException){
            val response = BaseErrorResponse(GENERAL_ERROR_CODE, networkException.message, REASON_FAILURE)
            return error(throwable = networkException, networkException.message, response)
        } catch (e: Exception) {
            val response = BaseErrorResponse(GENERAL_ERROR_CODE, SOMETHING_WENT_WRONG, REASON_FAILURE)
            return error(throwable = e, e.message ?: e.toString(), response)
        }
    }

    private fun <T> error(throwable: Throwable, message: String, responseError: BaseErrorResponse): Resource<T> {
        //Timber.d(message)
        return Resource.Error(throwable , message = message, data = null)
    }

    /*
    * After mapping the string converted error to BaseErrorResponse return error as Resource<T>
    * */
    private fun <T> responseErrorToJson(response: Response<T>): Resource<T> {
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) return Resource.Success(body)
        }
        val json = errorBodyToString(response)
        val responseError = gSon.fromJson(json, BaseErrorResponse::class.java)
        return error(throwable = Any() as Throwable, message = "", responseError = responseError)
    }

    /*
    * This will map the calling API error if any and return it as string to further map on Our BaseErrorResponse
    * */
    private fun <T> errorBodyToString(response: Response<T>): String{
        val reader: BufferedReader?
        val sb = StringBuilder()
        try {
            reader = BufferedReader(InputStreamReader(response.errorBody()?.byteStream()))
            var line: String?
            try {
                while (reader.readLine().also { line = it } != null) {
                    sb.append(line)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return sb.toString()
    }
}