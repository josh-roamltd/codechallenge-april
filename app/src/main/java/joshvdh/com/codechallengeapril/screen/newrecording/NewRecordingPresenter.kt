package joshvdh.com.codechallengeapril.screen.newrecording

import com.roamltd.kotlinkit.mvp.BaseView
import com.roamltd.kotlinkit.mvp.Presenter
import joshvdh.com.codechallengeapril.audiofft.AudioRecordThread
import joshvdh.com.codechallengeapril.utils.lerpTo
import java.util.*

interface NewRecordingView: BaseView {
    fun showHomeScreen()
    fun onRecordingUpdated(recording: Boolean)
    fun onDataChanged(values: List<Double>, minVal: Double, maxVal: Double)
}

class NewRecordingPresenter : Presenter<NewRecordingView>() {
    private val recordingThread = AudioRecordThread(callback = this::onFFTUpdated)

    private fun onFFTUpdated(fftData: List<Double>) {
        processFFTData(fftData)
        view?.onDataChanged(fftData, minVal, maxVal)
    }

    private var maxVal = Double.MIN_VALUE
    private var minVal = Double.MAX_VALUE

    private val rollingMaxVals = LinkedList<Double>()
    private val rollingMinVals = LinkedList<Double>()
    private var lastTime = System.currentTimeMillis()
    private var lastData = listOf<Double>()
    private var isRecording = false

    private fun processFFTData(values: List<Double>) {
        val deltaTime = (System.currentTimeMillis() / lastTime) * 0.001
        lastTime = System.currentTimeMillis()
        var filtered = values.toMutableList().apply {
            forEachIndexed { index, _ ->
                var sanitized = getOrElse(index) { 0.0 }
                sanitized = if (lastData.isEmpty()) sanitized
                else lastData[index].lerpTo(sanitized, deltaTime * 200.0)
                sanitized = Math.max(0.0, sanitized)
                set(index, sanitized)

                maxVal = Math.max(maxVal, sanitized)
                minVal = Math.min(minVal, sanitized)
            }
        }
        lastData = filtered

        rollingMaxVals.push(maxVal)
        rollingMinVals.push(minVal)
        if (rollingMaxVals.size > MAX_ROLLING_AVERAGE_SIZE) {
            rollingMaxVals.pop()
            rollingMinVals.pop()
        }
    }

    override fun onUnBindView() {
        stopThread()
    }

    fun onRecordingTogglePressed() {
        isRecording = !isRecording
        if (isRecording) {
            onRecordingStarted()
        } else {
            onRecordingStopped()
        }
    }

    fun onBackPressed() {
        view?.showHomeScreen()
    }

    private fun onRecordingStarted() {
        startThread()
        view?.onRecordingUpdated(true)
    }

    private fun onRecordingStopped() {
        stopThread()
        view?.onRecordingUpdated(false)
        view?.showHomeScreen()
    }

    private fun startThread() {
        recordingThread.onStart()
        recordingThread.runThreadLoop()
    }

    private fun stopThread() {
        recordingThread.stopThreadLoop()
        recordingThread.onStop()
    }

    companion object {
        private const val MAX_ROLLING_AVERAGE_SIZE = 32
    }
}