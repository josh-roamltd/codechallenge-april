package joshvdh.com.codechallengeapril.utils

import android.content.Context
import android.util.TypedValue


fun Number.toPX() = (this.toFloat() * DimenExtensions.dpAsPX).toInt()
fun Number.toDP() = this.toFloat() / DimenExtensions.dpAsPX

sealed class DimenExtensions {
    companion object {
        var dpAsPX = 0f

        fun init(appContext: Context) {
            dpAsPX = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                1f,
                appContext.resources.displayMetrics
            )
        }
    }
}

