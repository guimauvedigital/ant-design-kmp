package digital.guimauve.antdesign

import android.view.DragEvent

/**
 * Android implementation of DragEvent
 * Wraps Android's DragEvent for drag and drop operations
 */
actual class DragEvent(
    val nativeEvent: DragEvent? = null,
)
