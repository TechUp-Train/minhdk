package com.apero.composetraining.session3.exercises

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.media.Image
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.apero.composetraining.R
import com.apero.composetraining.common.AppTheme
import com.apero.composetraining.session1.exercises.OceanBlue
import java.text.SimpleDateFormat
import java.util.Date

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
val FormState.stepTitle: String
    get() = when (currentStep) {
        0 -> "Personal Info"
        1 -> "Contact Details"
        2 -> "Preferences"
        3 -> "Review & Submit"
        else -> ""
    }

@SuppressLint("SimpleDateFormat")
fun formatMillisToDate(millis: Long): String {
    val sdf = SimpleDateFormat("MM/dd/yyyy")
    return sdf.format(Date(millis))
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
}

// ─── Form Header ──────────────────────────────────────────────────────────────

@Composable
private fun FormHeader(
    state: FormState,
    modifier: Modifier = Modifier,
) {

}

// ─── Step 1: Personal Info ────────────────────────────────────────────────────

@Composable
fun RowScope.GenderChip(
    label: String,
    icon: ImageVector,
    selected: Boolean
) {
    val bg = if (selected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
    else
        MaterialTheme.colorScheme.surfaceVariant

    val borderColor = if (selected)
        MaterialTheme.colorScheme.primary
    else
        MaterialTheme.colorScheme.outlineVariant

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier
            .weight(1f)
            .height(48.dp)
            .clickable { }
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.width(6.dp))
            Text(
                label,
                color = if (selected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun ProfileAvatar(
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.BottomEnd,
        modifier = modifier
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Image(
                painter = ColorPainter(color = OceanBlue),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Surface(
            modifier = Modifier
                .size(30.dp)
                .offset(x = (-4).dp, y = (-4).dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primary
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Camera,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

@Composable
fun ColumnScope.InfoTitle(
    modifier: Modifier = Modifier
) {
    Text(
        "Avatar Picker",
        style = MaterialTheme.typography.titleMedium,
        color = Color.White,
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(10.dp))

    Text(
        "Optional: Choose your identity",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun InputField(
    modifier: Modifier = Modifier,
    input: String,
    icon: ImageVector,
    hint: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = input,
        onValueChange = onValueChange,
        label = { Text(hint) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)
@Composable
fun BirthDatePicker(
    date: String,
    onDateSelected: (String) -> Unit
) {
    var openDatePicker by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier.clickable {
            openDatePicker = true
        }
    ) {
        OutlinedTextField(
            value = date,
            onValueChange = {},
            label = { Text("Date of Birth") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = null
                )
            },
            placeholder = { Text("MM/DD/YYYY") },
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            readOnly = true,
            enabled = false,
            modifier = Modifier.fillMaxWidth()
        )
    }

    if (openDatePicker) {
        val datePickerState = rememberDatePickerState()

        DatePickerDialog(
            onDismissRequest = { openDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val formatted = formatMillisToDate(millis)
                            onDateSelected(formatted)
                        }
                        openDatePicker = false
                    }
                ) { Text("OK") }
            },
            dismissButton = {
                TextButton(onClick = { openDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

// ─── Step 2: Contact ──────────────────────────────────────────────────────────

@Preview
@Composable
fun ContactForm(
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAvatar(modifier = Modifier.size(120.dp))

        Spacer(Modifier.height(12.dp))

        InfoTitle()

        Spacer(Modifier.height(28.dp))

        InputField(
            input = state.firstName,
            icon = Icons.Default.SupervisedUserCircle,
            hint = "First name",
            modifier = Modifier.fillMaxWidth()
        ) {
            onAction(FormAction.UpdateFirstName(it))
        }

        Spacer(modifier = Modifier.height(10.dp))

        InputField(
            input = state.lastName,
            icon = Icons.Default.SupervisedUserCircle,
            hint = "Last name",
            modifier = Modifier.fillMaxWidth()
        ) {
            onAction(FormAction.UpdateLastName(it))
        }

        Spacer(modifier = Modifier.height(10.dp))

        BirthDatePicker(date = state.birthYear) {
            onAction(FormAction.UpdateBirthYear(it))
        }
    }
}

// ─── Step 3: Preferences ─────────────────────────────────────────────────────

@Composable
private fun PreferencesStep(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {

}

// ─── Step 4: Review ───────────────────────────────────────────────────────────

@Composable
private fun ReviewStep(
    state: FormState,
    modifier: Modifier = Modifier,
) {
}

@Composable
private fun ReviewSection(
    title: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
}

@Composable
private fun ReviewRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
}

// ─── Navigation Buttons ───────────────────────────────────────────────────────

@Composable
private fun FormNavigationButtons(
    state: FormState,
    onAction: (FormAction) -> Unit,
    modifier: Modifier = Modifier,
) {
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
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
}

// ─── Success Screen ───────────────────────────────────────────────────────────

@Composable
private fun SubmissionSuccessScreen(
    formState: FormState,
    modifier: Modifier = Modifier,
) {
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
