package digital.guimauve.antdesign

import org.w3c.dom.DragEvent as WebDragEvent

/**
 * JS/Web implementation of DragEvent
 * Wraps browser's DragEvent for drag and drop operations
 */
actual class DragEvent(
    val nativeEvent: WebDragEvent? = null,
)
