package ui.view.screen.main.intents

sealed class PickImageIntent {
    data object PickImage: PickImageIntent()
    data object AskPermission: PickImageIntent()
}
