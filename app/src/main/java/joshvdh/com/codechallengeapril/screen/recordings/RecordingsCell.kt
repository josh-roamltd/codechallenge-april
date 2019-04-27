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

    val cardRoot = RelativeLayout(context)
    val icon = AppCompatImageView(context)
    val titleText = AppCompatTextView(context)
    val moreIcon = AppCompatImageView(context)

    init {
        contentView.apply {
            //TODO: Shadow?
//            addView(View(context), )

            addView(cardRoot, Params.rel().fullWidth(200).marginX(4).marginTop(10)) {
                background = RecordingCellDrawable(
                    ContextCompat.getColor(context, R.color.cc_cell_grey),
                    ContextCompat.getColor(context, waveColors.random()),
                    12f
                )

                addView(icon, Params.rel().size(20f).margin(12f)){
                    setBackgroundResource(R.drawable.icon_cell_bell)
                }

                addView(moreIcon, Params.rel().size(20f).margin(12f).alignRight())

                addView(titleText, Params.rel().fillWrapped().marginLeft(12f).marginTop(32f)) {
                    textSizeDP = 12f
                    setTextColor(Color.WHITE)
                }
            }
        }
    }

    fun bindData(recording: Recording) {
        titleText.text = recording.recordingName
    }

    companion object {
        val waveColors = arrayOf(R.color.cc_wave_blue, R.color.cc_wave_green, R.color.cc_wave_purple)
    }
}