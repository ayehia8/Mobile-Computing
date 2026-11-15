# Team Work Division — 6 Members

The project is split so that every member owns a coherent slice of the code, can explain it
during the discussion, and has their own files to commit. Replace the names in the roster with
the real team members.

## Roster and ownership

| # | Member | Role | Owned files | Share |
|---|--------|------|-------------|-------|
| 1 | [Member 1] | Project lead / build & app shell | `build.gradle.kts`, `settings.gradle.kts`, `gradle.properties`, `app/build.gradle.kts`, `AndroidManifest.xml`, `res/values/*`, `res/values-night/*`, `.gitignore`, `ScoresApplication.kt`, `Theme.kt` | ~10% |
| 2 | [Member 2] | Data modelling & Room | `data/local/Entities.kt`, `data/local/MatchWithTeams.kt`, `data/local/AppDatabase.kt`, `data/local/Daos.kt` | ~17% |
| 3 | [Member 3] | Sample data & user preferences | `data/local/SeedData.kt`, `data/local/SettingsDataStore.kt`, settings part of `MatchViewModel.kt` | ~10% |
| 4 | [Member 4] | Repository, coroutines & state | `data/repository/MatchRepository.kt`, `MatchViewModel.kt` (match/team streams) | ~15% |
| 5 | [Member 5] | Main screen & responsive UI | `ui/MainActivity.kt` (tabs, cards, favorite/delete, adaptive grid) | ~31% |
| 6 | [Member 6] | Intent navigation, details screen & docs | `ui/MatchDetailsActivity.kt`, `ui/MatchFormatting.kt`, Intent call site + manifest activity entry, `ABSTRACTS.md`, `SETUP.md` | ~17% |

---

## Member 1 — Project lead / build & app shell

**Built**
- Gradle setup (Kotlin 2.0.21, AGP 8.7.3, Compose compiler plugin, KSP, Room, DataStore).
- App manifest, theme resources, and the Material 3 theme wrapper.
- `ScoresApplication`: application-wide singletons for the database, repository, and settings,
  plus first-launch seeding kickoff on `Dispatchers.IO`.

**Be ready to explain**
- Why the database/repository are created lazily in `Application` instead of in the Activity.
- What `SupervisorJob` does and why seeding runs off the main thread.
- How the Compose compiler plugin and KSP are wired in the build files.

**Suggested commits**
1. `chore: configure Gradle, Compose, KSP and Room dependencies`
2. `feat: add application class with lazy database and repository`
3. `style: add Material 3 light/dark themes and manifest entries`

---

## Member 2 — Data modelling & Room

**Built**
- `TeamEntity` and `MatchEntity` with a primary key, two foreign keys to `teams`
  (cascade delete), indexes on both team ids and `kickoffTime`.
- `MatchWithTeams` (`@Embedded` + two `@Relation`s) so one query returns a match and both teams.
- `AppDatabase` singleton and the `TeamDao` / `MatchDao` interfaces.
- Complete CRUD queries: `insert`, `insertAll`, `getResults`, `getUpcoming`, `getById`, `count`,
  `update`, `setFavorite`, `delete`, `deleteById`.

**Be ready to explain**
- Why the relation object avoids N+1 queries and what `@Transaction` guarantees.
- Why the foreign keys use `CASCADE` and why the indexed columns matter.
- The difference between `@Query`, `@Insert`, `@Update`, and `@Delete`.

**Suggested commits**
1. `feat: add team and match entities with foreign keys`
2. `feat: add MatchWithTeams relation and Room database`
3. `feat: implement match CRUD DAO queries`

---

## Member 3 — Sample data & user preferences

**Built**
- `SeedData`: 8 teams and 12 matches, with kickoff times relative to "now" so the app has
  results and upcoming fixtures immediately.
- `SettingsDataStore`: Preferences DataStore for dark mode and favorites-only, with an
  `IOException` fallback to `emptyPreferences()`.
- Settings flows and setters used by the view model.

**Be ready to explain**
- Why seeding is guarded by `count() == 0` and why it runs on first launch only.
- Why DataStore is preferred over `SharedPreferences` and how the `catch` operator makes it safe.
- What happens to the settings after process death.

**Suggested commits**
1. `feat: seed sample teams and matches on first launch`
2. `feat: persist dark mode and favorites-only with DataStore`

