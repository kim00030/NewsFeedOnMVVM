package com.dan.newsfeedswithinmvvm.ui.util

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dan.newsfeedswithinmvvm.ui.models.NewsResponse
import com.dan.newsfeedswithinmvvm.ui.repository.NewsRepository
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    val breakingNewsLiveData: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var breakingNewsPage = 1

    init {
        getBreakingNews("us")
    }

    private fun getBreakingNews(countryCode: String) = viewModelScope.launch {

        breakingNewsLiveData.postValue(Resource.Loading())

        val response =
            newsRepository.getBreakingNews(countryCode = countryCode, pageNumber = breakingNewsPage)

        breakingNewsLiveData.postValue(handleBreakingNewsResponse(response))
    }

    private fun handleBreakingNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {

        if (response.isSuccessful) {
            response.body()?.let { result ->
                return Resource.Success(result)
            }
        }
        return Resource.Error(null, response.message())
    }

}