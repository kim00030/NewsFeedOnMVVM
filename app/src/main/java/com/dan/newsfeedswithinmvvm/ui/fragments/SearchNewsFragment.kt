package com.dan.newsfeedswithinmvvm.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dan.newsfeedswithinmvvm.R
import com.dan.newsfeedswithinmvvm.databinding.FragmentSearchNewsBinding
import com.dan.newsfeedswithinmvvm.ui.adapters.NewsAdapter
import com.dan.newsfeedswithinmvvm.ui.util.AppConstants
import com.dan.newsfeedswithinmvvm.ui.util.NewsViewModel
import com.dan.newsfeedswithinmvvm.ui.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
@AndroidEntryPoint
class SearchNewsFragment : Fragment(R.layout.fragment_search_news) {

    @Inject
    lateinit var viewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private var binding: FragmentSearchNewsBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchNewsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()

        newsAdapter.setOnItemClickListener { article ->
            val bundle = Bundle().apply {
                putSerializable(AppConstants.ARTICLE, article)
            }
            findNavController().navigate(
                R.id.action_searchNewsFragment_to_articleFragment,
                bundle
            )
        }

        var job: Job? = null
        binding?.etSearch?.addTextChangedListener { editable ->
            job?.cancel()
            job = lifecycleScope.launch {
                delay(AppConstants.SEARCH_TIME_DELAY)
                editable?.let {
                    if (editable.toString().isNotEmpty()) {
                        viewModel.searchNews(editable.toString())
                    }
                }
            }
        }

        viewModel.searchNewsLiveData.observe(viewLifecycleOwner, { response ->

            when (response) {

                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        newsAdapter.differ.submitList(newsResponse.articles.toList())
                        val totalPages =
                            newsResponse.totalResults / AppConstants.QUERY_PAGE_SIZE + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        if (isLastPage) {
                            binding?.rvSearchNews?.setPadding(0, 0, 0, 0)
                        }
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.data?.let { it ->
                        Log.e(AppConstants.DEBUG_TAG, "An error occured: $it")
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })
    }

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= AppConstants.QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(binding?.etSearch?.text.toString())
                isScrolling = false
            }
        }
    }

    private fun hideProgressBar() {
        binding?.paginationProgressBar?.visibility = View.INVISIBLE
        isLoading = false
    }

    private fun showProgressBar() {
        binding?.paginationProgressBar?.visibility = View.VISIBLE
        isLoading = true
    }

    private fun setUpRecyclerView() {

        newsAdapter = NewsAdapter()
        binding?.rvSearchNews?.apply {
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(requireActivity())
            addOnScrollListener(this@SearchNewsFragment.scrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}