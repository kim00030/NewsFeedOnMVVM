package com.dan.newsfeedswithinmvvm.ui.util

import android.util.Log
import androidx.lifecycle.ViewModel
import com.dan.newsfeedswithinmvvm.ui.repository.NewsRepository
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
class NewsViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    init {
        Log.d("myDebug","")
    }


}