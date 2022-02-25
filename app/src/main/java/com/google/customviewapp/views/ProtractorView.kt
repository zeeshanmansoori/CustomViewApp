package com.google.customviewapp.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.google.customviewapp.R
import com.google.customviewapp.views.ViewUtils.adjustViewTranslation
import com.google.customviewapp.views.ViewUtils.getBitmapFromVectorDrawable
import com.google.customviewapp.views.ViewUtils.getDistance
import com.google.customviewapp.views.ViewUtils.pxFromDp

class ProtractorView : View {
    private var MARGIN_FACTOR = 0
    var PROTRACTOR_HEIGHT = 0
    var PROTRACTOR_WIDTH = 0
    var VIEW_HEIGHT = 0
    private val STROKE_WIDTH = 5f
    private var RULER_X = -1f
    private var RULER_Y = -1f
    private var mLegPaths: Path? = null
    private var mPaint: Paint? = null
    private var mDrawPaint: Paint? = null
    private var mRadius = 0f
    private var mHandleRadius = 0f
    private var mCenterX = 0f
    private var mCenterY = 0f
    private var mTouchX = 0f
    private var mTouchY = 0f
    private var moveX = 0f
    private var moveY = 0f
    private var mRulerX = 0f
    private var mRulerY = 0f
    private var handlePath: Path? = null
    private var handleRegion: Region? = null
    private var isMoving = false
    private var isDone = false
    private var isClose = false
    private var isHandle = true
    private var setRect: RectF? = null
    private var closeRect: RectF? = null
    private var markRect: RectF? = null
    private var backRect: RectF? = null
    private var mBitmapCross: Bitmap? = null
    private var mBitmapDone: Bitmap? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        MARGIN_FACTOR = pxFromDp(context, MARGIN_DP)
            .toInt()
        PROTRACTOR_WIDTH = pxFromDp(context, WIDTH_DP)
            .toInt()
        PROTRACTOR_HEIGHT = PROTRACTOR_WIDTH / 2
        VIEW_HEIGHT = PROTRACTOR_HEIGHT + MARGIN_FACTOR
        DP_8 = pxFromDp(context, 40f)
        mCenterY = PROTRACTOR_HEIGHT.toFloat()
        mCenterX = mCenterY
        mRadius = (PROTRACTOR_HEIGHT - MARGIN_FACTOR).toFloat()
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.color = Color.RED
        mPaint!!.style = Paint.Style.FILL_AND_STROKE
        markRect = RectF(
            mCenterX - mRadius, mCenterY - mRadius,
            mCenterX + mRadius, mCenterY + pxFromDp(context, 5f)
        )
        backRect = RectF(
            mCenterX - mRadius, mCenterY - mRadius,
            mCenterX + mRadius, mCenterY + pxFromDp(context, 5f)
        )
        mDrawPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mDrawPaint!!.style = Paint.Style.STROKE
        mDrawPaint!!.color = Color.BLACK
        mDrawPaint!!.strokeWidth = STROKE_WIDTH
        mLegPaths = Path()
        mTouchX = mCenterX - mRadius
        mTouchY = mCenterY
        mHandleRadius = 1.5f * DP_8
        handlePath = Path()
        handleRegion = Region()
        setRectFs()
        mBitmapCross = getBitmapFromVectorDrawable(context, R.drawable.ic_ruler_close)
        mBitmapDone = getBitmapFromVectorDrawable(context, R.drawable.ic_protractor_done)
        //mBitmapMarkings = getBitmapFromVectorDrawable(context, R.drawable.ic_protractor_markings)
        //mBitmapBack = getBitmapFromVectorDrawable(context, R.drawable.ic_protractor_back)
    }

    //900
    private fun setRectFs() {
        setRect = RectF(
            mCenterX - DP_8 * 4f / scaleX, mCenterY - 4f * DP_8 / scaleY,
            mCenterX - DP_8 / scaleX*.5f, mCenterY -  DP_8 / scaleY * 1.2f
        )
        closeRect = RectF(
            mCenterX + 0.2f*DP_8 / scaleX, mCenterY - 4f * DP_8 / scaleY,
            mCenterX + DP_8 * 3.5f / scaleX, mCenterY -   DP_8 / scaleY * 1.2f
        )
    }
