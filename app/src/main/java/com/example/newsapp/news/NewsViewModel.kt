package com.example.newsapp.news

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.newsapp.api.ApiManager
import com.example.newsapp.api.model.ArticlesItem
import com.example.newsapp.api.model.NewsResponse
import com.example.newsapp.api.model.SourcesItem
import com.example.newsapp.api.model.SourcesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {
    val sourceList =mutableStateListOf<SourcesItem>()
    val newsList = mutableStateListOf<ArticlesItem>()
    val selectedSourceId = mutableStateOf("")
    val isSheetOpen = mutableStateOf(false)
    val selectedArticle = mutableStateOf<ArticlesItem?>(null)
    val error = mutableStateOf("")
    val isLoading = mutableStateOf(true)


    fun getSourcesList(categoryApiId: String){
        ApiManager.newsService.getSources("adb724f79b944484accc7bdd2b789ecc" , categoryApiId ).enqueue(object :
            Callback<SourcesResponse> {
            override fun onResponse(call: Call<SourcesResponse>, response: Response<SourcesResponse>) {
                val sourcesList = response.body()?.sources?.filterNotNull()
                if (response.isSuccessful) {
                    if (!sourcesList.isNullOrEmpty()) {
                        sourceList.clear()
                        sourceList.addAll(sourcesList)
                    }
                }
            }

            override fun onFailure(call: Call<SourcesResponse>, throwable: Throwable) {
                error.value = throwable.message.toString()
            }

        })
    }

    fun getNewsBySource(sourceId : String){
        isLoading.value = true
        ApiManager.newsService.getNewsBySource(sourceId = sourceId , apiKey = "adb724f79b944484accc7bdd2b789ecc").enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                val articlesList = response.body()?.articles?.filterNotNull()
                if (response.isSuccessful) {
                    if (!articlesList.isNullOrEmpty()) {
                        newsList.clear()
                        newsList.addAll(articlesList)
                    }
                    isLoading.value = false
                }

            }

            override fun onFailure(call: Call<NewsResponse>, throwable: Throwable){
                error.value = throwable.message.toString()
            }

        })


    }

    fun getNewsBySearch(q : String){
        ApiManager.newsService.getNewsBySearch(q , "adb724f79b944484accc7bdd2b789ecc").enqueue(object :
            Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                val articlesList = response.body()?.articles?.filterNotNull()
                if (response.isSuccessful) {
                    if (!articlesList.isNullOrEmpty()) {
                        newsList.clear()
                        newsList.addAll(articlesList)
                    }
                }
            }

            override fun onFailure(call: Call<NewsResponse>, throwable: Throwable) {
                error.value = throwable.message.toString()
            }
        })
    }

}