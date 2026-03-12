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
 * ⭐⭐⭐⭐ BÀI TẬP 4: Multi-step Registration Form (Advanced)
 *
 * Mô tả: Form đăng ký nhiều bước với complex state management, validation, UDF pattern
 *
 * Steps: Personal Info → Contact → Preferences → Review
 *
 * Key concepts:
 * - @Stable annotation: đánh dấu class "ổn định" → Compose SKIP recompose nếu params không đổi
 * - UDF (Unidirectional Data Flow): State đi xuống, Events đi lên
 * - sealed class FormAction: type-safe events thay vì nhiều callbacks
 * - AnimatedContent: slide animation khi chuyển step
 * - rememberSaveable + custom Saver: form state survive xoay màn hình
 *
 * Bonus (nếu xong sớm):
 * - TODO: [Bonus] Implement custom Saver cho FormState (dùng mapSaver hoặc listSaver)
 * - val formState = rememberSaveable(saver = FormStateSaver) { FormState() }
 */

// ─── State & Actions (UDF pattern) ───────────────────────────────────────────

/**
 * @Stable annotation — tại sao cần?
 *
 * @Stable nói với Compose rằng:
 * 1. Nếu các properties không đổi (theo equals()), class được xem là "stable"
 * 2. Compose CÓ THỂ SKIP recompose nếu toàn bộ params không thay đổi
 *
 * @Stable vs @Immutable:
 * - @Stable: properties có thể thay đổi NHƯNG theo equals() đúng cách
 * - @Immutable: properties KHÔNG BAO GIỜ thay đổi (mạnh hơn @Stable)
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
    val currentStep: Int = 0,

    // Validation errors
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
 * sealed class FormAction — type-safe events từ UI lên ViewModel/Host
 *
 * Thay vì nhiều callbacks rời rạc (onFirstNameChange, onNext, onSubmit...)
 * → Dùng 1 callback duy nhất: onAction: (FormAction) -> Unit
 *
 * Lợi ích: API gọn hơn, dễ log, dễ test
 */
sealed class FormAction {
    data class UpdateFirstName(val value: String) : FormAction()
    data class UpdateLastName(val value: String) : FormAction()
    data class UpdateBirthYear(val value: String) : FormAction()
    data class UpdateEmail(val value: String) : FormAction()
    data class UpdatePhone(val value: String) : FormAction()
    data class UpdateCity(val value: String) : FormAction()
    data class UpdateNewsletter(val enabled: Boolean) : FormAction()
    data class UpdateNotifications(val enabled: Boolean) : FormAction()
    data class UpdateLanguage(val language: String) : FormAction()
    data object NextStep : FormAction()
    data object PrevStep : FormAction()
    data object Submit : FormAction()
}

// ─── Business Logic (Reducer) ─────────────────────────────────────────────────

/**
 * Hàm reduce: nhận state hiện tại + action → trả về state mới
 *
 * Pattern: Pure function, không có side effects
 * - Input: (FormState, FormAction) → Output: FormState
 * - Dễ test: chỉ cần verify output state
 */
fun reduceFormState(state: FormState, action: FormAction): FormState {
    // TODO: Implement reduceFormState
    // Với mỗi FormAction, trả về state.copy(...) phù hợp:
    // - UpdateFirstName → copy(firstName = action.value, firstNameError = null)
    // - UpdateEmail → copy(email = action.value, emailError = null)
    // - NextStep → validate trước (gọi validateCurrentStep), nếu có lỗi → trả lại state có lỗi
    //              nếu OK → copy(currentStep = min(currentStep + 1, totalSteps - 1))
    // - PrevStep → copy(currentStep = max(currentStep - 1, 0))
    // - Submit → copy(isSubmitted = true)
    // GỢI Ý: Dùng when (action) { is UpdateFirstName → ... }
    TODO("Not yet implemented")
}

private fun validateCurrentStep(state: FormState): FormState {
    // TODO: Validate dựa theo currentStep:
    // - Step 0: kiểm tra firstName và lastName không blank
    // - Step 1: kiểm tra email có "@", phone.length >= 9
    // - Các step khác: không cần validate
    // Trả về state.copy(xFirstNameError, lastNameError, emailError, phoneError)
    TODO("Not yet implemented")
}

private val FormState.hasCurrentStepErrors: Boolean
    get() = false  // TODO: Trả về true nếu step hiện tại có lỗi

// ─── Host Composable (Stateful) ───────────────────────────────────────────────

/**
 * MultiStepFormScreen — stateful host
 *
 * Host giữ state và cung cấp cho FormContent (stateless child)
 * Pattern: State hosting ở level cao nhất cần dùng state
 */
@Composable
fun MultiStepFormScreen(modifier: Modifier = Modifier) {
    // TODO: Implement MultiStepFormScreen
    // 1. var formState by remember { mutableStateOf(FormState()) }
    // 2. val onAction: (FormAction) -> Unit = { action → formState = reduceFormState(formState, action) }
    // 3. Kiểm tra formState.isSubmitted:
    //    → true: SubmissionSuccessScreen(formState)
    //    → false: FormContent(formState, onAction)
    Box {}
}

// ─── Stateless Form Content (UDF Consumer) ───────────────────────────────────

/**
 * FormContent — stateless, nhận state + onAction
 *
 * Đây là điểm áp dụng UDF:
 * - state goes down (nhận từ host)
 * - events go up (gửi onAction lên host)
 */
