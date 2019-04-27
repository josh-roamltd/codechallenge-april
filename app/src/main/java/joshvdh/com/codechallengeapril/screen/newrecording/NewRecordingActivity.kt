package joshvdh.com.codechallengeapril.screen.newrecording

import android.os.Bundle
import com.roamltd.kotlinkit.RLog
import com.roamltd.kotlinkit.view.setOnClickListener
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.screen.CCActivity
import kotlinx.android.synthetic.main.activity_newrecording.*

class NewRecordingActivity: CCActivity<NewRecordingView, NewRecordingPresenter>(), NewRecordingView {

    override fun initPresenter() = NewRecordingPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_newrecording)

        newRecordingBackBtnBg.setOnClickListener(presenter::onBackPressed)
        newRecordingRecordBg.setOnClickListener(presenter::onStartPressed)

        presenter.onStartPressed()
    }

    override fun onBack() {
        onBackPressed()
    }

    private var maxVal = Double.MIN_VALUE
    private var minVal = Double.MAX_VALUE

    override fun onDataChanged(values: List<Double>) {
        values.forEach {
            maxVal = Math.max(maxVal, it)
            minVal = Math.min(minVal, it)
        }
        val median = values.average()

        RLog.e("Audio values: count:${values.size}, min val: $minVal, max val: $maxVal, average val: $median")
    }
}