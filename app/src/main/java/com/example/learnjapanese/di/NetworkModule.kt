package com.example.learnjapanese.di

import com.example.learnjapanese.data.api.GrammarApiService
import com.example.learnjapanese.data.api.VocabularyApiService
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    /**
     * Cung cấp BASE_URL cho Retrofit
     */
    @Provides
    //fun provideBaseUrl() = "http://10.0.2.2:3000" // Thay đổi thành URL thực tế của API
    fun provideBaseUrl() = "http://192.168.68.156:3000"
    /**
     * Cung cấp Gson cho việc parse JSON
     */
    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()
    
    /**
     * Cung cấp OkHttpClient với logging interceptor
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .build()
    }
    
    /**
     * Cung cấp instance Retrofit
     */
    @Provides
    @Singleton
    fun provideRetrofit(baseUrl: String, okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    
    /**
     * Cung cấp VocabularyApiService
     */
    @Provides
    @Singleton
    fun provideVocabularyApiService(retrofit: Retrofit): VocabularyApiService {
        return retrofit.create(VocabularyApiService::class.java)
    }
    
    /**
     * Cung cấp GrammarApiService
     */
    @Provides
    @Singleton
    fun provideGrammarApiService(retrofit: Retrofit): GrammarApiService {
        return retrofit.create(GrammarApiService::class.java)
    }
} 