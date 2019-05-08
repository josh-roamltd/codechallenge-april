package joshvdh.com.codechallengeapril.screen.newrecording

import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import com.roamltd.kotlinkit.view.setOnClickListener
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.screen.CCActivity
import kotlinx.android.synthetic.main.activity_newrecording.*

class NewRecordingActivity : CCActivity<NewRecordingView, NewRecordingPresenter>(), NewRecordingView {

    private val waveformDrawable = WaveformBarDrawable(Color.WHITE, 8)

    override fun initPresenter() = NewRecordingPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_newrecording)

        newRecordingBackBtnBg.setOnClickListener(presenter::onBackPressed)
        newRecordingRecordBg.setOnClickListener(presenter::onRecordingTogglePressed)

        waveformBackground.background = waveformDrawable

        animateIn()
    }

    private fun animateIn() {
        newRecordingBackBtnBg.apply {
            scaleX = 0f
            scaleY = 0f
            animate().scaleX(1f).scaleY(1f)
        }

        newRecordingBackBtn.apply {
            alpha = 0f
            animate().alpha(1f).setStartDelay(300)
        }

        newRecordingRecordBg.apply {
            alpha = 0f
            scaleX = 0f
            scaleY = 0f
            animate().alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setStartDelay(500)
        }
    }

    override fun showHomeScreen() {
        runOnUiThread {
            newRecordingBackBtnBg.animate().scaleX(100f).scaleY(100f)
            newRecordingBackBtn.animate().alpha(0f)
            newRecordingRecordBg.animate().alpha(0f)
            newRecordingRecordIcon.animate().alpha(0f)
            waveformBackground.animate().scaleY(0f).setStartDelay(300).setDuration(500).withEndAction {
                onBackPressed()
            }
        }
    }

    override fun onRecordingUpdated(recording: Boolean) {
        val finalScale = if (recording) ICON_SCALE_RECORDING else ICON_SCALE_STOPPED
        val icon = if (recording) R.drawable.icon_stop else R.drawable.icon_equaliser

        newRecordingRecordIcon.animate().scaleX(0f).scaleY(0f).alpha(0f).withEndAction {
            newRecordingRecordIcon.animate().scaleX(finalScale.x).scaleY(finalScale.y).alpha(1f)
            newRecordingRecordIcon.setImageResource(icon)
        }
    }

    override fun onDataChanged(values: List<Double>, minVal: Double, maxVal: Double) {
        waveformDrawable.setData(values, minVal, maxVal)
    }

    companion object {
        private val ICON_SCALE_RECORDING = PointF(1f, 1f)
        private val ICON_SCALE_STOPPED = PointF(1f, 0.7f)
    }
}