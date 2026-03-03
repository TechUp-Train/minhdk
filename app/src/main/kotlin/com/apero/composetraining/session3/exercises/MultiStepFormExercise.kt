package com.apero.composetraining.session3.exercises

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.apero.composetraining.common.AppTheme

/**
 * ⭐⭐⭐⭐⭐ BÀI TẬP NÂNG CAO BUỔI 3: Multi-step Registration Form
 *
 * Mô tả: Form đăng ký nhiều bước với complex state management, validation, UDF pattern
 *
 * ┌──────────────────────────────────────┐
 * │ Step 1/4: Personal Info              │
 * │ ████████░░░░░░░░░░░░░░░░░░░░░░░░░░  │  ← LinearProgressIndicator
 * │                                      │
 * │ First Name *                         │
 * │ ┌────────────────────────────────┐   │
 * │ │ John                           │   │
 * │ └────────────────────────────────┘   │
 * │ Last Name *                          │
 * │ ┌────────────────────────────────┐   │
 * │ │ Doe                            │   │
 * │ └────────────────────────────────┘   │
 * │                                      │
 * │          [← Back]  [Next →]         │
 * └──────────────────────────────────────┘
 *
 * Steps: Personal Info → Contact → Preferences → Review
 *
 * Key concepts:
 * - @Stable annotation: đánh dấu class "ổn định" → Compose SKIP recompose nếu params không đổi
 * - UDF (Unidirectional Data Flow): State đi xuống, Events đi lên
 * - sealed class FormAction: type-safe events thay vì nhiều callbacks
 */

// ─── State & Actions (UDF pattern) ───────────────────────────────────────────

/**
 * @Stable annotation — tại sao cần?
 *
 * Mặc định, Compose không biết FormState có thay đổi hay không nếu
 * nó là data class thông thường. Compose sẽ recompose mỗi khi parent recompose.
 *
 * @Stable nói với Compose rằng:
 * 1. Nếu các properties không đổi (theo equals()), class được xem là "stable"
 * 2. Compose CÓ THỂ SKIP recompose nếu toàn bộ params không thay đổi
 *
 * Điều kiện để @Stable hiệu quả:
 * - equals() phải nhất quán (data class đã đảm bảo)
 * - Tất cả properties phải là stable types (String, Int, Boolean là stable)
 */
@Stable
data class FormState(
    // Step 1: Personal Info
    val firstName: String = "",
    val lastName: String = "",
    val birthYear: String = "",

    // Step 2: Contact
    val email: String = "",
    val phone: String = "",
    val city: String = "",

    // Step 3: Preferences
    val receiveNewsletter: Boolean = false,
    val receiveNotifications: Boolean = true,
    val preferredLanguage: String = "Vietnamese",

    // Navigation
    val currentStep: Int = 0, // 0-indexed: 0=Personal, 1=Contact, 2=Prefs, 3=Review

    // Validation errors — null = no error, non-null = error message
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val phoneError: String? = null,

    // Submission
    val isSubmitted: Boolean = false,
)

val FormState.totalSteps: Int get() = 4
val FormState.progress: Float get() = (currentStep + 1).toFloat() / totalSteps.toFloat()

val FormState.stepTitle: String get() = when (currentStep) {
    0 -> "Personal Info"
    1 -> "Contact Details"
    2 -> "Preferences"
    3 -> "Review & Submit"
    else -> ""
}

/**
 * sealed class FormAction — type-safe events đi từ UI lên ViewModel/Host
 *
 * Thay vì nhiều callbacks rời rạc:
 *   onFirstNameChange, onLastNameChange, onNext, onPrev, onSubmit...
 *
 * Dùng 1 callback duy nhất:
 *   onAction: (FormAction) -> Unit
 *
 * Lợi ích:
 * 1. API gọn hơn (1 callback thay vì N callbacks)
 * 2. Dễ log tất cả user actions
 * 3. Dễ test (chỉ cần verify action được gửi đúng)
 */
sealed class FormAction {
    // Step 1 actions
    data class UpdateFirstName(val value: String) : FormAction()
    data class UpdateLastName(val value: String) : FormAction()
    data class UpdateBirthYear(val value: String) : FormAction()

