package com.example.learnjapanese.di

import android.content.Context
import com.example.learnjapanese.data.dao.LearningProgressDao
import com.example.learnjapanese.data.dao.UserDao
import com.example.learnjapanese.data.database.AppDatabase
import com.example.learnjapanese.data.repository.LearningProgressRepository
import com.example.learnjapanese.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }
    
    @Provides
    @Singleton
    fun provideUserDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }
    
    @Provides
    @Singleton
    fun provideLearningProgressDao(appDatabase: AppDatabase): LearningProgressDao {
        return appDatabase.learningProgressDao()
    }
    
    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepository(userDao)
    }
    
    @Provides
    @Singleton
    fun provideLearningProgressRepository(learningProgressDao: LearningProgressDao): LearningProgressRepository {
        return LearningProgressRepository(learningProgressDao)
    }
} 