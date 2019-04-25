package joshvdh.com.codechallengeapril.screen.recordings

import joshvdh.com.codechallengeapril.model.Recording
import joshvdh.com.codechallengeapril.mvp.BaseView
import joshvdh.com.codechallengeapril.mvp.Presenter

interface RecordingsView : BaseView {

}

class RecordingsPresenter(private val recordings: List<Recording>) : Presenter<RecordingsView>() {
    fun onRecordingClicked(recording: Recording) {

    }
}