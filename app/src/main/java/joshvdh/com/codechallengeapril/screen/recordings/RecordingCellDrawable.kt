package joshvdh.com.codechallengeapril.screen.recordings

import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt
import android.support.v4.graphics.ColorUtils
import joshvdh.com.codechallengeapril.utils.toPX

class RecordingCellDrawable(
    @ColorInt private val backgroundColor: Int,
    @ColorInt private val waveColor: Int,
    private val radiusDP: Float
) : Drawable() {
    private val wavePaint = Paint(ColorUtils.setAlphaComponent(waveColor, 128))
    private var opacity: Int = 255

    private val backgroundPath = Path()
    private val wavePathA = Path()

    val rectf = RectF()
    override fun draw(canvas: Canvas) {
        backgroundPath.reset()
        rectf.set(canvas.clipBounds)
        val radii = radiusDP.toPX().toFloat()
        backgroundPath.addRoundRect(rectf, radii, radii, Path.Direction.CW)
        canvas.clipPath(backgroundPath)

        //Draw the background color
        canvas.drawColor(backgroundColor)



        //Draw some waves at 50p opacity
        canvas.drawPath()
    }

    override fun setAlpha(alpha: Int) {
        opacity = alpha
    }

    override fun getAlpha(): Int = opacity

    override fun getOpacity(): Int = PixelFormat.TRANSPARENT

    override fun setColorFilter(colorFilter: ColorFilter?) {
        backgroundPaint.colorFilter = colorFilter
        wavePaint.colorFilter = colorFilter
    }
}