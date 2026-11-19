# Team Work Division

Six members worked on the project. Each person committed their own files from their own account, and the sections below list the files they own and the points they should be ready to explain in the discussion.

## Roster

| # | Name | Student ID | GitHub | Area |
|---|------|------------|--------|------|
| 1 | Ali Yahya Mohamed Gaber | 2023170374 | ayehia8 | Build setup, Room data layer, repository, integration |
| 2 | Mohamed Omar Ahmed Mokhtar Harash | 2023170524 | Harash2005 | Sample data and user preferences |
| 3 | Baraa Abdullah Fathy Othman | 2023170135 | Albaraa-abdullah | Intent navigation and details screen |
| 4 | Youssef Samy Hanna Shafiq | 2022170517 | Josamy | Main screen, theme and view model |
| 5 | Mohamed Hassan Mohamed Abdeltawab | 2022170363 | Gomgma | Documentation and abstracts |
| 6 | Mostafa Walid Metwally Mohamed | 2023170616 | mostafawaleed235 | Sample data review and cleanup |

## 1. Ali Yahya Mohamed Gaber — build setup, data layer and integration

**Files:** `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `gradle/wrapper/*`, `app/build.gradle.kts`, `app/proguard-rules.pro`, `app/src/main/AndroidManifest.xml`, `app/src/main/res/values/*`, `app/src/main/res/values-night/*`, `ScoresApplication.kt`, `data/local/Entities.kt`, `data/local/MatchWithTeams.kt`, `data/local/AppDatabase.kt`, `data/local/Daos.kt`, `data/repository/MatchRepository.kt`

**What it does:** sets up Gradle with the Kotlin, Compose, KSP, Room and DataStore dependencies, defines the two Room entities and their foreign keys, writes the DAO interfaces with the CRUD queries, builds the repository that re-queries the database every minute, and merges the other branches into `main`.

**Points to explain:**

- why the database and the repository are created in `Application` and not in the activity
- what `SupervisorJob` does and why seeding runs on `Dispatchers.IO`
- why the foreign keys use `CASCADE` and why both team id columns are indexed
- how `@Relation` avoids a separate query for each team
- why `flatMapLatest` is used with the timer in the repository

## 2. Mohamed Omar Ahmed Mokhtar Harash — sample data and preferences

**Files:** `data/local/SeedData.kt`, `data/local/SettingsDataStore.kt`

**What it does:** defines the eight teams and twelve matches that are inserted on first launch, with kickoff times relative to the current time so the app always has both results and fixtures. Also wraps the Preferences DataStore that stores dark mode and the favourites-only filter.

**Points to explain:**

- why seeding only runs when `count()` returns zero
- why DataStore is used instead of `SharedPreferences`
- what the `catch` operator does when the preferences file cannot be read
- what happens to the settings after the process is killed

## 3. Baraa Abdullah Fathy Othman — Intent navigation and details screen

**Files:** `ui/MatchDetailsActivity.kt`, `ui/MatchFormatting.kt`, the `MatchDetailsActivity` entry in `AndroidManifest.xml`

**What it does:** adds the second activity, reads the match id from the intent extras, observes that match through the view model, and shows the teams, score, kickoff time and status. Also holds the small formatting helpers that both screens use.

**Points to explain:**

- the difference between an explicit and an implicit Intent, and why this one is explicit
- what happens when the extra is missing (the `-1` check and `finish()`)
- how the details screen gets live data from the same Room row
- why the date formatter is created on each call instead of being a shared field

## 4. Youssef Samy Hanna Shafiq — main screen and view model

**Files:** `ui/MainActivity.kt`, `ui/Theme.kt`, `ui/MatchViewModel.kt`

**What it does:** builds the results and upcoming tabs, the match card with its favourite and delete buttons, the empty states, and the responsive list that switches between one and two columns. The view model exposes the flows that the screen collects.

**Points to explain:**

- how `rememberSaveable` keeps the selected tab after rotation
- how `BoxWithConstraints` chooses between `LazyColumn` and `LazyVerticalGrid`
- why `collectAsStateWithLifecycle` is used instead of `collectAsState`
- what `stateIn(..., WhileSubscribed(5_000), ...)` does when the screen goes to the background
- how the favourites-only setting is combined with the Room stream

## 5. Mohamed Hassan Mohamed Abdeltawab — documentation

**Files:** `ABSTRACTS.md`, `SETUP.md`, `TEAM_CONTRIBUTIONS.md`

**What it does:** wrote the two abstracts, the setup and build instructions, and this division document.

**Points to explain:**

- how each abstract maps to the code
- the JDK 21 requirement and why the bundled Android Studio runtime does not work with AGP 8.7.3
- how the work split matches the commits on GitHub

## 6. Mostafa Walid Metwally Mohamed — sample data review

**Files:** `data/local/SeedData.kt`, `data/local/SettingsDataStore.kt`

**What it does:** reviewed the seeded rows and the preference keys, then tidied the end of both files.

**Points to explain:**

- what the seed data contains and how to inspect it in the Database Inspector
- which keys are stored in DataStore and what type each one has
- what would need to change to add a new team or a new match

## Working on GitHub

The repository was created by Ali and the other five members were added as collaborators. Each member worked on their own branch (`feature/room-data-layer`, `feature/ui-ux`, `Inent-Details`, `feature/sample-data-and-preferences`, `Mohamed_Hassan`) and opened a pull request against `main`. Every commit was made from the member's own account, so the contributor graph and the commit history show who wrote what. The pull requests were merged in dependency order: data layer first, then the UI, then details and documentation.

## Order for the discussion

1. Ali: project structure, build files and the Room schema.
2. Mohamed Omar: the seed data and the DataStore settings.
3. Baraa: open a match and walk through the Intent.
4. Youssef: the tabs, the card actions and the two-column layout.
5. Mohamed Hassan: the two abstracts.
6. Mostafa: the seeded rows in the Database Inspector.
