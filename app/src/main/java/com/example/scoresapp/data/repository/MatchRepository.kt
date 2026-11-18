package com.example.scoresapp.data.repository

import com.example.scoresapp.data.local.MatchDao
import com.example.scoresapp.data.local.MatchEntity
import com.example.scoresapp.data.local.MatchWithTeams
import com.example.scoresapp.data.local.TeamDao
import com.example.scoresapp.data.local.TeamEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow

class MatchRepository(
    private val matchDao: MatchDao,
    private val teamDao: TeamDao
) {
    /**
     * Emits the current time immediately, then once a minute. Re-querying with a fresh
     * "now" makes matches move from upcoming to results on their own, instead of only
     * after the process is restarted.
     */
    private fun nowFlow(): Flow<Long> = flow {
        while (true) {
            emit(System.currentTimeMillis())
            delay(60_000L)
        }
    }

    // Read
    @OptIn(ExperimentalCoroutinesApi::class)
    fun results(): Flow<List<MatchWithTeams>> =
        nowFlow().flatMapLatest { now -> matchDao.getResults(now) }

    @OptIn(ExperimentalCoroutinesApi::class)
    fun upcoming(): Flow<List<MatchWithTeams>> =
        nowFlow().flatMapLatest { now -> matchDao.getUpcoming(now) }

    fun match(id: Int): Flow<MatchWithTeams?> = matchDao.getById(id)

    fun teams(): Flow<List<TeamEntity>> = teamDao.getAll()

    // Create
    suspend fun addMatch(homeTeamId: Int, awayTeamId: Int, kickoffTime: Long) {
        matchDao.insert(
            MatchEntity(
                homeTeamId = homeTeamId,
                awayTeamId = awayTeamId,
                kickoffTime = kickoffTime
            )
        )
    }

    // Update
    suspend fun updateScore(match: MatchEntity, home: Int, away: Int) =
        matchDao.update(match.copy(homeScore = home, awayScore = away))

    suspend fun reschedule(match: MatchEntity, newKickoff: Long) =
        matchDao.update(match.copy(kickoffTime = newKickoff))

    suspend fun toggleFavorite(match: MatchEntity) =
        matchDao.setFavorite(match.id, !match.isFavorite)

    // Delete
    suspend fun deleteMatch(match: MatchEntity) = matchDao.delete(match)
}
