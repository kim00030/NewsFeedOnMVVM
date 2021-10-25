package com.dan.newsfeedswithinmvvm.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.dan.newsfeedswithinmvvm.R
import com.dan.newsfeedswithinmvvm.ui.util.NewsViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    @Inject
    lateinit var viewModel: NewsViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}