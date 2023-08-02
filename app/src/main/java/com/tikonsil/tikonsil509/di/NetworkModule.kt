package com.tikonsil.tikonsil509.di

import com.tikonsil.tikonsil509.data.remote.api.TikonsilApi
import com.tikonsil.tikonsil509.data.remote.provider.firebaseApi.FirebaseApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

   /* @Provides
    @Singleton
    @Named("base_url")
    fun provideBaseUrl(): String = runBlocking { FirebaseApi.getFSApis().base_url_firebase_instance }



    @Provides
    @Singleton
    fun provideRetrofit(@Named("base_url") url: String): Retrofit {
        return Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideTikonsilApiService(retrofit: Retrofit): TikonsilApi {
        return retrofit.create(TikonsilApi::class.java)
    }*/
}
