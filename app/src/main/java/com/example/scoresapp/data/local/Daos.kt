package com.example.scoresapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(teams: List<TeamEntity>)

    @Query("SELECT * FROM teams ORDER BY name")
    fun getAll(): Flow<List<TeamEntity>>
}

@Dao
interface MatchDao {

    // ---- Create ----
    @Insert
    suspend fun insert(match: MatchEntity): Long

    @Insert
    suspend fun insertAll(matches: List<MatchEntity>)

    // ---- Read ----
    @Transaction
    @Query("SELECT * FROM matches WHERE kickoffTime < :now ORDER BY kickoffTime DESC")
    fun getResults(now: Long): Flow<List<MatchWithTeams>>

    @Transaction
    @Query("SELECT * FROM matches WHERE kickoffTime >= :now ORDER BY kickoffTime ASC")
    fun getUpcoming(now: Long): Flow<List<MatchWithTeams>>

    @Transaction
    @Query("SELECT * FROM matches WHERE id = :id")
    fun getById(id: Int): Flow<MatchWithTeams?>

    @Query("SELECT COUNT(*) FROM matches")
    suspend fun count(): Int

    // ---- Update ----
    @Update
    suspend fun update(match: MatchEntity)

    @Query("UPDATE matches SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: Int, favorite: Boolean)

    // ---- Delete ----
    @Delete
    suspend fun delete(match: MatchEntity)

    @Query("DELETE FROM matches WHERE id = :id")
    suspend fun deleteById(id: Int)
}