//
//    private fun setRectFs() {
//        setRect = RectF(
//            mCenterX - DP_8 * 4f / scaleX, mCenterY - 6 * DP_8 / scaleY,
//            mCenterX - DP_8 / scaleX, mCenterY - 3 * DP_8 / scaleY
//        )
//        closeRect = RectF(
//            mCenterX + DP_8 / scaleX, mCenterY - 6f * DP_8 / scaleY,
//            mCenterX + DP_8 * 4f / scaleX, mCenterY - 3 * DP_8 / scaleY
//        )
//    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        setRectFs()
        drawBitmaps(canvas)

    }

    private fun drawBitmaps(canvas: Canvas) {
        canvas.drawBitmap(mBitmapCross!!, null, closeRect!!, null)
        canvas.drawBitmap(mBitmapDone!!, null, setRect!!, null)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.getX(0)
        val y = event.getY(0)
        if (RULER_X == -1f && RULER_Y == -1f) {
            RULER_X = getX()
            RULER_Y = getY()
        }
        if (event.actionMasked == MotionEvent.ACTION_DOWN) {
            Log.i("TAG", "ProTouch: here")
            if (!isInHandle(x, y) && !isInProtractor(x, y)) {
                return false
            }
            isHandle = true
            moveX = x
            moveY = 0f
            when {
                isInButton(setRect, x.toInt(), y.toInt()) -> {
                    isDone = true
                }
                isInButton(closeRect, x.toInt(), y.toInt()) -> {
                    isClose = true
                }
                isInProtractor(x, y) -> {
                    isMoving = true
                    moveY = y
                    Log.i("TAG", "onTouchEvent: here move")
                }
                isInHandle(x, y) -> {
                    val res = pointOnRadialDistance(x, y, mRadius)
                    mTouchX = res[0]
                    mTouchY = res[1]
                    invalidate()
                }
            }
            return true
        }
        if (isMoving) {
            when (event.action) {
                MotionEvent.ACTION_MOVE -> {
                    if (event.findPointerIndex(0) != 0) return false
                    adjustViewTranslation(this, x - moveX, y - moveY)
                }
                MotionEvent.ACTION_UP -> {
                    isClose = false
                    isMoving = isClose
                }
            }
            return true
        }
        if (isClose) {
            if (event.actionMasked == MotionEvent.ACTION_UP) {
                if (isInButton(closeRect, x.toInt(), y.toInt())) {
                    isMoving = false
                    isDone = isMoving
                    isClose = isDone
                    //   onRulerCloseListener.onRulerCloseListener(DisciplineFragment.DisciplineType.PROTRACTOR)
                }
            }
            return true
        }
        if (isDone) {
            if (event.actionMasked == MotionEvent.ACTION_UP) {
                if (isInButton(setRect, x.toInt(), y.toInt())) {
                    invalidate()
                }
                isMoving = false
                isDone = isMoving
            }
            return true
        }
        // else every thing will be
        // in the drawing part
        if (isHandle) {
            when (event.actionMasked) {
                MotionEvent.ACTION_MOVE, MotionEvent.ACTION_UP -> {
                    if (y > PROTRACTOR_HEIGHT) return true
                    val res = pointOnRadialDistance(x, y, mRadius)
                    mTouchX = res[0]
                    mTouchY = res[1]
                    invalidate()
                }
            }
        }
        return true
    }

    private fun isInProtractor(x: Float, y: Float): Boolean {
        val tmp = getDistance(x, y, mCenterX, mCenterY)
        return x >= MARGIN_FACTOR && x <= PROTRACTOR_WIDTH - MARGIN_FACTOR && y >= MARGIN_FACTOR && y <= PROTRACTOR_HEIGHT && tmp <= mRadius
    }

    private fun isInButton(rectF: RectF?, x: Int, y: Int): Boolean {
        val r = Rect()
        rectF!!.round(r)
        return Region(r).contains(x, y)
    }

    private fun isInHandle(x: Float, y: Float): Boolean {
        return handleRegion!!.contains(x.toInt(), y.toInt())
    }


    private fun pointOnRadialDistance(x: Float, y: Float, dist: Float): FloatArray {
        val res = FloatArray(2)
        val modV = getDistance(x, y, mCenterX, mCenterY)
        res[0] = ((x - mCenterX) * dist / modV + mCenterX).toFloat()
        res[1] = ((y - mCenterY) * dist / modV + mCenterY).toFloat()
        return res
    }

    private fun calculateRulerTip() {
        val values = FloatArray(9)
        val mat = this.matrix
        mat.getValues(values)
        val dX = values[Matrix.MTRANS_X]
        val dY = values[Matrix.MTRANS_Y]
        mRulerX = RULER_X + dX
        mRulerY = RULER_Y + dY
    }


    fun checkMinHeight(height: Float): Boolean {
        return height >= 2 * MARGIN_FACTOR + 9 * DP_8
    }

    companion object {
        var DP_8 = 0f
        const val POINTER_INDEX_FACTOR = 3
        private const val MARGIN_DP = 28f
        private const val WIDTH_DP = 300f
    }
}