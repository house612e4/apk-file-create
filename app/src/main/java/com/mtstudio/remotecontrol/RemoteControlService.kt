package com.mtstudio.remotecontrol

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.view.accessibility.AccessibilityEvent
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class RemoteControlService : AccessibilityService() {

    private lateinit var mSocket: Socket
    private val SERVER_URL = "https://mt-universal-hub.onrender.com" 
    private val SECRET_KEY = "MT_STUDIO_2026"

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        val options = IO.Options().apply {
            auth = mapOf("token" to SECRET_KEY)
        }
        mSocket = IO.socket(SERVER_URL, options)
        mSocket.connect()

        mSocket.on("child-execute-command") { args ->
            if (args.isNotEmpty()) {
                val cmd = args[0] as JSONObject
                val action = cmd.getString("action")
                val percentX = cmd.getDouble("x")
                val percentY = cmd.getDouble("y")

                val displayMetrics = resources.displayMetrics
                val screenWidth = displayMetrics.widthPixels
                val screenHeight = displayMetrics.heightPixels

                val actualX = (percentX * screenWidth).toFloat()
                val actualY = (percentY * screenHeight).toFloat()

                if (action == "click") {
                    simulateClick(actualX, actualY)
                }
            }
        }
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
        mSocket.disconnect()
    }
}