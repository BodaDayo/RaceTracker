package com.rgbstudios.racetracker

import com.rgbstudios.racetracker.ui.RaceParticipant
import com.rgbstudios.racetracker.ui.progressFactor
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RaceParticipantTest {
    private val raceParticipant = RaceParticipant(
        name = "Test"
    )

    /**
     * Happy path
     */
    @Test
    fun raceParticipant_RaceStarted_ProgressUpdated() = runTest {
        val expectedProgress = 1
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }


    @Test
    fun raceParticipant_RacePaused_ProgressUpdated() = runTest {
        val expectedProgress = 5
        val racerJob = launch { raceParticipant.run() }
        advanceTimeBy(expectedProgress * raceParticipant.progressDelayMillis)
        runCurrent()
        racerJob.cancelAndJoin()
        assertEquals(expectedProgress, raceParticipant.currentProgress)
    }

    @Test
    fun raceParticipant_RacePausedAndResumed_ProgressUpdated() = runTest {
        val expectedProgress = 5

        repeat(2) {
            val racerJob = launch { raceParticipant.run() }
            advanceTimeBy(expectedProgress * raceParticipant.progressDelayMillis)
            runCurrent()
            racerJob.cancelAndJoin()
        }

        assertEquals(expectedProgress * 2, raceParticipant.currentProgress)
    }

    @Test
    fun testReset_resetsProgressToZero() {
        val participant = RaceParticipant(name = "Test", maxProgress = 100, initialProgress = 50)
        participant.reset()
        assertEquals(0, participant.currentProgress) // After reset, the progress should be 0
    }

    @Test
    fun testProgressFactorCalculation() {
        val participant = RaceParticipant(name = "Test", maxProgress = 100, initialProgress = 25)
        val expectedProgressFactor = 0.25f
        assertEquals(expectedProgressFactor, participant.progressFactor, 0.01f)
    }

    /**
     * Error case
     */
    @Test(expected = IllegalArgumentException::class)
    fun raceParticipant_ProgressIncrementZero_ExceptionThrown() = runTest {
        RaceParticipant(name = "Progress Test", progressIncrement = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun raceParticipant_MaxProgressZero_ExceptionThrown() {
        RaceParticipant(name = "Progress Test", maxProgress = 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeMaxProgressThrowsException() {
        RaceParticipant(name = "Test", maxProgress = -10, progressIncrement = 1)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testNegativeProgressIncrementThrowsException() {
        RaceParticipant(name = "Test", maxProgress = 100, progressIncrement = -5)
    }

    @Test
    fun testInitialProgressGreaterThanMaxProgress() {
        val participant = RaceParticipant(name = "Test", maxProgress = 100, initialProgress = 150)
        assertEquals(150, participant.currentProgress)
        runBlocking {
            participant.run()
        }
        assertEquals(150, participant.currentProgress)
    }

    /**
     * Boundary cases
     */
    @Test
    fun raceParticipant_RaceFinished_ProgressUpdated() = runTest {
        launch { raceParticipant.run() }
        advanceTimeBy(raceParticipant.maxProgress * raceParticipant.progressDelayMillis)
        runCurrent()
        assertEquals(100, raceParticipant.currentProgress)
    }

    @Test
    fun testRun_maxProgressOne() = runTest {
        val participant = RaceParticipant(name = "Test", maxProgress = 1, progressIncrement = 1, progressDelayMillis = 0L)
        participant.run()
        assertEquals(1, participant.currentProgress)
    }

    @Test
    fun testRun_initialProgressEqualsMaxProgress() = runTest {
        val participant = RaceParticipant(name = "Test", maxProgress = 100, initialProgress = 100, progressIncrement = 1, progressDelayMillis = 0L)
        participant.run()
        assertEquals(100, participant.currentProgress)
    }

    @Test
    fun testRun_progressStopsAtMaxProgress() = runTest {
        val participant = RaceParticipant(name = "Test", maxProgress = 10, progressIncrement = 3, progressDelayMillis = 0L)
        participant.run()
        assertEquals(10, participant.currentProgress) // Now the progress stops exactly at maxProgress
    }

    @Test
    fun testRun_largeProgressIncrement() = runTest {
        val participant = RaceParticipant(name = "Test", maxProgress = 100, progressIncrement = 150, progressDelayMillis = 0L)
        participant.run()
        assertEquals(100, participant.currentProgress)
    }

    @Test
    fun testRun_maxProgressEdgeCase() = runTest {
        val participant = RaceParticipant(name = "Test", maxProgress = Int.MAX_VALUE, progressIncrement = Int.MAX_VALUE, progressDelayMillis = 0L)
        participant.run()
        assertEquals(Int.MAX_VALUE, participant.currentProgress)
    }



}