    // Step 2 actions
    data class UpdateEmail(val value: String) : FormAction()
    data class UpdatePhone(val value: String) : FormAction()
    data class UpdateCity(val value: String) : FormAction()

    // Step 3 actions
    data class UpdateNewsletter(val enabled: Boolean) : FormAction()
    data class UpdateNotifications(val enabled: Boolean) : FormAction()
    data class UpdateLanguage(val language: String) : FormAction()

    // Navigation actions
    data object NextStep : FormAction()
    data object PrevStep : FormAction()
    data object Submit : FormAction()
}

// ─── Business Logic (Reducer) ─────────────────────────────────────────────────

/**
 * Hàm reduce: nhận state hiện tại + action → trả về state mới
 *
 * Pattern này giúp logic dễ test và dễ trace:
 * - Input: (FormState, FormAction) → Output: FormState
 * - Pure function, không có side effects
 */
fun reduceFormState(state: FormState, action: FormAction): FormState {
    return when (action) {
        is FormAction.UpdateFirstName -> state.copy(
            firstName = action.value,
            firstNameError = null, // Clear error khi user sửa
        )
        is FormAction.UpdateLastName -> state.copy(
            lastName = action.value,
            lastNameError = null,
        )
        is FormAction.UpdateBirthYear -> state.copy(birthYear = action.value)
        is FormAction.UpdateEmail -> state.copy(
            email = action.value,
            emailError = null,
        )
        is FormAction.UpdatePhone -> state.copy(
            phone = action.value,
            phoneError = null,
        )
        is FormAction.UpdateCity -> state.copy(city = action.value)
        is FormAction.UpdateNewsletter -> state.copy(receiveNewsletter = action.enabled)
        is FormAction.UpdateNotifications -> state.copy(receiveNotifications = action.enabled)
        is FormAction.UpdateLanguage -> state.copy(preferredLanguage = action.language)

        is FormAction.NextStep -> {
            // Validate trước khi qua step tiếp theo
            val validated = validateCurrentStep(state)
            if (validated.hasCurrentStepErrors) {
                validated // Trả về state với errors
            } else {
                state.copy(currentStep = minOf(state.currentStep + 1, state.totalSteps - 1))
            }
        }

        is FormAction.PrevStep -> state.copy(
            currentStep = maxOf(state.currentStep - 1, 0),
        )

        is FormAction.Submit -> state.copy(isSubmitted = true)
    }
}

// Tách ra dùng function riêng
private fun validateCurrentStep(state: FormState): FormState {
    return when (state.currentStep) {
        0 -> state.copy(
            firstNameError = if (state.firstName.isBlank()) "First name is required" else null,
            lastNameError = if (state.lastName.isBlank()) "Last name is required" else null,
        )
        1 -> state.copy(
            emailError = when {
                state.email.isBlank() -> "Email is required"
                !state.email.contains("@") -> "Invalid email format"
                else -> null
            },
            phoneError = when {
                state.phone.isBlank() -> "Phone is required"
                state.phone.length < 9 -> "Phone too short"
                else -> null
            },
        )
        else -> state // Không cần validate step 2 và 3
    }
}

private val FormState.hasCurrentStepErrors: Boolean get() = when (currentStep) {
    0 -> firstNameError != null || lastNameError != null
    1 -> emailError != null || phoneError != null
    else -> false
}

// ─── Host Composable (Stateful) ───────────────────────────────────────────────

/**
 * MultiStepFormScreen — stateful host
 *
 * Host giữ state và cung cấp cho FormContent (stateless child)
 * Pattern: State hosting ở level cao nhất cần dùng state
 */
@Composable
fun MultiStepFormScreen(modifier: Modifier = Modifier) {
    // State cho toàn bộ form
    var formState by remember { mutableStateOf(FormState()) }

    // Handler cho actions — UDF: events go up, state goes down
    val onAction: (FormAction) -> Unit = { action ->
        val newState = reduceFormState(formState, action)
        // Validate sau NextStep
        formState = if (action is FormAction.NextStep) {
            val validated = validateCurrentStep(formState)
            if (validated.hasCurrentStepErrors) validated
            else newState
        } else {
            newState
        }
    }

    if (formState.isSubmitted) {
        // Màn hình thành công
        SubmissionSuccessScreen(
            formState = formState,
            modifier = modifier,
        )
    } else {
        // Form chính
        FormContent(
            state = formState,
            onAction = onAction,
            modifier = modifier,
        )
    }
}

