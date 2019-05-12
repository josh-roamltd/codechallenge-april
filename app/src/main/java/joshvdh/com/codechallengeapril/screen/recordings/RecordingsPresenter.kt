package joshvdh.com.codechallengeapril.screen.recordings

import com.roamltd.kotlinkit.mvp.BaseView
import com.roamltd.kotlinkit.mvp.Presenter
import joshvdh.com.codechallengeapril.model.Recording

interface RecordingsView : BaseView {
    fun onBindData(recordings: List<Recording>)
    fun showAddRecordingScreen()
}

class RecordingsPresenter(private val recordings: List<Recording>) : Presenter<RecordingsView>() {

    override fun onBindView(view: RecordingsView) {
        super.onBindView(view)
        view.onBindData(recordings)
    }

    fun onRecordingClicked(recording: Recording) {
        //Stub
    }

    fun onAddClicked() {
        view?.showAddRecordingScreen()
    }
}