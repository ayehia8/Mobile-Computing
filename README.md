# Scores App

An Android app for browsing football results and upcoming fixtures, built with Kotlin and Jetpack Compose.

## What it does

- Results tab for finished matches, upcoming tab for fixtures that have not started
- Each match card shows both teams, the score (or "vs"), the kickoff time, a favourite button and a delete button
- Tapping a card opens the details screen, a second activity started with an explicit Intent
- Dark mode and a favourites-only filter, both saved with DataStore
- One column on phones, two columns from 600 dp upwards
- Matches move from upcoming to results while the app is open, because the repository re-queries the database every minute

## How it is built

- Kotlin 2.0.21, AGP 8.7.3, minSdk 24, targetSdk 35
- Jetpack Compose with Material 3
- Room for the teams and matches tables, with foreign keys and a relation object
- DataStore for the user settings
- MVVM: Compose screen -> MatchViewModel -> MatchRepository -> Room / DataStore

## Project layout

```
app/src/main/java/com/example/scoresapp/
    ScoresApplication.kt
    data/local/          entities, DAO, database, seed data, settings
    data/repository/     MatchRepository
    ui/                  activities, view model, theme, formatting helpers
```

## Building

The build needs JDK 17 or 21. In Android Studio set the Gradle JDK to 21, or from the command line:

```
./gradlew assembleDebug
```

The APK is written to `app/build/outputs/apk/debug/app-debug.apk`. More detail is in `SETUP.md`.

## Team

| Name | Student ID |
|------|------------|
| Ali Yahya Mohamed Gaber | 2023170374 |
| Mohamed Omar Ahmed Mokhtar Harash | 2023170524 |
| Baraa Abdullah Fathy Othman | 2023170135 |
| Youssef Samy Hanna Shafiq | 2022170517 |
| Mohamed Hassan Mohamed Abdeltawab | 2022170363 |
| Mostafa Walid Metwally Mohamed | 2023170616 |

The division of work is described in `TEAM_CONTRIBUTIONS.md`, and the two abstracts are in `ABSTRACTS.md`.
