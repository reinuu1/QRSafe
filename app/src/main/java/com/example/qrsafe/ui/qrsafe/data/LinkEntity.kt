package com.example.qrsafe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links")
data class LinkEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val url: String,
    val status: String, // SAFE, MALICIOUS, SUSPECT, ERROR
    val timestamp: Long = System.currentTimeMillis()
)
