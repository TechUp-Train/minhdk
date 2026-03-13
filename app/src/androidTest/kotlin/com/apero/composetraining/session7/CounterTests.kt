package com.apero.composetraining.session7

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.session7.demos.TestableCounter
import org.junit.Rule
import org.junit.Test

/**
 * ⭐ BÀI TẬP: Viết 5 UI tests cho Counter
 *
 * Đã có sẵn test 1 làm ví dụ.
 * Hoàn thành 4 tests còn lại.
 */
class CounterTests {

    @get:Rule
    val composeTestRule = createComposeRule()

    private fun setupCounter() {
        composeTestRule.setContent {
            AppTheme { TestableCounter() }
        }
    }

    // Test 1: Initial state = 0 ✅ (ví dụ)
    @Test
    fun counter_initialState_isZero() {
        setupCounter()
        composeTestRule
            .onNodeWithTag("count_text")
            .assertTextEquals("0")
    }

    // TODO: [Session 7] Bài tập 1 - Test 2: Click "+" → count = 1
    @Test
    fun counter_clickIncrement_countIsOne() {
        setupCounter()
        // TODO: Viết test ở đây
        // composeTestRule.onNodeWithTag("increment_button").performClick()
        // composeTestRule.onNodeWithTag("count_text").assertTextEquals("1")
    }

    // TODO: [Session 7] Bài tập 1 - Test 3: Click "-" from 0 → count vẫn = 0
    @Test
    fun counter_clickDecrementFromZero_countStaysZero() {
        setupCounter()
        // TODO: Viết test ở đây
    }

    // TODO: [Session 7] Bài tập 1 - Test 4: Click "+" 3 lần → count = 3
    @Test
    fun counter_clickIncrementThreeTimes_countIsThree() {
        setupCounter()
        // TODO: Viết test ở đây
    }

    // TODO: [Session 7] Bài tập 1 - Test 5: Click "Reset" → count = 0
    @Test
    fun counter_clickReset_countIsZero() {
        setupCounter()
        // TODO: Viết test ở đây
    }
}
