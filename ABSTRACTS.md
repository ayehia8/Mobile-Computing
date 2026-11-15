# Scores App — Two Abstracts (ACM Format)

> Replace the author placeholders with the six team members' names before submission.

---

## Abstract 1 — System Design and Persistence

**Title:** Scores: A Compose-First Android Application for Football Results and Fixtures

**Authors:** [Student Name 1], [Student Name 2], [Student Name 3], [Student Name 4], [Student Name 5], [Student Name 6]

**Abstract**

Scores is an Android application that lets football supporters browse completed matches and upcoming fixtures in a single, lightweight interface. The application is written in Kotlin and uses Jetpack Compose for the entire user interface. It follows a layered MVVM design in which a repository mediates between the Compose screens and two local persistence mechanisms: Room and DataStore. Room stores teams and matches in two related tables, enforces referential integrity with foreign keys, and exposes reactive queries through Kotlin Flow, so the list updates automatically after every insert, update, or delete. A repository observes a time ticker and re-queries the database every minute, which moves a fixture from the upcoming list to the results list as its kickoff time passes without requiring an application restart. DataStore keeps user preferences—dark mode and a favorites-only filter—across process death, and preference changes are combined with the Room streams in the view model. Selecting a match launches a dedicated detail screen through an explicit Android Intent that carries the match identifier, demonstrating navigation between activities in a Compose application. The result is a small but complete application that exercises the core Android building blocks required by the course: Compose, Intents, Room, DataStore, coroutines, and Flow. Manual verification on an emulator confirmed that seeded data appears on first launch and that favorite, delete, and preference changes are reflected immediately and persist across restarts.

**CCS Concepts:** • Information systems → Data management systems; • Human-centered computing → Ubiquitous and mobile computing; • Software and its engineering → Software architectures.

**Keywords:** Android, Jetpack Compose, Room, DataStore, Kotlin Flow, MVVM

---

## Abstract 2 — User Experience and Interaction

**Title:** Designing a Responsive and Persistent Match-Browsing Experience on Android

**Authors:** [Student Name 1], [Student Name 2], [Student Name 3], [Student Name 4], [Student Name 5], [Student Name 6]

**Abstract**

This paper describes the user-facing design of Scores, an Android application for following football results and fixtures. The interface is built entirely with Jetpack Compose and Material 3. The main screen separates finished and upcoming matches into two tabs, and each match is presented as a card containing the two team names, the current score or a "vs" placeholder, the kickoff date and time, a favorite toggle, and a delete action. A responsive layout adapts to the available width: phones render a single-column list, while screens at least 600 dp wide render a two-column grid, so the same content remains readable on tablets and foldables without duplicating UI code. Tapping a card opens a detail activity through an explicit Intent; the activity reads the match identifier from its extras, observes the corresponding Room row, and presents the teams, score, kickoff time, and match status on a width-constrained, scrollable surface. Two preferences—dark mode and a favorites-only filter—are exposed directly in the top app bar and are persisted with DataStore, allowing the interface to be restored exactly as the user left it. While the app is open, a repository-level clock keeps the two lists current, so a match moves from upcoming to results when its kickoff time passes. The implementation favors short composables, immutable state, lambda-based event callbacks, and `val` over `var`, which keeps the UI predictable and easy to review.

**CCS Concepts:** • Human-centered computing → Mobile devices; • Human-centered computing → Interaction design; • Software and its engineering → Software usability.

**Keywords:** Android, Jetpack Compose, responsive layout, Material 3, user experience, DataStore