---

## Member 4 — Repository, coroutines & state

**Built**
- `MatchRepository`: `nowFlow()` emits the current time and refreshes every minute;
  `flatMapLatest` re-runs the Room queries so matches move from upcoming to results live.
- CRUD wrappers: `addMatch`, `updateScore`, `reschedule`, `toggleFavorite`, `deleteMatch`.
- `MatchViewModel`: `StateFlow` streams for results, upcoming, teams, dark mode, and
  favorites-only; filters favorites when the preference is on; exposes `match(id)` for the
  details screen.

**Be ready to explain**
- Why `flatMapLatest` is the right operator for "re-query with a new time value".
- What `stateIn(..., WhileSubscribed(5_000), ...)` does when the screen is backgrounded.
- How the favorites-only preference is combined with the Room stream.

**Suggested commits**
1. `feat: add repository with reactive time-based queries`
2. `feat: expose view-model state streams for lists and settings`
3. `feat: add single-match stream for the details screen`

---

## Member 5 — Main screen & responsive UI

**Built**
- `MainActivity` shell, Results/Upcoming tabs, top app bar with favorites-only and dark-mode toggles.
- `MatchCard`: team names, score or "vs", kickoff time, favorite toggle, delete action.
- Empty states and the responsive `MatchList`: single column on phones, two-column grid at
  ≥600 dp via `BoxWithConstraints`, reusing the same card composable.
- Accessibility labels (`contentDescription`) on every icon button.

**Be ready to explain**
- How `rememberSaveable` keeps the selected tab across rotation.
- How `BoxWithConstraints` decides between `LazyColumn` and `LazyVerticalGrid`.
- Why state is hoisted (callbacks passed up to the view model) and why `collectAsStateWithLifecycle`
  is used instead of `collectAsState`.
- Why the shared `SimpleDateFormat` was replaced by a per-call formatter.

**Suggested commits**
1. `feat: build main scores screen with results and upcoming tabs`
2. `feat: add match cards with favorite and delete actions`
3. `feat: make the match list responsive with a two-column grid`

---

## Member 6 — Intent navigation, details screen & documentation

**Built**
- `MatchDetailsActivity`: a second activity launched with an explicit `Intent` carrying
  `EXTRA_MATCH_ID`; validates the extra and finishes if it is missing.
- Details UI: teams, score or "vs", kickoff time, finished/upcoming status, favorite indicator,
  back navigation, and a width-constrained scrollable layout.
- Intent call site in `MatchCard` (`Intent(context, MatchDetailsActivity::class.java)`).
- `MatchFormatting.kt` shared formatting helpers; manifest activity registration.
- `ABSTRACTS.md` (two ACM-format abstracts), `SETUP.md`, and this work-division document.

**Be ready to explain**
- The difference between explicit and implicit intents and why this navigation uses an explicit one.
- How `getIntExtra` is validated and what happens for an invalid id.
- How the details screen observes the same Room row through `MatchViewModel.match(id)`.
- How the two abstracts map to the implementation.

**Suggested commits**
1. `feat: add match details activity opened via explicit Intent`
2. `refactor: extract shared match formatting helpers`
3. `docs: add ACM-format abstracts and team work division`

---

## Making the collaboration visible on GitHub

1. One member creates the repository and invites the other five as collaborators.
2. Every member clones the repository and works on their own branch
   (`feature/room-schema`, `feature/main-ui`, ...), then opens a pull request.
3. Each member commits **only their own files** with their **own GitHub account**; do not
   rewrite author names or fabricate history.
4. A different member reviews and merges each pull request. Merge order:
   build setup → Room → DataStore/seed → repository/view model → main UI → details/Intent/docs.
5. Create one issue per member ("Room schema and DAO", "Responsive main screen", ...) and link
   the pull request to it, so the rubric's "visible GitHub activity" is easy to verify.

## Suggested demo order for the individual discussion

1. Member 1 opens the project and shows the build and app startup.
2. Member 2 opens the Database Inspector and demonstrates the tables and relations.
3. Member 3 toggles dark mode / favorites-only, then restarts the app to show persistence.
4. Member 4 explains how a match moves from Upcoming to Results while the app is open.
5. Member 5 resizes the emulator to show the one-column and two-column layouts.
6. Member 6 taps a card, shows the details screen, and walks through the Intent extra.
