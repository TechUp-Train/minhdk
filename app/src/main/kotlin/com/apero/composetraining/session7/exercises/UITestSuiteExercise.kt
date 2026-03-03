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
 * File này đặc biệt vì nó là tài liệu giáo dục:
 * - Phần 1: Composables với đầy đủ testTag, contentDescription, semantics
 * - Phần 2: Documentation về 10 test cases với expected behavior
 * - Phần 3: Hướng dẫn setup androidTest
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
// PHẦN 1: TESTABLE COMPOSABLES
// ═══════════════════════════════════════════════════════════════════════════════

// ─── Test Tags (Constants) ────────────────────────────────────────────────────

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

// ─── Login Form State ─────────────────────────────────────────────────────────

data class LoginFormState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

val LoginFormState.isValid: Boolean
    get() = email.isNotBlank() && email.contains("@") && password.length >= 6

// ─── Login Form Composable ────────────────────────────────────────────────────

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
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        // Form title với semantics { heading() }
        // heading(): đánh dấu element này là heading trong accessibility tree
        // Screen reader sẽ announce "heading, Login to your account"
        Text(
            text = "Login to your account",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .testTag(LoginTestTags.FORM_TITLE)
                .semantics { heading() },
        )

        // Email field
        OutlinedTextField(
            value = state.email,
            onValueChange = onEmailChange,
            label = { Text("Email") },
            leadingIcon = {
                // contentDescription cho icon
                // Test có thể dùng: onNodeWithContentDescription("Email icon")
                Icon(
                    Icons.Default.Email,
                    contentDescription = "Email icon",
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            singleLine = true,
            isError = state.errorMessage != null,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LoginTestTags.EMAIL_FIELD), // testTag cho automation
        )

        // Password field
        OutlinedTextField(
            value = state.password,
            onValueChange = onPasswordChange,
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    Icons.Default.Lock,
                    contentDescription = "Password icon",
                )
            },
            trailingIcon = {
                // Toggle password visibility
                IconButton(
                    onClick = { passwordVisible = !passwordVisible },
                    modifier = Modifier.testTag(LoginTestTags.PASSWORD_TOGGLE),
                ) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff
                        else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Hide password"
                        else "Show password",
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            singleLine = true,
            isError = state.errorMessage != null,
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LoginTestTags.PASSWORD_FIELD),
        )

        // Error text (chỉ hiện khi có error)
        // Test: onNodeWithTag("error_text").assertExists() hoặc assertDoesNotExist()
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.testTag(LoginTestTags.ERROR_TEXT),
            )
        }

        // Login button
        Button(
            onClick = onLoginClick,
            enabled = state.isValid && !state.isLoading, // Disabled khi invalid hoặc loading
            modifier = Modifier
                .fillMaxWidth()
                .testTag(LoginTestTags.LOGIN_BUTTON),
        ) {
            if (state.isLoading) {
                // Loading indicator bên trong button
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(20.dp)
                        .testTag(LoginTestTags.LOADING_INDICATOR),
                    strokeWidth = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(if (state.isLoading) "Logging in..." else "Login")
        }
    }
}

