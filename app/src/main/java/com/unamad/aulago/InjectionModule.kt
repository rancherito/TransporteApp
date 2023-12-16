package com.unamad.aulago

import android.content.Context
import androidx.room.Room
import com.unamad.aulago.api.IApiGeneral
import com.unamad.aulago.emums.ServerAddressType
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
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
                "${SystemDatabase::class.simpleName!!}_beta_03".lowercase()
            ).build()
        }
    }


    @Singleton
    @Provides
    fun providesOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(90, TimeUnit.SECONDS)
            .readTimeout(90, TimeUnit.SECONDS)
            .build()


    @Singleton
    @Provides
    fun provideApiService(okHttpClient: OkHttpClient): IApiGeneral = Retrofit.Builder()
        .baseUrl(if(SystemConfig.isProduction) ServerAddressType.Production.address else SystemConfig.serverAddress.address)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build().create(IApiGeneral::class.java)
}