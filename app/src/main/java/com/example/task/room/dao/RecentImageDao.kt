package com.example.task.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.task.room.entities.RecentImage

@Dao
interface RecentImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recentImage: RecentImage)

    @Query("SELECT * FROM RecentImage") // Simple query to get all images
    suspend fun getAllImages(): List<RecentImage>

    // Add other query methods if needed
}