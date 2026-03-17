package com.example.kmpday3.util

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.addressOf
import kotlinx.cinterop.usePinned
import platform.Foundation.NSData
import platform.Foundation.getBytes
import platform.UIKit.*
import platform.darwin.NSObject

class ImagePickerDelegate(
    val onResult: (ByteArray?) -> Unit
) : NSObject(), UIImagePickerControllerDelegateProtocol, UINavigationControllerDelegateProtocol {

    override fun imagePickerController(
        picker: UIImagePickerController,
        didFinishPickingMediaWithInfo: Map<Any?, *>
    ) {
        val image = didFinishPickingMediaWithInfo[UIImagePickerControllerEditedImage]
            ?: didFinishPickingMediaWithInfo[UIImagePickerControllerOriginalImage]

        if (image !is UIImage) {
            picker.dismissViewControllerAnimated(true, null)
            onResult(null)
            return
        }

        val data = UIImageJPEGRepresentation(image, 0.9)
        val bytes = data?.toByteArray()

        picker.dismissViewControllerAnimated(true, null)
        onResult(bytes)
    }

    override fun imagePickerControllerDidCancel(picker: UIImagePickerController) {
        picker.dismissViewControllerAnimated(true, null)
        onResult(null)
    }
}

@OptIn(ExperimentalForeignApi::class)
fun NSData.toByteArray(): ByteArray {
    val length = this.length.toInt()
    val byteArray = ByteArray(length)
    byteArray.usePinned { pinned ->
        this.getBytes(pinned.addressOf(0), length.toULong())
    }
    return byteArray
}