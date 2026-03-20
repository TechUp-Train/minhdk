package com.minhdk.permissionlesson

sealed class ImageScreenState {

    data object Initial: ImageScreenState()

    data class PermissionGranted(
        val images: List<Image>,
        val selected: MutableList<String>
    ): ImageScreenState()

    data object PermissionDenied: ImageScreenState()

}
