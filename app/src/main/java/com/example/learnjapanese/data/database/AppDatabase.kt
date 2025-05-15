package com.example.learnjapanese.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.learnjapanese.data.model.Grammar
import com.example.learnjapanese.data.model.LearningProgress
import com.example.learnjapanese.data.model.User
import com.example.learnjapanese.data.model.Vocabulary
import com.example.learnjapanese.utils.com.example.learnjapanese.utils.Converters

@Database(
    entities = [
        User::class,
        LearningProgress::class,
        Vocabulary::class,
        Grammar::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    // Các DAO khác sẽ được thêm vào đây khi cần

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "learn_japanese_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 