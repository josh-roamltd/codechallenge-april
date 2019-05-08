package joshvdh.com.codechallengeapril.screen.recordings

import android.content.Context
import android.graphics.Color
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.widget.RelativeLayout
import com.roamltd.kotlinkit.view.param.*
import com.roamltd.kotlinkit.view.recycler.RoamViewHolder
import com.roamltd.kotlinkit.view.textview.textSizeDP
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.model.Recording

class RecordingsCell(context: Context) : RoamViewHolder(context) {

    interface Callback {
        fun onCellClicked()
    }

    private val cardRoot = RelativeLayout(context)
    private val icon = AppCompatImageView(context)
    private val titleText = AppCompatTextView(context)
    private val moreIcon = AppCompatImageView(context)

    private var callback: Callback? = null

    init {
        contentView.apply {
            addView(cardRoot, Params.rel().fullWidth(200).marginX(4).marginTop(10)) {
                background = RecordingCellDrawable(
                    ContextCompat.getColor(context, R.color.cc_cell_grey),
                    ContextCompat.getColor(context, waveColors.random()),
                    12f
                ).apply {
                    setWaveOffsetPercent(0.3f + (Math.random()*0.7).toFloat())
                }

                addView(icon, Params.rel().size(20f).margin(12f)){
                    setBackgroundResource(R.drawable.icon_cell_bell)
                }

                addView(moreIcon, Params.rel().size(20f).margin(12f).alignRight())

                addView(titleText, Params.rel().fillWrapped().marginLeft(12f).marginTop(32f)) {
                    textSizeDP = 12f
                    setTextColor(Color.WHITE)
                }
            }
        }.setOnClickListener {
            callback?.onCellClicked()
        }
    }

    fun bindData(recording: Recording, callback: Callback) {
        titleText.text = recording.recordingName
        this.callback = callback
    }

    companion object {
        val waveColors = arrayOf(
            R.color.cc_wave_blue,
            R.color.cc_wave_blue_lt,
            R.color.cc_wave_green,
            R.color.cc_wave_green_lt,
            R.color.cc_wave_purple,
            R.color.cc_wave_purple_lt
        )
    }
}