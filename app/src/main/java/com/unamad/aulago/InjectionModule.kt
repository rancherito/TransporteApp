package com.unamad.aulago

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object InjectionModule {

    @Provides
    @Singleton
    fun provideApplicationContext(@ApplicationContext context: Context): Context = context

    @Singleton
    @Provides
    fun provideCoroutineScope(): CoroutineScope = CoroutineScope(Dispatchers.IO)

    @Singleton
    @Provides
    fun providerSystemDatabase(@ApplicationContext appContext: Context): SystemDatabase {
        synchronized(this) {
            return Room.databaseBuilder(
                appContext.applicationContext,
                SystemDatabase::class.java,
                "${SystemDatabase::class.simpleName!!}_beta_05".lowercase()
            ).build()
        }
    }


}