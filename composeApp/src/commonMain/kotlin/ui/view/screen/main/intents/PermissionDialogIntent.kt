package ui.view.screen.main.intents

sealed class PermissionDialogIntent {
    data object Grant: PermissionDialogIntent()
    data object Deny: PermissionDialogIntent()
}