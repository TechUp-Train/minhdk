package com.example.myfirstkmp

import platform.Foundation.*

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter
import platform.Foundation.NSNumberFormatterDecimalStyle
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

actual fun getPlatform(): Platform = IOSPlatform()

actual fun formatString(value: Double): String {
    val formatter = NSNumberFormatter().apply {
        numberStyle = NSNumberFormatterDecimalStyle
        minimumFractionDigits = 2u
        maximumFractionDigits = 2u
    }
    val number = NSNumber.numberWithDouble(value)
    return "$${formatter.stringFromNumber(number) ?: value.toString()}"
}