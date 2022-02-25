package com.google.customviewapp.views

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.util.*

object ViewUtils {
    private const val DEFAULT_RECT_BORDER_MARGIN = 40.0f

    // Give the bounds of path without stroke width included
    fun getBoundOfPath(path: Path?): RectF {
        val rectF = RectF()
        if (path == null) return rectF
        path.computeBounds(rectF, true)
        return rectF
    }

    fun setBoundViewToRect(view: View, rectF: RectF): FrameLayout.LayoutParams {
        val params = FrameLayout.LayoutParams(
            Math.abs(Math.max(rectF.left, 0f) - rectF.right).toInt(),
            Math.abs(Math.max(rectF.top, 0f) - rectF.bottom).toInt()
        )
        view.layoutParams = params
        view.translationX = Math.max(rectF.left, 0f)
        view.translationY = Math.max(rectF.top, 0f)
        return params
    }

    fun computeRenderOffset(view: View, pivotX: Float, pivotY: Float) {
        if (view.pivotX == pivotX && view.pivotY == pivotY) {
            return
        }
        val prevPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(prevPoint)
        view.pivotX = pivotX
        view.pivotY = pivotY
        val currPoint = floatArrayOf(0.0f, 0.0f)
        view.matrix.mapPoints(currPoint)
        val offsetX = currPoint[0] - prevPoint[0]
        val offsetY = currPoint[1] - prevPoint[1]
        view.translationX = view.translationX - offsetX
        view.translationY = view.translationY - offsetY
    }

    @JvmStatic
    fun adjustViewTranslation(view: View, deltaX: Float, deltaY: Float): Boolean {
        val deltaVector = floatArrayOf(deltaX, deltaY)
        view.matrix.mapVectors(deltaVector)
        val translateX = view.translationX + deltaVector[0]
        val translateY = view.translationY + deltaVector[1]
        // uncomment this if you face issues with the view going out of screen
//        if (! (view instanceof RulerView)
//                && !(view instanceof TriangularRulerView)
//                && !(view instanceof ProtractorView)
//                && (translateX < 0 || translateY < 0)) return false;
        view.translationX = translateX
        view.translationY = translateY
        return true
    }

    fun slideUpDownAnimate(view: View?, length: Float) {
        ObjectAnimator
            .ofFloat(view, "translationY", length)
            .setDuration(500)
            .start()
    }

    fun scaleImageLayout(layout: LinearLayout, scale: Int, scaleRatio: Float) {
        val layoutParams = layout.layoutParams as RelativeLayout.LayoutParams
        layoutParams.height += (scaleRatio * scale).toInt()
        layoutParams.width += (scaleRatio * scale).toInt()
        layoutParams.leftMargin += (scaleRatio * scale).toInt()
        layout.layoutParams = layoutParams
    }

    fun loadBitmapFromView(v: View): Bitmap {
        val b = Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val c = Canvas(b)
        v.layout(v.left, v.top, v.right, v.bottom)
        v.draw(c)
        return b
    }

    @JvmStatic
    fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context!!, drawableId)!!
        val bitmap = Bitmap.createBitmap(
            drawable.intrinsicWidth,
            drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun eraseStampBitmapOnCanvas(
        canvas: Canvas,
        pointF: PointF,
        mBitmap: Bitmap,
        stampBitmapRect: Rect
    ) {
        setStampBitmapRect(stampBitmapRect, pointF, mBitmap)
        val size = mBitmap.height * mBitmap.width
        val pixels = IntArray(size)
        val copyBitmap = Bitmap.createScaledBitmap(mBitmap, mBitmap.width, mBitmap.height, false)
        copyBitmap.getPixels(pixels, 0, mBitmap.width, 0, 0, mBitmap.width, mBitmap.height)
        for (i in pixels.indices) {
            if (pixels[i] != 0) {
                pixels[i] = Color.WHITE
            }
        }
        copyBitmap.setPixels(pixels, 0, mBitmap.width, 0, 0, mBitmap.width, mBitmap.height)
        val paint = Paint()
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_OUT)
        canvas.drawBitmap(copyBitmap, null, stampBitmapRect, paint)
    }

    fun setStampBitmapRect(bitmapRect: Rect, operation: PointF, mBitmap: Bitmap) {
        val width = mBitmap.width
        val height = mBitmap.height
        bitmapRect[operation.x.toInt() - width / 2, operation.y.toInt() - height / 2, operation.x.toInt() + mBitmap.width / 2] =
            operation.y.toInt() + mBitmap.height / 2
    }

    @JvmStatic
    fun getDistance(lastX: Float, lastY: Float, x: Float, y: Float): Double {
        val x1 = lastX - x
        val y1 = lastY - y
        return Math.sqrt((x1 * x1 + y1 * y1).toDouble())
    }

    fun getStringFromBitmap(bitmapOfStamp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmapOfStamp.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    fun getStringFromPointList(pointList: List<PointF>): String {
        val ans = StringBuilder()
        for (pointF in pointList) {
            ans.append(pointF.x).append("-").append(pointF.y).append(",")
        }
        return ans.toString()
    }

    fun getPathData(drawPath: Path?): StringBuilder {
        val measure = PathMeasure(drawPath, false)
        val aCoordinates = FloatArray(2)
        var distance = 0f
        val speed = 1f
        var iStarting = true
        val ans = StringBuilder()
        while (distance <= measure.length) {
            measure.getPosTan(distance, aCoordinates, null)
            if (iStarting) {
                ans.append("M").append(aCoordinates[0]).append(",").append(aCoordinates[1])
                    .append(" ")
                iStarting = false
            } else {
                ans.append("L").append(aCoordinates[0]).append(",").append(aCoordinates[1])
                    .append(" ")
            }
            distance += speed
        }
        return ans
    }

    fun getBitmapFromString(bitmapStr: String?): Bitmap? {
        return try {
            val encodeByte = Base64.decode(bitmapStr, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun dpFromPx(context: Context, px: Float): Float {
        return px / context.resources.displayMetrics.density
    }

    @JvmStatic
    fun pxFromDp(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }

    fun stringForTime(timeMs: Int): String {
        val mFormatBuilder = StringBuilder()
        val mFormatter = Formatter(mFormatBuilder, Locale.getDefault())
        val totalSeconds = timeMs / 1000
        //  videoDurationInSeconds = totalSeconds % 60;
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        mFormatBuilder.setLength(0)
        return if (hours > 0) {
            mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString()
        } else {
            mFormatter.format("%02d:%02d", minutes, seconds).toString()
        }
    }

    fun log(s: String?) {
        Log.d("zeeshan", s!!)
    }

    fun log(s: String, error: Throwable) {
        Log.d("zeeshan", s + error)
        Log.e("zeeshan", s, error)
    }
}