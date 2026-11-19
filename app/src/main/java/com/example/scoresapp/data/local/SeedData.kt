package com.example.scoresapp.data.local

object SeedData {
    private const val DAY = 24L * 60 * 60 * 1000

    suspend fun populate(db: AppDatabase) {
        val now = System.currentTimeMillis()

        db.teamDao().insertAll(
            listOf(
                TeamEntity(1, "Al Ahly"),
                TeamEntity(2, "Zamalek"),
                TeamEntity(3, "Pyramids"),
                TeamEntity(4, "Ismaily"),
                TeamEntity(5, "Al Masry"),
                TeamEntity(6, "Future FC"),
                TeamEntity(7, "Ceramica Cleopatra"),
                TeamEntity(8, "Smouha")
            )
        )

        db.matchDao().insertAll(
            listOf(
                // Past matches (with results)
                MatchEntity(homeTeamId = 1, awayTeamId = 2, kickoffTime = now - 14 * DAY, homeScore = 2, awayScore = 1),
                MatchEntity(homeTeamId = 3, awayTeamId = 4, kickoffTime = now - 12 * DAY, homeScore = 0, awayScore = 0),
                MatchEntity(homeTeamId = 5, awayTeamId = 6, kickoffTime = now - 9 * DAY, homeScore = 1, awayScore = 3),
                MatchEntity(homeTeamId = 7, awayTeamId = 8, kickoffTime = now - 7 * DAY, homeScore = 2, awayScore = 2),
                MatchEntity(homeTeamId = 2, awayTeamId = 3, kickoffTime = now - 4 * DAY, homeScore = 1, awayScore = 0),
                MatchEntity(homeTeamId = 4, awayTeamId = 1, kickoffTime = now - 2 * DAY, homeScore = 0, awayScore = 2),
                // Upcoming matches (no scores yet)
                MatchEntity(homeTeamId = 1, awayTeamId = 3, kickoffTime = now + 2 * DAY),
                MatchEntity(homeTeamId = 6, awayTeamId = 2, kickoffTime = now + 3 * DAY),
                MatchEntity(homeTeamId = 8, awayTeamId = 5, kickoffTime = now + 5 * DAY),
                MatchEntity(homeTeamId = 4, awayTeamId = 7, kickoffTime = now + 7 * DAY),
                MatchEntity(homeTeamId = 2, awayTeamId = 1, kickoffTime = now + 10 * DAY),
                MatchEntity(homeTeamId = 3, awayTeamId = 6, kickoffTime = now + 14 * DAY)
            )
        )
    }
}


