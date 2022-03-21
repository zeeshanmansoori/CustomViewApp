package com.google.customviewapp.displayOverlay
import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.view.WindowManager
import android.widget.ImageView
import com.google.customviewapp.R


class ChatHeadService : Service() {
    private var windowManager: WindowManager? = null
    private var chatHead: ImageView? = null
    var params: WindowManager.LayoutParams? = null
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        chatHead = ImageView(this)
        chatHead!!.setImageResource(R.drawable.ic_launcher_background)
        params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT
        )
        params!!.gravity = Gravity.TOP or Gravity.LEFT
        params!!.x = 0
        params!!.y = 100

        //this code is for dragging the chat head
        chatHead!!.setOnTouchListener(object : OnTouchListener {
            private var initialX = 0
            private var initialY = 0
            private var initialTouchX = 0f
            private var initialTouchY = 0f
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialX = params!!.x
                        initialY = params!!.y
                        initialTouchX = event.rawX
                        initialTouchY = event.rawY
                        return true
                    }
                    MotionEvent.ACTION_UP -> return true
                    MotionEvent.ACTION_MOVE -> {
                        params!!.x = (initialX
                                + (event.rawX - initialTouchX).toInt())
                        params!!.y = (initialY
                                + (event.rawY - initialTouchY).toInt())
                        windowManager!!.updateViewLayout(chatHead, params)
                        return true
                    }
                }
                return false
            }
        })
        windowManager!!.addView(chatHead, params)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (chatHead != null) windowManager!!.removeView(chatHead)
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }
}