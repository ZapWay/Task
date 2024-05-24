package com.example.task.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentImage(
    @PrimaryKey
    val imageId: String,
    val imageUrl: String,
    val photographer: String
)