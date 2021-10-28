package com.dan.newsfeedswithinmvvm.ui.di

import android.content.Context
import com.dan.newsfeedswithinmvvm.ui.util.ConnectivityManager
import com.dan.newsfeedswithinmvvm.ui.util.ConnectivityManagerImpl
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
object AppModule {

    @Singleton
    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return ConnectivityManagerImpl(context)
    }
}