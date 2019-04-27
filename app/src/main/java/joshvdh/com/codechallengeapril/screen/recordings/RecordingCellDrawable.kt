package joshvdh.com.codechallengeapril.screen.recordings

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import com.roamltd.kotlinkit.setAlpha
import com.roamltd.kotlinkit.view.toPX

class RecordingCellDrawable(
    @ColorInt private val backgroundColor: Int,
    @ColorInt private val waveColor: Int,
    private val radiusDP: Float
) : Drawable() {
    private val wavePaint = Paint().apply {
        color = waveColor.setAlpha(0.5f)
        style = Paint.Style.FILL
    }
    private var opacity: Int = 255

    private val backgroundPath = Path()
    private val wavePath = Path()
    private var offset = 0f

    private val rectf = RectF()
    override fun draw(canvas: Canvas) {
        backgroundPath.reset()
        rectf.set(canvas.clipBounds)
        val radii = radiusDP.toPX().toFloat()
        backgroundPath.addRoundRect(rectf, radii, radii, Path.Direction.CW)
        canvas.clipPath(backgroundPath)

        //Draw the background color
        canvas.drawColor(backgroundColor)

        //Draw some waves at 50p opacity
        val width = canvas.clipBounds.width().toFloat()
        val height = canvas.clipBounds.height().toFloat()
        val halfWidth = width * 0.5f
        val halfHeight = height * 0.5f
        val qWidth = halfWidth * 0.5f
        val qHeight = halfHeight * 0.5f
        var progress = Math.sin(getProgress(0.1f)*Math.PI*2f).toFloat()

        for (i in 0 until 2) {
            //TODO: make rolling waves moving on X axis
            wavePath.apply {
                reset()
                moveTo(0f, halfHeight)
                quadTo(qWidth, halfHeight + (halfHeight * progress), halfWidth, halfHeight)
                quadTo(halfWidth + qWidth, halfHeight + (halfHeight * (-progress)), width, halfHeight)
                lineTo(width, height)
                lineTo(0f, height)
                close()
            }
            canvas.drawPath(wavePath, wavePaint)
            progress *= -1f
        }

        invalidateSelf()
    }

    fun setWaveOffsetPercent(offsetPercent: Float) {
        offset = offsetPercent
    }

    override fun setAlpha(alpha: Int) {
        opacity = alpha
    }

    private fun getProgress(speed: Float) = (((System.currentTimeMillis()%10000).toFloat() * 0.001f * speed) + offset) % 1.0f

    override fun getAlpha(): Int = opacity

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        wavePaint.colorFilter = colorFilter
    }
}