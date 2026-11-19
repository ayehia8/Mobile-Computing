# Scores App — Two Abstracts

## Abstract 1 — System Design and Persistence

**Title:** Scores: A Compose-Based Android Application for Football Results and Fixtures

**Authors:** Ali Yahya Mohamed Gaber (2023170374), Mohamed Omar Ahmed Mokhtar Harash (2023170524), Baraa Abdullah Fathy Othman (2023170135), Youssef Samy Hanna Shafiq (2022170517), Mohamed Hassan Mohamed Abdeltawab (2022170363), Mostafa Walid Metwally Mohamed (2023170616)

**Abstract**

Scores is an Android application for following football results and fixtures. It is written in Kotlin and the whole interface is built with Jetpack Compose. The code follows an MVVM structure: the screens observe a view model, the view model reads from a repository, and the repository talks to two local storage components, Room and DataStore. Room keeps teams and matches in two related tables and enforces the link between them with foreign keys. Every Room query returns a Kotlin Flow, so the lists on screen refresh by themselves after each insert, update or delete. The repository also runs a timer that re-queries the database once a minute. This is what moves a fixture from the upcoming list to the results list when its kickoff time passes, without the user restarting the app. DataStore stores the two user settings, dark mode and the favourites-only filter, and both survive process death. Tapping a match opens a details screen through an explicit Intent that carries the match id. The project therefore covers the main Android components of the course: Compose, Intents, Room, DataStore, coroutines and Flow.

**CCS Concepts:** • Information systems → Data management systems; • Human-centered computing → Ubiquitous and mobile computing; • Software and its engineering → Software architectures.

**Keywords:** Android, Jetpack Compose, Room, DataStore, Kotlin Flow, MVVM

---

## Abstract 2 — User Experience and Interaction

**Title:** Designing a Responsive Match-Browsing Screen on Android

**Authors:** Ali Yahya Mohamed Gaber (2023170374), Mohamed Omar Ahmed Mokhtar Harash (2023170524), Baraa Abdullah Fathy Othman (2023170135), Youssef Samy Hanna Shafiq (2022170517), Mohamed Hassan Mohamed Abdeltawab (2022170363), Mostafa Walid Metwally Mohamed (2023170616)

**Abstract**

The second half of the project is the user interface. The app is built with Jetpack Compose and Material 3, and the main screen has two tabs, one for finished matches and one for upcoming fixtures. Each match appears as a card that shows the two team names, the score or the word "vs" when the match has not been played yet, the kickoff date and time, a favourite button and a delete button. The layout adapts to the width of the screen: phones get a single column and any screen at least 600 dp wide gets a two-column grid. Both cases reuse the same card composable, so there is no duplicated UI code. Tapping a card starts MatchDetailsActivity with an explicit Intent. The activity reads the match id from the intent extras and observes that row in Room, so the details screen shows the same data as the list and stays current. Dark mode and the favourites-only filter sit in the top app bar and are saved in DataStore, which means the app returns in the same state after it is closed. Short composables, immutable state, callbacks passed as lambdas and val instead of var were used throughout to keep the UI easy to read.

**CCS Concepts:** • Human-centered computing → Mobile devices; • Human-centered computing → Interaction design; • Software and its engineering → Software usability.

**Keywords:** Android, Jetpack Compose, responsive layout, Material 3, user experience, DataStore
