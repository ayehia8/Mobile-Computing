package com.example.scoresapp.data.local

import androidx.room.Embedded
import androidx.room.Relation

/** A match together with both team rows, so the UI can show names/logos directly. */
data class MatchWithTeams(
    @Embedded val match: MatchEntity,
    @Relation(parentColumn = "homeTeamId", entityColumn = "id")
    val homeTeam: TeamEntity,
    @Relation(parentColumn = "awayTeamId", entityColumn = "id")
    val awayTeam: TeamEntity
)
