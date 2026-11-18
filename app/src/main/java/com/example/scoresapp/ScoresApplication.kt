package com.example.scoresapp

import android.app.Application
import com.example.scoresapp.data.local.AppDatabase
import com.example.scoresapp.data.local.SeedData
import com.example.scoresapp.data.local.SettingsDataStore
import com.example.scoresapp.data.repository.MatchRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class ScoresApplication : Application() {
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val database by lazy { AppDatabase.getInstance(this) }

    val repository by lazy { MatchRepository(database.matchDao(), database.teamDao()) }
    val settings by lazy { SettingsDataStore(this) }

    override fun onCreate() {
        super.onCreate()
        // Seed on first launch. Doing this here (instead of from Room's onCreate
        // callback) guarantees the database instance already exists.
        applicationScope.launch {
            if (database.matchDao().count() == 0) {
                SeedData.populate(database)
            }
        }
    }
}
