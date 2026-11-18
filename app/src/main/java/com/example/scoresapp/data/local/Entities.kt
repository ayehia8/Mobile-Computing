package com.example.scoresapp.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val logoUrl: String? = null
)

@Entity(
    tableName = "matches",
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["homeTeamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["id"],
            childColumns = ["awayTeamId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("homeTeamId"), Index("awayTeamId"), Index("kickoffTime")]
)
data class MatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val kickoffTime: Long,        // epoch millis
    val homeScore: Int? = null,   // null = not played yet
    val awayScore: Int? = null,
    val isFavorite: Boolean = false
)
