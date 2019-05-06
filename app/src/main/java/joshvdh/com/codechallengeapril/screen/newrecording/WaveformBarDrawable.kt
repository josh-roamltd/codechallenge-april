package joshvdh.com.codechallengeapril.screen.newrecording

import android.graphics.Canvas
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.ColorInt

class WaveformBarDrawable(@ColorInt barColor: Int, private val barsPerQuadrant: Int) : Drawable() {
    private val barPaint = Paint().apply {
        color = barColor
        style = Paint.Style.FILL
    }

    private var waveFormData = mutableListOf<Double>()
    private var maxVal = 0.0
    private var minVal = 0.0
    private var lastTime = System.currentTimeMillis()

    fun setData(waveForm: List<Double>, minVal: Double, maxVal: Double) {
        this.waveFormData = waveForm.toMutableList()
        this.minVal = minVal
        this.maxVal = maxVal
        invalidateSelf()
    }

    override fun draw(canvas: Canvas) {
        val deltaTime = (System.currentTimeMillis() / lastTime) * 0.001
        lastTime = System.currentTimeMillis()

        if (waveFormData.isEmpty()) return

        val quadSizeX = canvas.clipBounds.width() / 2
        val quadSizeY = canvas.clipBounds.height() / 2

        //Assuming the gaps
        val barWidth = quadSizeX / 2 / barsPerQuadrant

        drawQuadrant(canvas, QUAD_X_LEFT, QUAD_Y_TOP, barWidth, quadSizeX, quadSizeY)
        drawQuadrant(canvas, QUAD_X_RIGHT, QUAD_Y_TOP, barWidth, quadSizeX, quadSizeY)
        drawQuadrant(canvas, QUAD_X_LEFT, QUAD_Y_BOTTOM, barWidth, quadSizeX, quadSizeY)
        drawQuadrant(canvas, QUAD_X_RIGHT, QUAD_Y_BOTTOM, barWidth, quadSizeX, quadSizeY)

        //Reduce size of list
        waveFormData.forEachIndexed {
            index, d ->
            waveFormData[index] = Math.max(0.0, d * (1 - deltaTime))
        }
    }

    private fun getIndexOffset(index: Int, barCount: Int) = index * (waveFormData.size / barCount)

    private fun getIntensityForIndex(index: Int) =
        waveFormData.getOrNull(index)?.let { (it - (minVal)) / (maxVal - (minVal)) } ?: 0.0

    private val tempRect = Rect()
    private fun drawQuadrant(
        canvas: Canvas,
        directionX: Int,
        directionY: Int,
        barWidth: Int,
        quadrantWidth: Int,
        quadrantHeight: Int
    ) {
        for (i in 0 until barsPerQuadrant) {
            val realIndex = getIndexOffset(i, barsPerQuadrant)
            val startX = quadrantWidth + ((barWidth * 2 * i) * directionX)
            val endX = startX + barWidth * directionX
            val baseY = quadrantHeight
            val sizeY = (getIntensityForIndex(realIndex) * quadrantHeight).toInt()


            val endY = baseY + sizeY * directionY
            val top = Math.min(baseY, endY)
            val bottom = Math.max(baseY, endY)

            tempRect.set(Math.min(startX, endX), top, Math.max(startX, endX), bottom)

            //TODO Draw
            canvas.drawRect(tempRect, barPaint)
        }
    }

    override fun setAlpha(alpha: Int) {
        barPaint.alpha = alpha
    }

    override fun getOpacity() = barPaint.alpha

    override fun setColorFilter(colorFilter: ColorFilter?) {
        barPaint.colorFilter = colorFilter
    }

    companion object {
        private const val QUAD_Y_TOP = -1
        private const val QUAD_Y_BOTTOM = 1
        private const val QUAD_X_LEFT = -1
        private const val QUAD_X_RIGHT = 1
    }
}