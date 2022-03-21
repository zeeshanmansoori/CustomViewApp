package com.google.customviewapp.displayOverlay

import android.content.Context
import android.graphics.PixelFormat
import android.util.Log
import android.view.*
import com.google.customviewapp.R
import com.google.customviewapp.tools.log

class Window(  // declaring required variables
    private val context: Context
) {
    private var mView: View? = null
    private var mView2: View? = null

    init {
        addFirstView()
    }

    fun addFirstView() {
        val mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
            // than filling the screen
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,  // Display it on top of other application windows
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
            // through any transparent parts
            PixelFormat.TRANSLUCENT
        )
        // getting a LayoutInflater
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflating the view with the custom layout we created
        mView = layoutInflater.inflate(R.layout.popup_window, null)

        // set onClickListener on the remove button, which removes
        // the view from the window
        mView?.findViewById<View>(R.id.rubber)?.setOnClickListener { removeFirstView() }
        mView?.findViewById<View>(R.id.pencil)?.setOnClickListener { addSecondView() }
        // Define the position of the
        // window within the screen
        mParams.gravity = Gravity.START.or(Gravity.TOP)
        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addToWindow(mWindowManager, mView, mParams)
    }

    fun addSecondView() {
        val mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
            // than filling the screen
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,  // Display it on top of other application windows
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,  // Don't let it grab the input focus
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,  // Make the underlying application window visible
            // through any transparent parts
            PixelFormat.TRANSLUCENT
        )
        // getting a LayoutInflater
        val layoutInflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        // inflating the view with the custom layout we created
        mView2 = layoutInflater.inflate(R.layout.popup_window2, null)

        // set onClickListener on the remove button, which removes
        // the view from the window
        mView2?.findViewById<View>(R.id.rubber)?.setOnClickListener { removeFirstView() }


        val mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addToWindow(mWindowManager, mView2, mParams)
    }

    private fun addToWindow(
        windowManager: WindowManager,
        view: View?,
        mParams: WindowManager.LayoutParams
    ) {
        try {
            view ?: return
            if (view.windowToken == null) {
                if (view.parent == null) {
                    windowManager.addView(view, mParams)
                }
            }
        } catch (e: Exception) {
            log("exception $e")
        }
    }

    private fun removeFirstView() {
        try {
            // remove the view from the window
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
            // invalidate the view
            mView?.invalidate()

        } catch (e: Exception) {
            Log.d("Error2", e.toString())
        }
    }

//    private fun disableInteraction() {
//        try {
//            // remove the view from the window
//            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).removeView(mView)
//            // invalidate the view
//            mView.invalidate()
//            // remove all views
//            (mView.parent as ViewGroup).removeAllViews()
//
//            // the above steps are necessary when you are adding and removing
//            // the view simultaneously, it might give some exceptions
//        } catch (e: Exception) {
//            Log.d("Error2", e.toString())
//        }
//    }


}