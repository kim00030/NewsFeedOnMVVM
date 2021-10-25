package com.dan.newsfeedswithinmvvm.ui.api

import com.dan.newsfeedswithinmvvm.ui.NewsResponse
import com.dan.newsfeedswithinmvvm.ui.util.AppConstants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Dan Kim
 */
//https://newsapi.org/v2/everything?q=tesla&from=2021-09-24&sortBy=publishedAt&apiKey=47445ae41d3f4d7d85ac393ea9f49382
interface NewAPI {

    @GET("v2/top-headlines")
    suspend fun getBreakingNews(
        @Query("country") countryCode: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apiKey: String = AppConstants.API_KEY
    ): Response<NewsResponse>

    @GET("v2/everything")
    suspend fun searchForNews(
        @Query("q") searchQuery: String,
        @Query("page") pageNumber: Int = 1,
        @Query("apikey") apiKey: String = AppConstants.API_KEY
    ): Response<NewsResponse>
}