@Composable
private fun FormContent(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement FormContent
    // - Column(fillMaxSize, padding=16.dp)
    // - FormHeader(state)
    // - Spacer(16.dp)
    // - LinearProgressIndicator(progress = state.progress, fillMaxWidth)
    // - Spacer(4.dp) + Text "Step ${currentStep+1} of ${totalSteps}: ${stepTitle}" (primary)
    // - Spacer(24.dp)
    // - AnimatedContent(targetState = state.currentStep,
    //       transitionSpec = { // slide từ phải vào nếu đi tới, từ trái vào nếu đi lùi
    //           val direction = if (targetState > initialState) 1 else -1
    //           slideInHorizontally { it * direction } togetherWith slideOutHorizontally { it * -direction }
    //       },
    //       modifier = Modifier.weight(1f)
    //   ) { step → when(step) { 0 → PersonalInfoStep, 1 → ContactStep, 2 → PreferencesStep, 3 → ReviewStep } }
    // - FormNavigationButtons(state, onAction)
    Box {}
}

// ─── Form Header ──────────────────────────────────────────────────────────────

@Composable
private fun FormHeader(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement FormHeader
    // - Column: Text "Registration Form" (headlineMedium) + Text subtitle (bodyMedium, onSurfaceVariant)
    Box {}
}

// ─── Step 1: Personal Info ────────────────────────────────────────────────────

@Composable
private fun PersonalInfoStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement PersonalInfoStep
    // - Column(fillMaxSize, verticalScroll, spacedBy=12.dp)
    // - ValidatedTextField firstName (error = state.firstNameError)
    // - ValidatedTextField lastName (error = state.lastNameError)
    // - OutlinedTextField birthYear (optional, keyboardType = Number)
    Box {}
}

// ─── Step 2: Contact ──────────────────────────────────────────────────────────

@Composable
private fun ContactStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ContactStep
    // - Column(fillMaxSize, verticalScroll, spacedBy=12.dp)
    // - ValidatedTextField email (error = emailError, keyboardType = Email)
    // - ValidatedTextField phone (error = phoneError, keyboardType = Phone)
    // - OutlinedTextField city (optional)
    Box {}
}

// ─── Step 3: Preferences ─────────────────────────────────────────────────────

@Composable
private fun PreferencesStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement PreferencesStep
    // - Column(fillMaxSize, verticalScroll, spacedBy=16.dp)
    // - SwitchRow "Receive newsletter" (receiveNewsletter)
    // - SwitchRow "Push notifications" (receiveNotifications)
    // - HorizontalDivider
    // - Text "Preferred language"
    // - listOf("Vietnamese", "English", "Japanese", "Korean").forEach { lang →
    //     Row: RadioButton(selected = state.preferredLanguage == lang, onClick = ...) + Text lang
    //   }
    Box {}
}

// ─── Step 4: Review ───────────────────────────────────────────────────────────

@Composable
private fun ReviewStep(
    state: FormState,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ReviewStep
    // - Column(fillMaxSize, verticalScroll, spacedBy=16.dp)
    // - Text "Review your information" (titleMedium)
    // - ReviewSection("Personal Info") { ReviewRow("Name", ...) + ReviewRow("Birth Year", ...) }
    // - ReviewSection("Contact") { email, phone, city }
    // - ReviewSection("Preferences") { newsletter, notifications, language }
    // - Text "Nhấn Submit để hoàn tất" (bodySmall, onSurfaceVariant)
    Box {}
}

@Composable
private fun ReviewSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    // TODO: Implement ReviewSection
    // - Card(fillMaxWidth, surfaceVariant color) { Column(padding=12.dp) { Text title + content() } }
    Box {}
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement ReviewRow
    // - Row(fillMaxWidth, SpaceBetween): Text label (onSurfaceVariant) + Text value (Medium)
    Box {}
}

// ─── Navigation Buttons ───────────────────────────────────────────────────────

@Composable
private fun FormNavigationButtons(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement FormNavigationButtons
    // - val isLastStep = state.currentStep == state.totalSteps - 1
    // - Row(fillMaxWidth, spacedBy=12.dp, padding top=16.dp)
    // - Nếu currentStep > 0: OutlinedButton "Back" (weight(1f)) → onAction(PrevStep)
    // - Button "Next" hoặc "Submit" (weight(1f)) → onAction(NextStep) hoặc onAction(Submit)
    Box {}
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
    // TODO: Implement ValidatedTextField
    // - OutlinedTextField với isError = errorMessage != null
    // - supportingText = errorMessage?.let { { Text(it) } }
    Box {}
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement SwitchRow
    // - Row(fillMaxWidth, SpaceBetween, CenterVertically)
    // - Text label (bodyLarge) + Switch(checked, onCheckedChange)
    Box {}
}

// ─── Success Screen ───────────────────────────────────────────────────────────

@Composable
private fun SubmissionSuccessScreen(
    formState: FormState,
    modifier: Modifier = Modifier,
) {
    // TODO: Implement SubmissionSuccessScreen
    // - Column(fillMaxSize, padding=32.dp, Center, CenterHorizontally)
    // - Surface icon (80dp, extraLarge, primaryContainer) { Box(Center) { Icon(Check, 48dp) } }
    // - Spacer(24.dp) + Text "Registration Complete!" (headlineMedium)
    // - Spacer(8.dp) + Text "Welcome, ${firstName} ${lastName}!" (bodyLarge, onSurfaceVariant)
    Box {}
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
