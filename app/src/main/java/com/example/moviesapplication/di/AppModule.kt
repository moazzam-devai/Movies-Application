package com.example.moviesapplication.di

import android.app.Application
import androidx.room.Room
import com.example.moviesapplication.BuildConfig
import com.example.moviesapplication.apiservice.FetchMoviesApiService
import com.example.moviesapplication.apiservice.SearchMoviesApiService
import com.example.moviesapplication.db.MovieDatabase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(interceptor)
            .build()
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideDatabase(app: Application): MovieDatabase =
        Room.databaseBuilder(app, MovieDatabase::class.java, "movie_database")
            .build()

    @Provides
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    fun provideMoviesListApiService(retrofit: Retrofit): FetchMoviesApiService =
        retrofit.create(FetchMoviesApiService::class.java)

    @Provides
    fun provideSearchMoviesApiService(retrofit: Retrofit): SearchMoviesApiService =
        retrofit.create(SearchMoviesApiService::class.java)
}