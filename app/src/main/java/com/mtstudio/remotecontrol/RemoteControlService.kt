package com.mtstudio.remotecontrol

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent

/**
 * Temporary stub of RemoteControlService to allow CI build to succeed.
 * Socket.io code removed so the project compiles. Reintroduce network
 * code later as a separate change.
 */
class RemoteControlService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        // TODO: Reconnect socket.io logic here when a compatible dependency is added.
    }

    private fun simulateClick(x: Float, y: Float) {
        val p = Path().apply { moveTo(x, y) }
        val gestureBuilder = GestureDescription.Builder()
        gestureBuilder.addStroke(GestureDescription.StrokeDescription(p, 0, 50))
        dispatchGesture(gestureBuilder.build(), null, null)
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
    override fun onInterrupt() {}
    override fun onDestroy() {
        super.onDestroy()
        // No socket to disconnect in the stub.
    }
}
