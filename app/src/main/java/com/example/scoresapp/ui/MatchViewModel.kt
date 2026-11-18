package com.example.scoresapp.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.scoresapp.ScoresApplication
import com.example.scoresapp.data.local.MatchEntity
import com.example.scoresapp.data.local.MatchWithTeams
import com.example.scoresapp.data.local.TeamEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MatchViewModel(app: Application) : AndroidViewModel(app) {

    private val scoresApp = app as ScoresApplication
    private val repo = scoresApp.repository
    private val settings = scoresApp.settings

    private val whileSubscribed = SharingStarted.WhileSubscribed(5_000)

    val darkMode: StateFlow<Boolean> =
        settings.darkMode.stateIn(viewModelScope, whileSubscribed, false)

    val favoritesOnly: StateFlow<Boolean> =
        settings.favoritesOnly.stateIn(viewModelScope, whileSubscribed, false)

    val results: StateFlow<List<MatchWithTeams>> =
        combine(repo.results(), favoritesOnly) { list, onlyFav ->
            if (onlyFav) list.filter { it.match.isFavorite } else list
        }.stateIn(viewModelScope, whileSubscribed, emptyList())

    val upcoming: StateFlow<List<MatchWithTeams>> =
        combine(repo.upcoming(), favoritesOnly) { list, onlyFav ->
            if (onlyFav) list.filter { it.match.isFavorite } else list
        }.stateIn(viewModelScope, whileSubscribed, emptyList())

    val teams: StateFlow<List<TeamEntity>> =
        repo.teams().stateIn(viewModelScope, whileSubscribed, emptyList())

    /** Single match stream used by the details screen (opened via an explicit Intent). */
    fun match(id: Int): Flow<MatchWithTeams?> = repo.match(id)

    fun addMatch(homeId: Int, awayId: Int, kickoff: Long) =
        viewModelScope.launch { repo.addMatch(homeId, awayId, kickoff) }

    fun updateScore(match: MatchEntity, home: Int, away: Int) =
        viewModelScope.launch { repo.updateScore(match, home, away) }

    fun reschedule(match: MatchEntity, newKickoff: Long) =
        viewModelScope.launch { repo.reschedule(match, newKickoff) }

    fun toggleFavorite(match: MatchEntity) =
        viewModelScope.launch { repo.toggleFavorite(match) }

    fun deleteMatch(match: MatchEntity) =
        viewModelScope.launch { repo.deleteMatch(match) }

    fun setDarkMode(enabled: Boolean) =
        viewModelScope.launch { settings.setDarkMode(enabled) }

    fun setFavoritesOnly(enabled: Boolean) =
        viewModelScope.launch { settings.setFavoritesOnly(enabled) }
}
