package com.soni.tweetme.di

import android.app.Application
import android.content.Context
import com.soni.tweetme.repository.TweetRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class TweetAppModule {

    @Singleton
    @Provides
    fun providesContext(application: Application): Context = application.applicationContext

    /* Repository */
    @Provides
    @Singleton
    fun tweetRepository(application: Application): TweetRepository {
        return TweetRepository(application = application)
    }
}
