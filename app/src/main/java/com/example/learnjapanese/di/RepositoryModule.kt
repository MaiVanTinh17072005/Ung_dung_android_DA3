package com.example.learnjapanese.di

import com.example.learnjapanese.data.api.AIChatApiService
import com.example.learnjapanese.data.api.GrammarApiService
import com.example.learnjapanese.data.api.ReadingApiService
import com.example.learnjapanese.data.api.VocabularyApiService
import com.example.learnjapanese.data.repository.AIChatRepository
import com.example.learnjapanese.data.repository.GrammarRepository
import com.example.learnjapanese.data.repository.ReadingRepository
import com.example.learnjapanese.data.repository.VocabularyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    
    @Provides
    @Singleton
    fun provideVocabularyRepository(
        vocabularyApiService: VocabularyApiService
    ): VocabularyRepository {
        return VocabularyRepository(vocabularyApiService)
    }
    
    @Provides
    @Singleton
    fun provideGrammarRepository(
        grammarApiService: GrammarApiService
    ): GrammarRepository {
        return GrammarRepository(grammarApiService)
    }
    
    @Provides
    @Singleton
    fun provideAIChatRepository(
        aiChatApiService: AIChatApiService
    ): AIChatRepository {
        return AIChatRepository(aiChatApiService)
    }
    
    @Provides
    @Singleton
    fun provideReadingRepository(
        readingApiService: ReadingApiService
    ): ReadingRepository {
        return ReadingRepository(readingApiService)
    }
} 