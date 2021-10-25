package com.dan.newsfeedswithinmvvm.ui.di

import android.content.Context
import androidx.room.Room
import com.dan.newsfeedswithinmvvm.ui.db.ArticleDao
import com.dan.newsfeedswithinmvvm.ui.db.ArticleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Dan Kim
 */

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideArticleDataBase(@ApplicationContext context: Context): ArticleDatabase {
        return Room.databaseBuilder(
            context,
            ArticleDatabase::class.java,
            ArticleDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    }

    @Singleton
    @Provides
    fun provideArticleDAO(database: ArticleDatabase): ArticleDao {
        return database.getArticleDao()
    }
}