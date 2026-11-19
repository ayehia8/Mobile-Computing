# Build setup

## Requirements

- Android Studio, or a JDK 17/21 plus the Android SDK
- Internet access on the first Gradle sync

## Open the project

1. **File > Open** and select the repository root, not a file inside it.
2. Accept the prompt to trust the project.
3. Let Gradle sync. It downloads Gradle 8.9, AGP 8.7.3, Kotlin 2.0.21 and the libraries on the first sync.
4. If Android Studio asks for an SDK, install **Android 15 (API 35)**. `local.properties` is created automatically and must never be committed.
5. Pick an emulator or a device and press **Run**.

## Versions

- Gradle 8.9, Android Gradle Plugin 8.7.3, JDK 17
- Kotlin 2.0.21 with the Compose compiler plugin
- KSP 2.0.21-1.0.28, required by Room
- compileSdk and targetSdk 35, minSdk 24
- Room 2.6.1, DataStore 1.1.1, Lifecycle 2.8.7, Compose BOM 2024.10.01

If you change the Kotlin version, the KSP version has to start with the same number. For Kotlin 2.0.21 that means KSP 2.0.21-1.0.x.

## Layout

```
settings.gradle.kts
build.gradle.kts
gradle.properties
ABSTRACTS.md                    two abstracts
TEAM_CONTRIBUTIONS.md           work split across the six members
app/build.gradle.kts
app/src/main/AndroidManifest.xml
app/src/main/java/com/example/scoresapp/
    ScoresApplication.kt
    data/local/       Entities, Daos, AppDatabase, MatchWithTeams, SeedData, SettingsDataStore
    data/repository/  MatchRepository
    ui/               MatchViewModel, MainActivity, MatchDetailsActivity, MatchFormatting, Theme
app/src/main/res/values/        strings.xml, themes.xml
app/src/main/res/values-night/  themes.xml
```

Each file sits in the folder that matches its `package` line. If you add a file, put it in the matching folder or the compiler will not find it.

## Using the data layer

```kotlin
val vm: MatchViewModel = viewModel()
val results by vm.results.collectAsStateWithLifecycle()
val upcoming by vm.upcoming.collectAsStateWithLifecycle()

LazyColumn {
    items(results, key = { it.match.id }) { item ->
        Text("${item.homeTeam.name} ${item.match.homeScore} - ${item.match.awayScore} ${item.awayTeam.name}")
    }
}
```

## Behaviour notes

- `ScoresApplication.onCreate()` seeds the database with 8 teams and 12 matches when the matches table is empty.
- `MatchRepository.results()` and `upcoming()` re-query once a minute, so a match moves from upcoming to results while the app is open.
- Dark mode and the favourites-only filter are stored in DataStore and survive a restart.
- The main screen has Results and Upcoming tabs, a favourite toggle, a delete action, and the two switches in the app bar.
- Tapping a match starts `MatchDetailsActivity` with an explicit `Intent` that carries the match id in `MatchDetailsActivity.EXTRA_MATCH_ID`. The details screen observes that match through `MatchViewModel.match(id)`.
- The list is one column on phones and a two-column grid from 600 dp upwards, using `BoxWithConstraints`. The details screen is capped at 560 dp.

## Building from the command line

The build needs JDK 17 or 21. If the JDK that Android Studio ships with is newer than that, AGP 8.7.3 rejects it, so point Gradle at a supported one.

Windows:

```
set JAVA_HOME=C:\path\to\jdk-21
gradlew.bat assembleDebug
```

macOS or Linux:

```
export JAVA_HOME=/path/to/jdk-21
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`.

In Android Studio the same setting is under **Settings > Build, Execution, Deployment > Build Tools > Gradle > Gradle JDK**.

## Checking the database

Run the app, then open **View > Tool Windows > App Inspection > Database Inspector** and select `scores.db`. There should be 8 teams and 12 matches. Toggling a favourite or deleting a row updates the list immediately.