// ─── Counter Widget ───────────────────────────────────────────────────────────

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
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Decrement button
        FilledTonalIconButton(
            onClick = onDecrement,
            enabled = count > 0,
            modifier = Modifier.testTag(CounterTestTags.DECREMENT_BTN),
        ) {
            Icon(
                Icons.Default.Remove,
                contentDescription = "Decrease count", // Accessibility
            )
        }

        // Counter text
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.displayMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(horizontal = 32.dp)
                .testTag(CounterTestTags.COUNTER_TEXT),
        )

        // Increment button
        FilledTonalIconButton(
            onClick = onIncrement,
            modifier = Modifier.testTag(CounterTestTags.INCREMENT_BTN),
        ) {
            Icon(
                Icons.Default.Add,
                contentDescription = "Increase count",
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 2: TEST CASES DOCUMENTATION
// ═══════════════════════════════════════════════════════════════════════════════

/**
 * 📚 TEST CASES DOCUMENTATION
 *
 * File này document 10 test cases cho LoginForm và CounterWidget.
 * Trong production, các tests này sẽ nằm trong androidTest source set.
 *
 * Setup cần thiết (app/build.gradle.kts):
 * ```kotlin
 * androidTestImplementation("androidx.compose.ui:ui-test-junit4")
 * debugImplementation("androidx.compose.ui:ui-test-manifest")
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 1: Initial state — button disabled
 * ────────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginButton_initialState_isDisabled() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(), // Empty state
 *             onEmailChange = {},
 *             onPasswordChange = {},
 *             onLoginClick = {},
 *         )
 *     }
 *
 *     // onNodeWithTag: tìm node bằng testTag
 *     // assertIsNotEnabled: verify button disabled
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOGIN_BUTTON)
 *         .assertIsNotEnabled()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 2: Valid input — button enabled
 * ─────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginButton_validInput_isEnabled() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(
 *                 email = "test@example.com",
 *                 password = "password123", // >= 6 chars
 *             ),
 *             onEmailChange = {},
 *             onPasswordChange = {},
 *             onLoginClick = {},
 *         )
 *     }
 *
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOGIN_BUTTON)
 *         .assertIsEnabled() // Button should be enabled
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 3: Empty email — button disabled
 * ─────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginButton_emptyEmail_isDisabled() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(
 *                 email = "", // Empty
 *                 password = "password123",
 *             ),
 *             // ...
 *         )
 *     }
 *
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOGIN_BUTTON)
 *         .assertIsNotEnabled()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 4: Short password — button disabled
 * ────────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginButton_shortPassword_isDisabled() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(
 *                 email = "test@example.com",
 *                 password = "12345", // < 6 chars
 *             ),
 *             // ...
 *         )
 *     }
 *
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOGIN_BUTTON)
 *         .assertIsNotEnabled()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 5: Loading state — shows indicator
 * ───────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginForm_loadingState_showsIndicator() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(
 *                 email = "test@example.com",
 *                 password = "password123",
 *                 isLoading = true, // Loading
 *             ),
 *             // ...
 *         )
 *     }
 *
 *     // Loading indicator should exist
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOADING_INDICATOR)
 *         .assertExists()
 *
 *     // Button should be disabled during loading
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.LOGIN_BUTTON)
 *         .assertIsNotEnabled()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 6: Error state — shows error message
 * ─────────────────────────────────────────
 * ```kotlin
 * @Test
 * fun loginForm_errorState_showsErrorMessage() {
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(
 *                 errorMessage = "Invalid credentials",
 *             ),
 *             // ...
 *         )
 *     }
 *
 *     // Error text should be visible
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.ERROR_TEXT)
 *         .assertExists()
 *         .assertTextEquals("Invalid credentials")
 *
 *     // Alternative: find by text content
 *     composeTestRule
 *         .onNodeWithText("Invalid credentials")
 *         .assertExists()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 7: Text input — performTextInput
 * ─────────────────────────────────────
 * ```kotlin
 * @Test
 * fun emailField_performTextInput_updatesValue() {
 *     var capturedEmail = ""
 *
 *     composeTestRule.setContent {
 *         LoginForm(
 *             state = LoginFormState(),
 *             onEmailChange = { capturedEmail = it },
 *             onPasswordChange = {},
 *             onLoginClick = {},
 *         )
 *     }
 *
 *     // Perform text input
 *     composeTestRule
 *         .onNodeWithTag(LoginTestTags.EMAIL_FIELD)
 *         .performTextInput("user@test.com")
 *
 *     // Verify callback received the input
 *     assertEquals("user@test.com", capturedEmail)
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 8: Find by contentDescription
 * ──────────────────────────────────
 * ```kotlin
 * @Test
 * fun emailIcon_hasCorrectContentDescription() {
 *     composeTestRule.setContent {
 *         LoginForm(state = LoginFormState(), ...)
 *     }
 *
 *     // onNodeWithContentDescription: tìm node bằng accessibility label
 *     // Dùng cho icons, images không có text
 *     composeTestRule
 *         .onNodeWithContentDescription("Email icon")
 *         .assertExists()
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 9: Counter increment
 * ────────────────────────
 * ```kotlin
 * @Test
 * fun counterWidget_clickIncrement_increasesCount() {
 *     var count = 0
 *
 *     composeTestRule.setContent {
 *         CounterWidget(
 *             count = count,
 *             onIncrement = { count++ },
 *             onDecrement = { count-- },
 *         )
 *     }
 *
 *     // Click increment 3 times
 *     repeat(3) {
 *         composeTestRule
 *             .onNodeWithTag(CounterTestTags.INCREMENT_BTN)
 *             .performClick()
 *     }
 *
 *     assertEquals(3, count)
 * }
 * ```
 *
 * ─────────────────────────────────────────────────────────────────────────────
 *
 * TEST 10: Counter decrement boundary
 * ──────────────────────────────────
 * ```kotlin
 * @Test
 * fun counterWidget_atZero_decrementDisabled() {
 *     composeTestRule.setContent {
 *         CounterWidget(
 *             count = 0,
 *             onIncrement = {},
 *             onDecrement = {},
 *         )
 *     }
 *
 *     // At count = 0, decrement should be disabled
 *     composeTestRule
 *         .onNodeWithTag(CounterTestTags.DECREMENT_BTN)
 *         .assertIsNotEnabled()
 *
 *     // Increment should still be enabled
 *     composeTestRule
 *         .onNodeWithTag(CounterTestTags.INCREMENT_BTN)
 *         .assertIsEnabled()
 * }
 * ```
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * 📌 SELECTOR COMPARISON
 *
 * | Selector                      | Khi nào dùng                          |
 * |-------------------------------|---------------------------------------|
 * | onNodeWithTag("tag")          | Test automation, không có text rõ     |
 * | onNodeWithText("text")        | Tìm button, label có text cụ thể      |
 * | onNodeWithContentDescription  | Tìm icon, image (accessibility)       |
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 */

// ═══════════════════════════════════════════════════════════════════════════════
// PHẦN 3: DEMO SCREEN
// ═══════════════════════════════════════════════════════════════════════════════

@Composable
fun UITestSuiteScreen(modifier: Modifier = Modifier) {
    var loginState by remember { mutableStateOf(LoginFormState()) }
    var counter by remember { mutableIntStateOf(0) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        // Header
        Text(
            text = "UI Test Suite Demo",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Composables với testTag, contentDescription, semantics",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        HorizontalDivider()

        // Login Form Section
        Text("Login Form", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        Card {
            LoginForm(
                state = loginState,
                onEmailChange = { loginState = loginState.copy(email = it, errorMessage = null) },
                onPasswordChange = { loginState = loginState.copy(password = it, errorMessage = null) },
                onLoginClick = {
                    loginState = loginState.copy(isLoading = true)
                    // Simulate login (in real app, call API)
                },
            )
        }

        // Quick state buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(
                onClick = {
                    loginState = LoginFormState(
                        errorMessage = "Invalid credentials",
                    )
                },
            ) {
                Text("Show Error")
            }
            OutlinedButton(
                onClick = {
                    loginState = LoginFormState(
                        email = "test@example.com",
                        password = "password123",
                        isLoading = true,
                    )
                },
            ) {
                Text("Show Loading")
            }
            OutlinedButton(onClick = { loginState = LoginFormState() }) {
                Text("Reset")
            }
        }

        HorizontalDivider()

        // Counter Section
        Text("Counter Widget", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        Card(modifier = Modifier.fillMaxWidth()) {
            CounterWidget(
                count = counter,
                onIncrement = { counter++ },
                onDecrement = { if (counter > 0) counter-- },
                modifier = Modifier.padding(16.dp),
            )
        }

        // Test info card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            ),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "📚 Test Documentation",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Xem source code file này để xem:\n" +
                        "• 10 test cases với code examples\n" +
                        "• Cách dùng onNodeWithTag, onNodeWithText, onNodeWithContentDescription\n" +
                        "• assertIsEnabled, performTextInput, performClick",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                )
            }
        }
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

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
            state = LoginFormState(
                email = "test@example.com",
                password = "password",
            ),
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
