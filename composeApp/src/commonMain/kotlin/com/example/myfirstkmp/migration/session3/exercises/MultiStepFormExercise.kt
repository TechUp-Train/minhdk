package com.example.myfirstkmp.migration.session3.exercises

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.SupervisedUserCircle
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmp.migration.session1.exercises.OceanBlue
import com.example.myfirstkmp.theme.AppTheme
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

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

fun formatLongDate(millis: Long): String {
    val instant = Instant.fromEpochMilliseconds(millis)
    val local = instant.toLocalDateTime(TimeZone.currentSystemDefault())

    val day = local.day.toString().padStart(2, '0')
    val month = local.month.number.toString().padStart(2, '0')
    val year = local.year.toString()

    return "$day/$month/$year"
}

private val languages = listOf(
    "English (US)",
    "English (UK)",
    "Vietnamese",
    "Japanese",
    "Korean",
    "Chinese",
    "French",
    "German",
    "Spanish",
    "Italian"
)

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

fun reduceFormState(state: FormState, action: FormAction): FormState {
    return when (action) {
        is FormAction.UpdateFirstName -> state.copy(
            firstName = action.value,
            firstNameError = null
        )

        is FormAction.UpdateLastName -> state.copy(
            lastName = action.value,
            lastNameError = null
        )

        is FormAction.UpdateBirthYear -> state.copy(birthYear = action.value)
        is FormAction.UpdateEmail -> state.copy(
            email = action.value,
            emailError = null
        )

        is FormAction.UpdatePhone -> state.copy(
            phone = action.value,
            phoneError = null
        )

        is FormAction.UpdateCity -> state.copy(city = action.value)
        is FormAction.UpdateNewsletter -> state.copy(receiveNewsletter = action.enabled)
        is FormAction.UpdateNotifications -> state.copy(receiveNotifications = action.enabled)
        is FormAction.UpdateLanguage -> state.copy(preferredLanguage = action.language)
        is FormAction.NextStep -> {
            val validated = validateCurrentStep(state)
            if (validated.hasCurrentStepErrors) {
                validated
            } else {
                validated.copy(
                    currentStep = minOf(state.currentStep + 1, state.totalSteps - 1)
                )
            }
        }

        is FormAction.PrevStep -> state.copy(
            currentStep = maxOf(state.currentStep - 1, 0)
        )

        is FormAction.Submit -> state.copy(isSubmitted = true)
    }
}

private fun validateCurrentStep(state: FormState): FormState {
    return when (state.currentStep) {
        0 -> state.copy(
            firstNameError = if (state.firstName.isBlank()) "First name is required" else null,
            lastNameError = if (state.lastName.isBlank()) "Last name is required" else null
        )

        1 -> state.copy(
            emailError = if (!state.email.contains("@")) "Invalid email address" else null,
            phoneError = if (state.phone.length < 9) "Phone must be at least 9 digits" else null
        )

        else -> state
    }
}

private val FormState.hasCurrentStepErrors: Boolean
    get() = when (currentStep) {
        0 -> firstNameError != null || lastNameError != null
        1 -> emailError != null || phoneError != null
        else -> false
    }

// ─── Host Composable (Stateful) ───────────────────────────────────────────────
@Composable
fun MultiStepFormScreen(modifier: Modifier = Modifier) {
    var formState by remember { mutableStateOf(FormState()) }

    val onAction: (FormAction) -> Unit = { action ->
        formState = reduceFormState(formState, action)
    }

    if (formState.isSubmitted) {
        SubmissionSuccessScreen(formState = formState)
    } else {
        Column(
            modifier = modifier.fillMaxSize()
        ) {
            FormHeader(state = formState)

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                FormContent(
                    state = formState,
                    onAction = onAction
                )
            }

            FormNavigationButtons(
                state = formState,
                onAction = { onAction(it) }
            )
        }
    }
}

// ─── Stateless Form Content (UDF Consumer) ───────────────────────────────────

@Preview
@Composable
private fun FormContent(
    modifier: Modifier = Modifier,
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {}
) {
    when (state.currentStep) {
        0 -> PersonalInfoStep(modifier = modifier, state = state, onAction = onAction)
        1 -> ContactStep(state = state, onAction = onAction)
        2 -> PreferencesStep(state = state, onAction = onAction)
        3 -> ReviewStep(modifier = modifier, state = state)
        else -> {}
    }
}