// ─── Stateless Form Content (UDF Consumer) ───────────────────────────────────

/**
 * FormContent — stateless, nhận state + onAction
 *
 * Đây là điểm áp dụng UDF:
 * - Nhận state từ trên xuống (state goes down)
 * - Gửi actions lên trên (events go up)
 * - FormContent không biết cách xử lý state, chỉ biết render và gửi events
 */
@Composable
private fun FormContent(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        // Header: Step indicator
        FormHeader(state = state)

        Spacer(modifier = Modifier.height(16.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { state.progress },
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Step ${state.currentStep + 1} of ${state.totalSteps}: ${state.stepTitle}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.primary,
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Step content — AnimatedContent để tạo slide animation khi chuyển step
        AnimatedContent(
            targetState = state.currentStep,
            transitionSpec = {
                // Slide từ phải vào nếu đi tới, từ trái vào nếu đi lùi
                val direction = if (targetState > initialState) 1 else -1
                slideInHorizontally { it * direction } togetherWith
                    slideOutHorizontally { it * -direction }
            },
            modifier = Modifier.weight(1f),
            label = "form_step_animation",
        ) { step ->
            when (step) {
                0 -> PersonalInfoStep(state = state, onAction = onAction)
                1 -> ContactStep(state = state, onAction = onAction)
                2 -> PreferencesStep(state = state, onAction = onAction)
                3 -> ReviewStep(state = state)
                else -> Unit
            }
        }

        // Navigation buttons
        FormNavigationButtons(
            state = state,
            onAction = onAction,
        )
    }
}

// ─── Form Header ──────────────────────────────────────────────────────────────

@Composable
private fun FormHeader(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(
            text = "Registration Form",
            style = MaterialTheme.typography.headlineMedium,
        )
        Text(
            text = "Điền đầy đủ thông tin để hoàn tất đăng ký",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Step 1: Personal Info ────────────────────────────────────────────────────

@Composable
private fun PersonalInfoStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // First Name
        ValidatedTextField(
            value = state.firstName,
            onValueChange = { onAction(FormAction.UpdateFirstName(it)) },
            label = "First Name *",
            errorMessage = state.firstNameError,
        )

        // Last Name
        ValidatedTextField(
            value = state.lastName,
            onValueChange = { onAction(FormAction.UpdateLastName(it)) },
            label = "Last Name *",
            errorMessage = state.lastNameError,
        )

        // Birth Year
        OutlinedTextField(
            value = state.birthYear,
            onValueChange = { onAction(FormAction.UpdateBirthYear(it)) },
            label = { Text("Birth Year (optional)") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
        )
    }
}

// ─── Step 2: Contact ──────────────────────────────────────────────────────────

@Composable
private fun ContactStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Email
        ValidatedTextField(
            value = state.email,
            onValueChange = { onAction(FormAction.UpdateEmail(it)) },
            label = "Email *",
            errorMessage = state.emailError,
            keyboardType = KeyboardType.Email,
        )

        // Phone
        ValidatedTextField(
            value = state.phone,
            onValueChange = { onAction(FormAction.UpdatePhone(it)) },
            label = "Phone *",
            errorMessage = state.phoneError,
            keyboardType = KeyboardType.Phone,
        )

        // City
        OutlinedTextField(
            value = state.city,
            onValueChange = { onAction(FormAction.UpdateCity(it)) },
            label = { Text("City (optional)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
        )
    }
}

// ─── Step 3: Preferences ─────────────────────────────────────────────────────

@Composable
private fun PreferencesStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val languages = listOf("Vietnamese", "English", "Japanese", "Korean")

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text("Preferences", style = MaterialTheme.typography.titleMedium)

        // Toggle options
        SwitchRow(
            label = "Receive newsletter",
            checked = state.receiveNewsletter,
            onCheckedChange = { onAction(FormAction.UpdateNewsletter(it)) },
        )

        SwitchRow(
            label = "Push notifications",
            checked = state.receiveNotifications,
            onCheckedChange = { onAction(FormAction.UpdateNotifications(it)) },
        )

        HorizontalDivider()

        Text("Preferred language", style = MaterialTheme.typography.titleSmall)

        // Language selection
        languages.forEach { lang ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.preferredLanguage == lang,
                    onClick = { onAction(FormAction.UpdateLanguage(lang)) },
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(lang)
            }
        }
    }
}

