package com.dan.newsfeedswithinmvvm.ui.repository

import com.dan.newsfeedswithinmvvm.ui.api.RetrofitInstance
import com.dan.newsfeedswithinmvvm.ui.db.ArticleDatabase
import com.dan.newsfeedswithinmvvm.ui.models.Article

/**
 * Created by Dan Kim
 */
class NewsRepository(
    private val db: ArticleDatabase
) {

    suspend fun getBreakingNews(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api?.getBreakingNews(
            countryCode = countryCode,
            pageNumber = pageNumber
        )

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api?.searchForNews(
            searchQuery = searchQuery,
            pageNumber = pageNumber
        )

    suspend fun upsert(article: Article) {
        db.getArticleDao().upsert(article)
    }

    fun getSavedNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article) = db.getArticleDao().deleteArticle(article)
}