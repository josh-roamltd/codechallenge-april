package joshvdh.com.codechallengeapril.screen.recordings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.realm.Realm
import io.realm.kotlin.where
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.model.Recording
import joshvdh.com.codechallengeapril.screen.BaseFragment

class RecordingsFragment: BaseFragment<RecordingsView, RecordingsPresenter>(), RecordingsView {
    private val recordings = Realm.getDefaultInstance().where<Recording>().findAll()
    override val presenter = RecordingsPresenter(recordings)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_recordings, container, true).apply {

    }
}