package com.dan.newsfeedswithinmvvm.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.dan.newsfeedswithinmvvm.R
import com.dan.newsfeedswithinmvvm.databinding.FragmentArticleBinding
import com.dan.newsfeedswithinmvvm.ui.util.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Dan Kim
 */
@AndroidEntryPoint
class ArticleFragment : Fragment(R.layout.fragment_article) {

    @Inject
    lateinit var viewModel: NewsViewModel

    val args: ArticleFragmentArgs by navArgs()

    private var binding: FragmentArticleBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentArticleBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val article = args.article
        binding?.webView?.apply {
            webViewClient = WebViewClient()
            loadUrl(article.url ?: "")
        }

        binding?.fab?.setOnClickListener {
            viewModel.saveArticle(article)
            Snackbar.make(view, "Article saved successfully", Snackbar.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}