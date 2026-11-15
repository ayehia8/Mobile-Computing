package com.example.scoresapp.ui

import com.example.scoresapp.data.local.MatchWithTeams
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats a kickoff timestamp for display. A fresh formatter is created per call so the app
 * never shares a mutable [SimpleDateFormat] instance.
 */
internal fun formatKickoff(epochMillis: Long): String =
    SimpleDateFormat("EEE d MMM · HH:mm", Locale.getDefault()).format(Date(epochMillis))

/** "2 - 1" once the match has a score, otherwise "vs". */
internal fun MatchWithTeams.scoreLabel(): String {
    val home = match.homeScore
    val away = match.awayScore
    return if (home != null && away != null) "$home - $away" else "vs"
}

/** Whether the match already has a final score. */
internal fun MatchWithTeams.isPlayed(): Boolean =
    match.homeScore != null && match.awayScore != null
