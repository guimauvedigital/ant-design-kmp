package com.antdesign.ui

/**
 * Android implementation of DragEvent
 * Wraps Android's DragEvent for drag and drop operations
 */
actual class DragEvent(
    val nativeEvent: android.view.DragEvent? = null
)
