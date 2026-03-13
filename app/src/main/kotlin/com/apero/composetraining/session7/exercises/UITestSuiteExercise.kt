package com.apero.composetraining.session7.exercises

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 7: Complete UI Test Suite — Semantic Testing
 *
 * Mô tả: File này chứa CẢ composables VÀ documentation về cách test chúng
 *
 * Key concepts:
 * - testTag: identifier cho test (onNodeWithTag)
 * - contentDescription: accessibility label, dùng được trong test (onNodeWithContentDescription)
 * - semantics { heading() }: đánh dấu semantic role
 * - onNodeWithTag vs onNodeWithText vs onNodeWithContentDescription
 * - assertIsEnabled, assertIsNotEnabled, performTextInput, performClick
 *
 * ⚠️ Lưu ý: Actual @Test functions cần setup androidTest source set
 *    File này focus vào cách viết composables testable + document test cases
 */

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 1: TEST TAGS (Constants)
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * Test Tags — constants cho test identifiers
 *
 * Tại sao dùng constants thay vì hardcode strings?
 * 1. Avoid typos: "emial_field" vs "email_field" → compile error vs runtime fail
 * 2. Refactor dễ: đổi 1 chỗ, cập nhật cả composable và tests
 * 3. Autocomplete: IDE suggest tags available
 */
object LoginTestTags {
    const val EMAIL_FIELD = "email_field"
    const val PASSWORD_FIELD = "password_field"
    const val LOGIN_BUTTON = "login_button"
    const val ERROR_TEXT = "error_text"
    const val LOADING_INDICATOR = "loading_indicator"
    const val PASSWORD_TOGGLE = "password_toggle"
    const val FORM_TITLE = "form_title"
}

object CounterTestTags {
    const val COUNTER_TEXT = "counter_text"
    const val INCREMENT_BTN = "increment_btn"
    const val DECREMENT_BTN = "decrement_btn"
}

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 2: DATA CLASSES
// ═══════════════════════════════════════════════════════════════════════════════

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

val LoginFormState.isValid: Boolean
    get() = email.isNotBlank() && email.contains("@") && password.length >= 6

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 3: TESTABLE COMPOSABLES
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * LoginForm — form với đầy đủ semantics cho testing
 *
 * Semantics trong Compose:
 * - testTag: ID cho UI tests (KHÔNG hiện trên screen)
 * - contentDescription: accessibility label (screen reader đọc)
 * - semantics { heading() }: đánh dấu là heading (cho accessibility tree)
 *
 * Khi nào dùng cái nào?
 * - testTag: khi cần test automation nhưng không có text/description rõ ràng
 * - contentDescription: cho icons, images (accessibility BẮT BUỘC)
 * - semantics { }: cho custom accessibility properties
 */
@Composable
fun LoginForm(
    state: LoginFormState,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement LoginForm với đầy đủ testTag và semantics
    // 1. var passwordVisible by remember { mutableStateOf(false) }
    //
    // 2. Column(fillMaxWidth, padding=16.dp, spacedBy=16.dp):
    //
    //    // Form title với semantics { heading() }
    //    Text(
    //        text = "Login to your account",
    //        modifier = Modifier
    //            .testTag(LoginTestTags.FORM_TITLE)
    //            .semantics { heading() }  // Đánh dấu là heading cho accessibility
    //    )
    //
    //    // Email field
    //    OutlinedTextField(
    //        value = state.email,
    //        onValueChange = onEmailChange,
    //        label = { Text("Email") },
    //        leadingIcon = { Icon(Email, contentDescription = "Email icon") },  // contentDescription cho accessibility
    //        isError = state.errorMessage != null,
    //        enabled = !state.isLoading,
    //        modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.EMAIL_FIELD)  // testTag cho automation
    //    )
    //
    //    // Password field với toggle visibility
    //    OutlinedTextField(
    //        value = state.password,
    //        ...
    //        trailingIcon = {
    //            IconButton(
    //                onClick = { passwordVisible = !passwordVisible },
    //                modifier = Modifier.testTag(LoginTestTags.PASSWORD_TOGGLE)
    //            ) {
    //                Icon(
    //                    if (passwordVisible) VisibilityOff else Visibility,
    //                    contentDescription = if (passwordVisible) "Hide password" else "Show password"
    //                )
    //            }
    //        },
    //        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
    //        modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.PASSWORD_FIELD)
    //    )
    //
    //    // Error text (chỉ hiện khi có error)
    //    if (state.errorMessage != null) {
    //        Text(
    //            text = state.errorMessage,
    //            color = error,
    //            modifier = Modifier.testTag(LoginTestTags.ERROR_TEXT)
    //        )
    //    }
    //
    //    // Login button
    //    Button(
    //        onClick = onLoginClick,
    //        enabled = state.isValid && !state.isLoading,
    //        modifier = Modifier.fillMaxWidth().testTag(LoginTestTags.LOGIN_BUTTON)
    //    ) {
    //        if (state.isLoading) {
    //            CircularProgressIndicator(
    //                modifier = Modifier.size(20.dp).testTag(LoginTestTags.LOADING_INDICATOR)
    //            )
    //            Spacer(8.dp)
    //        }
    //        Text(if (state.isLoading) "Logging in..." else "Login")
    //    }
    Box {}
}

/**
 * CounterWidget — simple counter với testTags
 */
