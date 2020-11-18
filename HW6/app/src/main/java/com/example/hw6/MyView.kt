package com.example.hw6

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.graphics.Rect
import android.os.Parcelable
import android.view.View
import androidx.core.content.ContextCompat
import android.graphics.drawable.Drawable
import android.content.res.TypedArray
import kotlin.math.abs


class MyView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    private val pic = ContextCompat.getDrawable(context, R.drawable.my_view)

    private val drawGround: Rect = Rect()

    private var time: Int
    private val speed: Int

    class MyState(state: Parcelable?, val time: Int) : BaseSavedState(state)

    override fun onSaveInstanceState(): Parcelable? {
        return MyState(super.onSaveInstanceState(), time)
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val my = state as MyState
        super.onRestoreInstanceState(my.superState)
        time = my.time
    }

    init {
        val a: TypedArray = context.obtainStyledAttributes(
            attrs, R.styleable.MyView, 0, 0
        )
        try {
            if (a.getBoolean(R.styleable.MyView_clockwise, true)) {
                speed = a.getInt(R.styleable.MyView_speed, 5)
            } else {
                speed = -a.getInt(R.styleable.MyView_speed, 5)
            }
            time = 0
        } finally {
            a.recycle()
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        pic?.let { pic ->
            time = (time + speed) % 720
                val picWidth = pic.intrinsicWidth
            val picHeight = pic.intrinsicHeight
            val diffWidth = width - picWidth
            val diffHeight = height - picHeight
            val left = diffWidth / 2
            val top = diffHeight / 2
            pic.setBounds(left, top, left + picWidth, top + picHeight)
            canvas?.let {canvas ->
                val saved = canvas.save()
                drawFirst(canvas, left, top, picWidth, picHeight, pic)
                canvas.restoreToCount(saved)
                drawSecond(canvas, left, top, picWidth, picHeight, pic)
            }

        }

        invalidate()
    }

    private fun drawFirst(
        canvas: Canvas,
        left: Int,
        top: Int,
        width: Int,
        height: Int,
        pic: Drawable
    ) {
        if (abs(time) < 360) {
            canvas.rotate(
                time.toFloat(), (left + width / 4).toFloat(), (top + height / 2).toFloat()
            )
        }
        drawGround.set(left, top, left + width / 2, top + height)
        canvas.clipRect(drawGround)
        pic.draw(canvas)
    }

    private fun drawSecond(
        canvas: Canvas,
        left: Int,
        top: Int,
        width: Int,
        height: Int,
        pic: Drawable
    ) {
        if (abs(time) > 360) {
            val alpha = ((time % 360) / 90) * 90
            canvas.rotate(
                alpha.toFloat(),
                (left + 3 * width / 4).toFloat(),
                (top + height / 2).toFloat()
            )
        }
        drawGround.set(left + width / 2, top, left + width, top + height)
        canvas.clipRect(drawGround)
        pic.draw(canvas)
    }
}

