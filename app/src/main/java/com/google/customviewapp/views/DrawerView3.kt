package com.google.customviewapp.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.google.customviewapp.R

class DrawerView3(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {

    private var bitmapClose: Bitmap
    private var bitmapDone: Bitmap
    private var setRect = RectF()
    private var closeRect = RectF()

    init {
        setRectFs()
        val d1 = ContextCompat.getDrawable(context, R.drawable.ic_ruler_close)!!
        val d2 = ContextCompat.getDrawable(context, R.drawable.ic_protractor_done)!!

        bitmapClose =
            Bitmap.createBitmap(d1.intrinsicWidth, d1.intrinsicHeight, Bitmap.Config.ARGB_8888)
        bitmapDone =
            Bitmap.createBitmap(d2.intrinsicWidth, d2.intrinsicHeight, Bitmap.Config.ARGB_8888)

    }

    private fun setRectFs() {
        val mCenterX = 100
        val mCenterY = 200
        val DP_8 = context.resources.displayMetrics.density * 8
        setRect = RectF(
            mCenterX - DP_8 * 4f / scaleX,
            mCenterY - 6 * DP_8 / scaleY,
            mCenterX - DP_8 / scaleX,
            mCenterY - 3 * DP_8 / scaleY
        )
        closeRect = RectF(
            mCenterX + DP_8 / scaleX,
            mCenterY - 6f * DP_8 / scaleY,
            mCenterX + DP_8 * 4f / scaleX,
            mCenterY - 3 * DP_8 / scaleY
        )
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
setRectFs()
        canvas?.drawBitmap(bitmapClose,null, closeRect, null)
        canvas?.drawBitmap(bitmapDone, null,setRect, null,)
    }
}