@Composable
fun CounterWidget(
    count: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement CounterWidget
    // - Row(fillMaxWidth, Center, CenterVertically)
    //
    // - FilledTonalIconButton(
    //       onClick = onDecrement,
    //       enabled = count > 0,
    //       modifier = Modifier.testTag(CounterTestTags.DECREMENT_BTN)
    //   ) { Icon(Remove, contentDescription = "Decrease count") }
    //
    // - Text(
    //       text = count.toString(),
    //       modifier = Modifier.padding(horizontal=32.dp).testTag(CounterTestTags.COUNTER_TEXT)
    //   )
    //
    // - FilledTonalIconButton(
    //       onClick = onIncrement,
    //       modifier = Modifier.testTag(CounterTestTags.INCREMENT_BTN)
    //   ) { Icon(Add, contentDescription = "Increase count") }
    Box {}
}

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 4: TEST CASES DOCUMENTATION (10 test cases)
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * 📚 TEST CASES DOCUMENTATION
 *
 * Setup cần thiết (app/build.gradle.kts):
 * ```kotlin
 * androidTestImplementation("androidx.compose.ui:ui-test-junit4")
 * debugImplementation("androidx.compose.ui:ui-test-manifest")
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 * TEST 1: Initial state — button disabled
 * ```kotlin
 * @Test fun loginButton_initialState_isDisabled() {
 *     composeTestRule.setContent { LoginForm(state = LoginFormState(), ...) }
 *     composeTestRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON).assertIsNotEnabled()
 * }
 * ```
 *
 * TEST 2: Valid input — button enabled
 * ```kotlin
 * @Test fun loginButton_validInput_isEnabled() {
 *     composeTestRule.setContent {
 *         LoginForm(state = LoginFormState(email = "test@example.com", password = "password123"), ...)
 *     }
 *     composeTestRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON).assertIsEnabled()
 * }
 * ```
 *
 * TEST 3-4: Empty email / Short password — button disabled
 *
 * TEST 5: Loading state — shows indicator
 * ```kotlin
 * @Test fun loginForm_loadingState_showsIndicator() {
 *     composeTestRule.setContent {
 *         LoginForm(state = LoginFormState(isLoading = true, email = "...", password = "..."), ...)
 *     }
 *     composeTestRule.onNodeWithTag(LoginTestTags.LOADING_INDICATOR).assertExists()
 *     composeTestRule.onNodeWithTag(LoginTestTags.LOGIN_BUTTON).assertIsNotEnabled()
 * }
 * ```
 *
 * TEST 6: Error state — shows error message
 * ```kotlin
 * @Test fun loginForm_errorState_showsErrorMessage() {
 *     composeTestRule.setContent {
 *         LoginForm(state = LoginFormState(errorMessage = "Invalid credentials"), ...)
 *     }
 *     composeTestRule.onNodeWithTag(LoginTestTags.ERROR_TEXT).assertExists()
 *     composeTestRule.onNodeWithText("Invalid credentials").assertExists()
 * }
 * ```
 *
 * TEST 7: Text input — performTextInput
 * TEST 8: Find by contentDescription
 * TEST 9: Counter increment — performClick
 * TEST 10: Counter decrement boundary — assertIsNotEnabled at 0
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * 📌 SELECTOR COMPARISON
 * | Selector                      | Khi nào dùng                          |
 * |-------------------------------|---------------------------------------|
 * | onNodeWithTag("tag")          | Test automation, không có text rõ     |
 * | onNodeWithText("text")        | Tìm button, label có text cụ thể      |
 * | onNodeWithContentDescription  | Tìm icon, image (accessibility)       |
 * ═══════════════════════════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 5: DEMO SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun UITestSuiteScreen(modifier: Modifier = Modifier) {
    // TODO: Implement UITestSuiteScreen
    // 1. var loginState by remember { mutableStateOf(LoginFormState()) }
    //    var counter by remember { mutableIntStateOf(0) }
    //
    // 2. Column(fillMaxSize, verticalScroll, padding=16.dp, spacedBy=24.dp):
    //    - Header: Text "UI Test Suite Demo" + subtitle
    //    - HorizontalDivider
    //
    //    - Text "Login Form" + Card { LoginForm(...) }
    //    - Row quick state buttons: "Show Error", "Show Loading", "Reset"
    //
    //    - HorizontalDivider
    //    - Text "Counter Widget" + Card { CounterWidget(...) }
    //
    //    - Card info về test documentation
    Box {}
}

// ═══════════════════════════════════════════════════════════════════════════════
// PREVIEWS
// ═══════════════════════════════════════════════════════════════════════════════

@Preview(showBackground = true, name = "UI Test Suite - Light")
@Composable
private fun UITestSuitePreview() {
    AppTheme {
        UITestSuiteScreen()
    }
}

@Preview(
    showBackground = true,
    name = "UI Test Suite - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun UITestSuiteDarkPreview() {
    AppTheme(darkTheme = true) {
        UITestSuiteScreen()
    }
}

@Preview(showBackground = true, name = "Login Form Preview")
@Composable
private fun LoginFormPreview() {
    AppTheme {
        LoginForm(
            state = LoginFormState(email = "test@example.com", password = "password"),
            onEmailChange = {},
            onPasswordChange = {},
            onLoginClick = {},
        )
    }
}

@Preview(showBackground = true, name = "Counter Widget Preview")
@Composable
private fun CounterWidgetPreview() {
    AppTheme {
        CounterWidget(
            count = 5,
            onIncrement = {},
            onDecrement = {},
            modifier = Modifier.padding(16.dp),
        )
    }
}
