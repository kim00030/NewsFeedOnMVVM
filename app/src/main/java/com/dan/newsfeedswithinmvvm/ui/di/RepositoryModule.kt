package com.dan.newsfeedswithinmvvm.ui.di

import com.dan.newsfeedswithinmvvm.ui.db.ArticleDatabase
import com.dan.newsfeedswithinmvvm.ui.repository.NewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Dan Kim
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun providesNewsRepository(database: ArticleDatabase): NewsRepository {
        return NewsRepository(database)
    }

}