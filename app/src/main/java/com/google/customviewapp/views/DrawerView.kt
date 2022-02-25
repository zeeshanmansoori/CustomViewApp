package com.google.customviewapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*


class DrawerView(context: Context, attributeSet: AttributeSet? = null) :
    View(context, attributeSet) {

    private val MAX_CLICK_DURATION = 200
    private var startClickTime: Long = 0
    private val mPaint = Paint()
    private val textPaint = Paint()
    private var startX = 0f
    private var stopX = 0f

    private var startY = 0f
    private var stopY = 0f

    private val path = Path()
    private var drawerText: String? = null
    private var listener: DrawerListener? = null
    private var fill: Boolean = false
    private var mBitmap: Bitmap? = null
    private var mCanvas: Canvas? = null

    init {

        mPaint.apply {

            color = Color.BLACK
            strokeWidth = 13.5f
            isAntiAlias = true
            isDither = true
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            alpha = 255
            xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_OVER)
            shader = null


        }

        textPaint.apply {
            color = Color.BLACK
            textSize = 100f

        }


    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas()
        mCanvas?.setBitmap(mBitmap)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)


        if (mBitmap != null)
            canvas?.drawBitmap(mBitmap!!, 0f, 0f, null)
        //canvas?.drawLine(startX,startY,stopX,stopY,mPaint)

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {


        val action = event?.action ?: return true

        if (fill) {
            if (action != MotionEvent.ACTION_DOWN) return true
            floodFill(mBitmap, Point(x.toInt(), y.toInt()), Color.BLACK, Color.YELLOW)
            invalidate()
            return true
        }

        when (action) {
            MotionEvent.ACTION_DOWN -> {
                startClickTime = Calendar.getInstance().timeInMillis
                path.moveTo(event.x, event.y)
                startX = event.x
                startY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                path.lineTo(event.x, event.y)
                stopX = event.x
                stopY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val clickDuration: Long = Calendar.getInstance().timeInMillis - startClickTime
                if (clickDuration < MAX_CLICK_DURATION) {
                    listener?.onclick()
                }
            }


        }


        mCanvas?.apply {
            drawPath(path, mPaint)
            drawerText?.let { text ->
                drawText(text, 0, text.length, startX, startY + 20f, textPaint)
            }
        }
        invalidate()

        return true
    }


    fun clearCanvas() {
        path.reset()
        invalidate()
    }


    fun drawText(text: String) {
        drawerText = text
        invalidate()
    }


    fun setListener(drawerListener: DrawerListener) {
        listener = drawerListener
    }

    interface DrawerListener {
        fun onclick()
    }


    fun log(msg: String) {
        Log.d("zeeshan", "log: $msg")
    }

    private fun floodFill(newBitmap: Bitmap?, pt: Point, targetColor: Int, replacementColor: Int) {
        log("flood start ${System.currentTimeMillis()}")
        newBitmap ?: return
        val bmp = newBitmap
        val q: Queue<Point> = LinkedList()
        q.add(pt)
        while (q.size > 0) {
            val n = q.poll() ?: return

            if (bmp.getPixel(n.x, n.y) != targetColor) continue
            val e = Point(n.x + 1, n.y)
            while (n.x > 0 && bmp.getPixel(n.x, n.y) == targetColor) {
                bmp.setPixel(n.x, n.y, replacementColor)
                if (n.y > 0 && bmp.getPixel(n.x, n.y - 1) == targetColor) q.add(Point(n.x, n.y - 1))
                if (n.y < bmp.height - 1
                    && bmp.getPixel(n.x, n.y + 1) == targetColor
                ) q.add(Point(n.x, n.y + 1))
                n.x--
            }
            while (e.x < bmp.width - 1
                && bmp.getPixel(e.x, e.y) == targetColor
            ) {
                bmp.setPixel(e.x, e.y, replacementColor)
                if (e.y > 0 && bmp.getPixel(e.x, e.y - 1) == targetColor) q.add(Point(e.x, e.y - 1))
                if (e.y < bmp.height - 1
                    && bmp.getPixel(e.x, e.y + 1) == targetColor
                ) q.add(Point(e.x, e.y + 1))
                e.x++
            }
        }
        log("flood start ${System.currentTimeMillis()}")
        mCanvas?.drawBitmap(bmp, Matrix(), null)
        invalidate()
        return
    }


    fun toggleFill() {
        fill = !fill
    }


}