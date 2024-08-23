# RaceTrackerApp

RaceTrackerApp is a simple Android application developed as part of an Android Basics Compose course. This app demonstrates the use of Kotlin Coroutines to handle long-running operations in a structured and efficient way. The project focuses on teaching key coroutine concepts while implementing a fun race-tracking simulation.

## Features

- Simulates a race where participants progress at different speeds.
- Uses Jetpack Compose for UI development.
- Demonstrates key concepts of Kotlin coroutines, including sequential execution, coroutine builders, dispatchers, jobs, and scopes.
- **Spiced up with Lottie Animation**: Added a Lottie animation that simulates a race runner, which starts when the user clicks "Start" and stops when they click "Pause". This adds a dynamic visual element to the race-tracking experience.

## How the App Works

In the RaceTrackerApp, two participants race to complete a defined distance, progressing at different speeds. The app simulates this using coroutines to increment each participant’s progress over time. The coroutines run concurrently, mimicking real-time progress while adhering to the principles of structured concurrency.

The app uses Jetpack Compose to visualize each participant’s progress in a linear progress bar, demonstrating how coroutines work seamlessly with UI components.

### **Added Custom Feature: Lottie Animation**

The app includes a Lottie animation to simulate a race runner. The animation runs while the race is in progress and pauses when the race is paused. This visual addition adds an engaging, dynamic element to the app.

## Technology Stack

- **Kotlin**
- **Jetpack Compose**
- **Kotlin Coroutines**
- **Lottie**
- **Android Studio**

## Getting Started

To run this project locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/BodaDayo/racetrackapp.git
