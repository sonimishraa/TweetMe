package com.soni.tweetme.di

import android.app.Application
import android.content.Context
import com.google.firebase.auth.FirebaseAuth
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

    @Singleton
    @Provides
    fun fireBaseAuth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    /* Repository */
    @Provides
    @Singleton
    fun tweetRepository(firebaseAuth: FirebaseAuth): TweetRepository {
        return TweetRepository(firebaseAuth)
    }
}
