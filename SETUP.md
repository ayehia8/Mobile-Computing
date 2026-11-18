# Scores App — build setup

This folder is now a complete Android Studio (Gradle) project.

## Open it
1. Android Studio > **File > Open** and select this `Mobile` folder
   (not a file inside it).
2. Accept any prompt to trust the project.
3. Let Gradle sync. It downloads Gradle 8.9, AGP 8.7.3, Kotlin 2.0.21 and all
   libraries on the first sync (needs internet).
4. If Android Studio asks for an SDK, let it install **Android 15 (API 35)**
   (or point it to an existing SDK). `local.properties` is created
   automatically — never share it.
5. Pick an emulator/device and press **Run**.

## Versions used
- Gradle 8.9, Android Gradle Plugin 8.7.3, JDK 17 (Android Studio's bundled JDK)
- Kotlin 2.0.21 + Compose compiler plugin 2.0.21
- KSP 2.0.21-1.0.28 (needed by Room)
- compileSdk/targetSdk 35, minSdk 24
- Room 2.6.1, DataStore 1.1.1, Lifecycle 2.8.7, Compose BOM 2024.10.01

To bump Kotlin, keep this rule: the KSP version must start with the Kotlin
version (e.g. Kotlin 2.0.21 -> KSP 2.0.21-1.0.x).

## Layout
    settings.gradle.kts
    build.gradle.kts
    gradle.properties
    ABSTRACTS.md                     Two ACM-format project abstracts
    TEAM_CONTRIBUTIONS.md            Work split across six team members
    app/build.gradle.kts
    app/src/main/AndroidManifest.xml
    app/src/main/java/com/example/scoresapp/
        ScoresApplication.kt
        data/local/      Entities, Daos, AppDatabase, MatchWithTeams,
                         SeedData, SettingsDataStore
        data/repository/ MatchRepository
        ui/              MatchViewModel, MainActivity, MatchDetailsActivity,
                         MatchFormatting, Theme
    app/src/main/res/values/         strings.xml, themes.xml
    app/src/main/res/values-night/   themes.xml

Every file sits in the folder matching its `package` line — that is what makes
the files able to "see" each other. If you move/add a file, put it in the
matching folder or Android Studio will flag it.

## Using the data layer (for teammates)
    val vm: MatchViewModel = viewModel()
    val results by vm.results.collectAsStateWithLifecycle()
    val upcoming by vm.upcoming.collectAsStateWithLifecycle()

    LazyColumn {
        items(results, key = { it.match.id }) { item ->
            Text("${item.homeTeam.name} ${item.match.homeScore} - ${item.match.awayScore} ${item.awayTeam.name}")
        }
    }

## Behaviour notes
- `ScoresApplication.onCreate()` seeds the database (8 teams, 12 matches) when
  the matches table is empty.
- `MatchRepository.results()` / `upcoming()` re-query once a minute, so matches
  move from "upcoming" to "results" while the app is open.
- Settings (dark mode / favorites only) are stored in DataStore and survive
  restarts.
- The main screen has Results/Upcoming tabs, favorite toggle, delete, and
  dark-mode + favorites-only switches in the app bar.
- Tapping a match starts `MatchDetailsActivity` with an explicit `Intent` that
  carries the match id (`MatchDetailsActivity.EXTRA_MATCH_ID`). The details
  screen observes that match through `MatchViewModel.match(id)`.
- The match list is one column on phones and a two-column grid at 600dp and
  wider (`BoxWithConstraints`). The details screen is capped at 560dp.

## Building from the command line
The Gradle build needs a JDK 17 or 21. This machine's Android Studio runtime
(`D:\IDEs\Mobile\jbr`) is JDK 25, which AGP 8.7.3 does not support, so point
Gradle at JDK 21:

    $env:JAVA_HOME="D:\IDEs\New folder\eclipse\plugins\org.eclipse.justj.openjdk.hotspot.jre.full.win32.x86_64_21.0.5.v20241023-1957\jre"
    .\gradlew.bat assembleDebug

In Android Studio, set **Settings > Build, Execution, Deployment > Build Tools >
Gradle > Gradle JDK** to a JDK 17 or 21 installation. The APK is written to
`app/build/outputs/apk/debug/app-debug.apk`.

## Check it works
Run on an emulator, then open **View > Tool Windows > App Inspection >
Database Inspector** and select `scores.db`. You should see 8 teams and
12 matches; toggling favorite / deleting a row updates the list live.