// ─── Form Header ──────────────────────────────────────────────────────────────
@Preview
@Composable
private fun FormHeader(
    modifier: Modifier = Modifier,
    state: FormState = FormState()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = {},
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF1A1A2E)
                )
            }

            Text(
                text = state.stepTitle,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = state.stepTitle,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF1A1A2E)
            )
            Text(
                text = "Step ${state.stepTitle} of ${state.totalSteps}",
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        val progress = (state.currentStep.toFloat() + 1) / state.totalSteps.toFloat()

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .height(6.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = Color(0xFF3D5AFE),
            trackColor = Color(0xFFE0E0E0)
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

// ─── Step 1: Personal Info ────────────────────────────────────────────────────
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
                painter = ColorPainter(color = OceanBlue.copy(alpha = 0.5f)),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }

        Surface(
            modifier = Modifier
                .size(30.dp)
                .offset(x = (-4).dp, y = (-4).dp),
            shape = CircleShape,
            color = OceanBlue
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
        text = "Personal Info",
        style = MaterialTheme.typography.headlineSmall,
        color = Color.White,
        fontWeight = FontWeight.Bold
    )

    Spacer(modifier = Modifier.height(6.dp))

    Text(
        text = "Please provide your basic details to get started.",
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
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
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
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
            colors = OutlinedTextFieldDefaults.colors(
                disabledTextColor = Color.Black,
                disabledLabelColor = Color.Gray,
                disabledLeadingIconColor = Color.Black,
                disabledBorderColor = Color.Gray,
                disabledPlaceholderColor = Color.Gray
            ),
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
                            val formatted = formatLongDate(millis)
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

@Preview
@Composable
fun PersonalInfoStep(
    modifier: Modifier = Modifier,
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(12.dp),
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

// ─── Step 2: Contact ──────────────────────────────────────────────────────────
@Preview
@Composable
fun ContactStep(
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(12.dp)
    ) {

        Text(
            text = "Personal Info",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )

        Spacer(Modifier.height(24.dp))

        InputField(
            input = state.email,
            hint = "Email Address",
            icon = Icons.Default.Email,
            onValueChange = {
                onAction(FormAction.UpdateEmail(it))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        InputField(
            input = state.phone,
            hint = "Phone Number",
            icon = Icons.Default.Phone,
            onValueChange = {
                onAction(FormAction.UpdatePhone(it))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        InputField(
            input = state.city,
            hint = "City",
            icon = Icons.Default.LocationOn,
            onValueChange = {
                onAction(FormAction.UpdateCity(it))
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// ─── Step 3: Preferences ─────────────────────────────────────────────────────

@Composable
fun PreferenceSwitch(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFF3D5AFE)
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreferencesStep(
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
    ) {

        Text(
            text = "Preferences",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D5AFE)
        )

        Spacer(Modifier.height(6.dp))

        Text(
            text = "Customize your experience and communication settings.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Preferred Language",
            style = MaterialTheme.typography.labelLarge,
            color = Color(0xFF3D5AFE)
        )

        Spacer(Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {

            OutlinedTextField(
                value = state.preferredLanguage,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded)
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languages.forEach { language ->
                    DropdownMenuItem(
                        text = { Text(language) },
                        onClick = {
                            onAction(FormAction.UpdateLanguage(language))
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))

        PreferenceSwitch(
            title = "Receive Newsletter",
            description = "Stay updated with our weekly curated news and insights.",
            checked = state.receiveNewsletter,
            onCheckedChange = {
                onAction(FormAction.UpdateNewsletter(!state.receiveNewsletter))
            }
        )

        Spacer(Modifier.height(20.dp))

        PreferenceSwitch(
            title = "Push Notifications",
            description = "Get real-time alerts about your account activity.",
            checked = state.receiveNotifications,
            onCheckedChange = {
                onAction(FormAction.UpdateNotifications(!state.receiveNotifications))
            }
        )
    }
}

// ─── Step 4: Review ───────────────────────────────────────────────────────────

@Composable
private fun PreferenceItem(
    text: String,
    enable: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val icon = if (enable) Icons.Outlined.CheckCircle else Icons.Default.Cancel
        val color = if (enable) Color.Blue else Color.Red
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = Color(0xFF1A1A2E)
        )
    }
}

@Composable
private fun ReviewItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F6FA), RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(Color(0xFFECEFF1), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color(0xFF5C6BC0),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color(0xFF9E9E9E)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF1A1A2E)
            )
        }
    }
}

@Composable
private fun ReviewSection(
    title: String,
    onEditClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF3D5AFE),
            letterSpacing = 0.8.sp
        )
        TextButton(onClick = onEditClick) {
            Text(
                text = "Edit",
                fontSize = 13.sp,
                color = Color(0xFF3D5AFE)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    Column(content = content)
}

@Preview
@Composable
private fun ReviewStep(
    modifier: Modifier = Modifier,
    state: FormState = FormState()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 12.dp)
    ) {
        Text(
            text = "Review",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Please confirm your details before submitting.",
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E)
        )

        Spacer(modifier = Modifier.height(24.dp))

        ReviewSection(title = "PERSONAL INFO", onEditClick = {}) {
            ReviewItem(
                icon = Icons.Outlined.Person,
                label = "Full Name",
                value = state.firstName + " " + state.lastName
            )
            Spacer(modifier = Modifier.height(8.dp))
            ReviewItem(
                icon = Icons.Outlined.CalendarMonth,
                label = "Date of Birth",
                value = state.birthYear
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReviewSection(title = "CONTACT DETAILS", onEditClick = {}) {
            ReviewItem(
                icon = Icons.Outlined.Email,
                label = "Email Address",
                value = state.email
            )
            Spacer(modifier = Modifier.height(8.dp))
            ReviewItem(
                icon = Icons.Outlined.PhoneAndroid,
                label = "Phone Number",
                value = state.phone
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        ReviewSection(title = "PREFERENCES", onEditClick = {}) {
            PreferenceItem(text = "Newsletter Subscribed", enable = state.receiveNewsletter)
            PreferenceItem(
                text = "Two-Factor Authentication Enabled",
                enable = state.receiveNotifications
            )
            PreferenceItem(
                text = "Region: North America (EST)",
                enable = state.receiveNotifications
            )
        }
    }
}

// ─── Navigation Buttons ───────────────────────────────────────────────────────
@Composable
private fun BoxScope.BottomButtonsStep1(
    modifier: Modifier = Modifier,
    onAction: (FormAction) -> Unit
) {
    Button(
        onClick = {
            onAction(FormAction.NextStep)
        },
        modifier = modifier,
        shape = RoundedCornerShape(50.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
    ) {
        Text(text = "Next", color = Color.White, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.width(4.dp))
        Icon(
            Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
private fun BoxScope.BottomButtonsStep2(
    modifier: Modifier = Modifier,
    onAction: (FormAction) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { onAction(FormAction.PrevStep) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, Color(0xFFBDBDBD))
        ) {
            Text(
                text = "Back",
                color = Color(0xFF1A1A2E),
                fontWeight = FontWeight.SemiBold
            )
        }
        Button(
            onClick = {
                onAction(FormAction.NextStep)
            },
            modifier = Modifier.weight(2f),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
        ) {
            Text(text = "Next", color = Color.White, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun BoxScope.BottomButtonsStep3(
    modifier: Modifier = Modifier,
    onAction: (FormAction) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { onAction(FormAction.PrevStep) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, Color(0xFFBDBDBD))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF1A1A2E),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Back",
                color = Color(0xFF1A1A2E),
                fontWeight = FontWeight.SemiBold
            )
        }
        Button(
            onClick = { onAction(FormAction.NextStep) },
            modifier = Modifier.weight(2f),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
        ) {
            Text(
                text = "Next Step",
                color = Color.White,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Composable
private fun BoxScope.BottomButtonsStep4(
    modifier: Modifier = Modifier,
    onAction: (FormAction) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = { onAction(FormAction.PrevStep) },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(50.dp),
            border = BorderStroke(1.dp, Color(0xFFBDBDBD))
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = null,
                tint = Color(0xFF1A1A2E),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Back",
                color = Color(0xFF1A1A2E),
                fontWeight = FontWeight.SemiBold
            )
        }
        Button(
            onClick = { onAction(FormAction.Submit) },
            modifier = Modifier.weight(2f),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3D5AFE))
        ) {
            Text(
                text = "Submit Application",
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun FormNavigationButtons(
    state: FormState = FormState(),
    onAction: (FormAction) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        when (state.currentStep) {
            0 -> BottomButtonsStep1(
                modifier = Modifier.align(Alignment.CenterEnd),
                onAction = onAction
            )

            1 -> BottomButtonsStep2(
                modifier = Modifier.fillMaxWidth(),
                onAction = onAction
            )

            2 -> BottomButtonsStep3(
                modifier = Modifier.fillMaxWidth(),
                onAction = onAction
            )

            3 -> BottomButtonsStep4(
                modifier = Modifier.fillMaxWidth(),
                onAction = onAction
            )
        }
    }
}

// ─── Success Screen ───────────────────────────────────────────────────────────
@Preview
@Composable
private fun SubmissionSuccessScreen(
    modifier: Modifier = Modifier,
    formState: FormState = FormState()
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Registration Successful",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A2E),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Your account has been created successfully. You can now explore all the features and start your journey with us.",
            fontSize = 14.sp,
            color = Color(0xFF9E9E9E),
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
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
    name = "Multi Step Form - Dark"
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
