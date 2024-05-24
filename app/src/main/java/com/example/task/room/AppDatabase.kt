package com.example.task.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.task.room.dao.RecentImageDao
import com.example.task.room.entities.RecentImage

@Database(entities = [RecentImage::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun recentImageDao(): RecentImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "recent_images_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}