// ─── Step 4: Review ───────────────────────────────────────────────────────────

@Composable
private fun ReviewStep(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = "Review your information",
            style = MaterialTheme.typography.titleMedium,
        )

        // Hiển thị tất cả data đã điền
        ReviewSection(title = "Personal Info") {
            ReviewRow("Name", "${state.firstName} ${state.lastName}")
            ReviewRow("Birth Year", state.birthYear.ifBlank { "—" })
        }

        ReviewSection(title = "Contact") {
            ReviewRow("Email", state.email)
            ReviewRow("Phone", state.phone)
            ReviewRow("City", state.city.ifBlank { "—" })
        }

        ReviewSection(title = "Preferences") {
            ReviewRow("Newsletter", if (state.receiveNewsletter) "Yes" else "No")
            ReviewRow("Notifications", if (state.receiveNotifications) "Yes" else "No")
            ReviewRow("Language", state.preferredLanguage)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Nhấn Submit để hoàn tất đăng ký",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun ReviewSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
        )
    }
}

// ─── Navigation Buttons ───────────────────────────────────────────────────────

@Composable
private fun FormNavigationButtons(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isLastStep = state.currentStep == state.totalSteps - 1

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Nút Back (ẩn ở step đầu tiên)
        if (state.currentStep > 0) {
            OutlinedButton(
                onClick = { onAction(FormAction.PrevStep) },
                modifier = Modifier.weight(1f),
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Back")
            }
        }

        // Nút Next / Submit
        Button(
            onClick = {
                if (isLastStep) {
                    onAction(FormAction.Submit)
                } else {
                    onAction(FormAction.NextStep)
                }
            },
            modifier = Modifier.weight(1f),
        ) {
            if (isLastStep) {
                Icon(Icons.Default.Check, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Submit")
            } else {
                Text("Next")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
            }
        }
    }
}

// ─── Shared Components ────────────────────────────────────────────────────────

@Composable
private fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = errorMessage != null,
        supportingText = errorMessage?.let { { Text(it) } },
        modifier = modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        singleLine = true,
    )
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

// ─── Success Screen ───────────────────────────────────────────────────────────

@Composable
private fun SubmissionSuccessScreen(
    formState: FormState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        // Success icon
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(80.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Registration Complete!",
            style = MaterialTheme.typography.headlineMedium,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Welcome, ${formState.firstName} ${formState.lastName}!",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

// ─── Previews ─────────────────────────────────────────────────────────────────

@Preview(showBackground = true, name = "Multi Step Form - Light")
@Composable
private fun MultiStepFormPreview() {
    AppTheme {
        MultiStepFormScreen()
    }
}

@Preview(
    showBackground = true,
    name = "Multi Step Form - Dark",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun MultiStepFormDarkPreview() {
    AppTheme(darkTheme = true) {
        MultiStepFormScreen()
    }
}

@Preview(showBackground = true, name = "Review Step Preview")
@Composable
private fun ReviewStepPreview() {
    AppTheme {
        val sampleState = FormState(
            firstName = "John",
            lastName = "Doe",
            birthYear = "1995",
            email = "john@example.com",
            phone = "0901234567",
            city = "Ho Chi Minh City",
            receiveNewsletter = true,
            receiveNotifications = true,
            preferredLanguage = "Vietnamese",
            currentStep = 3,
        )
        FormContent(state = sampleState, onAction = {})
    }
}

@Preview(showBackground = true, name = "Success Screen Preview")
@Composable
private fun SuccessScreenPreview() {
    AppTheme {
        SubmissionSuccessScreen(
            formState = FormState(firstName = "John", lastName = "Doe"),
        )
    }
}
