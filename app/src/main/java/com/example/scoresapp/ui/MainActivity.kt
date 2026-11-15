package com.example.scoresapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as gridItems
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.scoresapp.data.local.MatchWithTeams

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: MatchViewModel = viewModel()
            val darkMode by viewModel.darkMode.collectAsStateWithLifecycle()
            ScoresTheme(darkTheme = darkMode) {
                ScoresApp(viewModel = viewModel, darkMode = darkMode)
            }
        }
    }
}

private enum class ScoresTab { RESULTS, UPCOMING }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScoresApp(viewModel: MatchViewModel, darkMode: Boolean) {
    var selectedTab by rememberSaveable { mutableStateOf(ScoresTab.RESULTS) }
    val context = LocalContext.current

    val favoritesOnly by viewModel.favoritesOnly.collectAsStateWithLifecycle()
    val results by viewModel.results.collectAsStateWithLifecycle()
    val upcoming by viewModel.upcoming.collectAsStateWithLifecycle()

    val matches = if (selectedTab == ScoresTab.RESULTS) results else upcoming

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scores") },
                actions = {
                    IconToggleButton(
                        checked = favoritesOnly,
                        onCheckedChange = viewModel::setFavoritesOnly
                    ) {
                        Icon(
                            imageVector = if (favoritesOnly) {
                                Icons.Filled.Favorite
                            } else {
                                Icons.Filled.FavoriteBorder
                            },
                            contentDescription = "Show favorites only"
                        )
                    }
                    IconToggleButton(
                        checked = darkMode,
                        onCheckedChange = viewModel::setDarkMode
                    ) {
                        Icon(
                            imageVector = Icons.Filled.DarkMode,
                            contentDescription = "Dark mode"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                Tab(
                    selected = selectedTab == ScoresTab.RESULTS,
                    onClick = { selectedTab = ScoresTab.RESULTS },
                    text = { Text("Results") }
                )
                Tab(
                    selected = selectedTab == ScoresTab.UPCOMING,
                    onClick = { selectedTab = ScoresTab.UPCOMING },
                    text = { Text("Upcoming") }
                )
            }

            if (matches.isEmpty()) {
                EmptyState(selectedTab)
            } else {
                MatchList(
                    matches = matches,
                    onMatchClick = { matchId ->
                        context.startActivity(
                            Intent(context, MatchDetailsActivity::class.java)
                                .putExtra(MatchDetailsActivity.EXTRA_MATCH_ID, matchId)
                        )
                    },
                    onToggleFavorite = { item -> viewModel.toggleFavorite(item.match) },
                    onDelete = { item -> viewModel.deleteMatch(item.match) }
                )
            }
        }
    }
}

/**
 * One column on phones, two on tablets (>= 600dp wide). The same cards are reused in both
 * layouts, so the screen adapts without duplicating UI code.
 */
@Composable
private fun MatchList(
    matches: List<MatchWithTeams>,
    onMatchClick: (Int) -> Unit,
    onToggleFavorite: (MatchWithTeams) -> Unit,
    onDelete: (MatchWithTeams) -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val wideLayout = maxWidth >= 600.dp
        val contentPadding = PaddingValues(16.dp)
        val spacing = 12.dp

        if (wideLayout) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = contentPadding,
                horizontalArrangement = Arrangement.spacedBy(spacing),
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                gridItems(matches, key = { it.match.id }) { item ->
                    MatchCard(
                        item = item,
                        onClick = { onMatchClick(item.match.id) },
                        onToggleFavorite = { onToggleFavorite(item) },
                        onDelete = { onDelete(item) }
                    )
                }
            }
        } else {
            LazyColumn(
                contentPadding = contentPadding,
                verticalArrangement = Arrangement.spacedBy(spacing)
            ) {
                items(matches, key = { it.match.id }) { item ->
                    MatchCard(
                        item = item,
                        onClick = { onMatchClick(item.match.id) },
                        onToggleFavorite = { onToggleFavorite(item) },
                        onDelete = { onDelete(item) }
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyState(tab: ScoresTab) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (tab == ScoresTab.RESULTS) {
                "No results yet"
            } else {
                "No upcoming matches"
            },
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Composable
private fun MatchCard(
    item: MatchWithTeams,
    onClick: () -> Unit,
    onToggleFavorite: () -> Unit,
    onDelete: () -> Unit
) {
    val match = item.match
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = item.homeTeam.name, style = MaterialTheme.typography.titleMedium)
                Text(text = item.awayTeam.name, style = MaterialTheme.typography.titleMedium)
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = item.scoreLabel(),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = formatKickoff(match.kickoffTime),
                    style = MaterialTheme.typography.labelSmall
                )
            }

            IconButton(onClick = onToggleFavorite) {
                Icon(
                    imageVector = if (match.isFavorite) {
                        Icons.Filled.Favorite
                    } else {
                        Icons.Filled.FavoriteBorder
                    },
                    tint = if (match.isFavorite) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    contentDescription = "Toggle favorite"
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Delete match"
                )
            }
        }
    }
}
