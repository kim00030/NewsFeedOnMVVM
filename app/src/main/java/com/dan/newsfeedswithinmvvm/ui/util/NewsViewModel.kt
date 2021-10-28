package com.dan.newsfeedswithinmvvm.ui.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dan.newsfeedswithinmvvm.ui.models.Article
import com.dan.newsfeedswithinmvvm.ui.models.NewsResponse
import com.dan.newsfeedswithinmvvm.ui.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
class NewsViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1
    private var breakingNewsResponse: NewsResponse? = null

    val searchNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    private var searchNewsResponse: NewsResponse? = null

    init {
        getBreakingNews(AppConstants.DEFAULT_COUNTRY_CODE)
    }

    fun getBreakingNews(countryCode: String) = viewModelScope.launch {
        safeBreakingNewsCall(countryCode)
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {

        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                breakingNewsPage++
                if (breakingNewsResponse == null) {
                    breakingNewsResponse = resultResponse
                } else {
                    val oldArticles = breakingNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(breakingNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(null, response.message())
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        safeSearchNewsCall(searchQuery)
    }

    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                searchNewsPage++
                if (searchNewsResponse == null) {
                    searchNewsResponse = resultResponse
                } else {
                    val oldArticles = searchNewsResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(null, response.message())
    }

    fun saveArticle(article: Article) = viewModelScope.launch {
        newsRepository.upsert(article)
    }

    fun getSavedNews() = newsRepository.getSavedNews()

    fun deleteArticle(article: Article) = viewModelScope.launch {
        newsRepository.deleteArticle(article)
    }

    private suspend fun safeSearchNewsCall(searchQuery: String) {
        searchNewsLiveData.postValue(Resource.Loading())
        try {
            if (connectivityManager.isInternetConnected()) {
                val response =
                    newsRepository.searchNews(
                        searchQuery = searchQuery,
                        pageNumber = searchNewsPage
                    )
                searchNewsLiveData.postValue(response?.let { handleSearchNewsResponse(it) })
            } else {
                searchNewsLiveData.postValue(Resource.Error(null, "No Internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNewsLiveData.postValue(
                    Resource.Error(
                        null,
                        "IO Exception"
                    )
                )
                else -> searchNewsLiveData.postValue(Resource.Error(null, "Something goes wrong"))
            }
        }
    }

    private suspend fun safeBreakingNewsCall(countryCode: String) {
        breakingNewsLiveData.postValue(Resource.Loading())
        try {
            if (connectivityManager.isInternetConnected()) {
                val response =
                    newsRepository.getBreakingNews(
                        countryCode = countryCode,
                        pageNumber = breakingNewsPage
                    )
                breakingNewsLiveData.postValue(response?.let { handleBreakingNewsResponse(it) })
            } else {
                breakingNewsLiveData.postValue(Resource.Error(null, "No Internet connection"))
            }

        } catch (t: Throwable) {
            when (t) {
                is IOException -> breakingNewsLiveData.postValue(
                    Resource.Error(
                        null,
                        "IO Exception"
                    )
                )
                else -> breakingNewsLiveData.postValue(Resource.Error(null, "Something goes wrong"))
            }
        }
    }
}