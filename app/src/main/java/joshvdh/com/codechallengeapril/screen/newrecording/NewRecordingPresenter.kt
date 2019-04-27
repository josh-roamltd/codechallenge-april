package joshvdh.com.codechallengeapril.screen.newrecording

import com.roamltd.kotlinkit.mvp.BaseView
import com.roamltd.kotlinkit.mvp.Presenter
import joshvdh.com.codechallengeapril.audiofft.AudioRecordThread

interface NewRecordingView: BaseView {
    fun onBack()
    fun onDataChanged(values: List<Double>)
}

class NewRecordingPresenter : Presenter<NewRecordingView>() {
    private val recordingThread = AudioRecordThread(callback = this::onFFTUpdated)

    private fun onFFTUpdated(fftData: List<Double>) {
        view?.onDataChanged(fftData)
    }

    override fun onUnBindView() {
        stopThread()
    }

    fun onBackPressed() {
        view?.onBack()
    }

    fun onStartPressed() {
        startThread()
    }

    fun onStopPressed() {
        stopThread()
    }

    private fun startThread() {
        recordingThread.onStart()
        recordingThread.runThreadLoop()
    }

    private fun stopThread() {
        recordingThread.onStop()
        recordingThread.stopThreadLoop()
    }
}