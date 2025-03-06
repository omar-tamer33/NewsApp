package com.example.newsapp.api

import com.example.newsapp.api.model.NewsResponse
import com.example.newsapp.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {
    @GET("top-headlines/sources")
    fun getSources(@Query("apiKey") apiKey : String , @Query("category") category : String) : Call<SourcesResponse>

    @GET("everything")
    fun getNewsBySource(@Query("sources") sourceId : String , @Query("apiKey") apiKey: String) : Call<NewsResponse>

    @GET("everything")
    fun getNewsBySearch(@Query("q") q : String , @Query("apiKey") apiKey: String) : Call<NewsResponse>
}