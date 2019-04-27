package joshvdh.com.codechallengeapril.screen.recordings

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.roamltd.kotlinkit.view.setOnClickListener
import io.realm.Realm
import io.realm.kotlin.where
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.model.Recording
import joshvdh.com.codechallengeapril.screen.CCFragment
import joshvdh.com.codechallengeapril.screen.home.HomeActivity
import kotlinx.android.synthetic.main.fragment_recordings.*

class RecordingsFragment : CCFragment<RecordingsView, RecordingsPresenter>(), RecordingsView {
    private val recordings = Realm.getDefaultInstance().where<Recording>().findAll()
    override fun initPresenter() = RecordingsPresenter(recordings)

    override fun onCreateView(savedInstanceState: Bundle?) {
        super.onCreateView(savedInstanceState)

        setContentView(R.layout.fragment_recordings)

        recordingsContent.layoutManager = LinearLayoutManager(context)

        recordingsAddBtnBg.apply {
            isClickable = true
            setOnClickListener (presenter::onAddClicked)
        }
    }

    override fun onBindData(recordings: List<Recording>) {
        recordingsContent.adapter = RecordingsAdapter(recordings)
    }

    override fun onResume() {
        super.onResume()

        recordingsAddBtn.alpha = 1f
        recordingsAddBtnBg.scaleX = 1f
        recordingsAddBtnBg.scaleY = 1f
    }

    override fun showAddRecordingScreen() {
        recordingsAddBtn.animate().alpha(0f).setDuration(250)
        recordingsAddBtnBg.animate().setStartDelay(250).scaleX(100f).scaleY(100f).setDuration(1000).withEndAction {
            (context as? HomeActivity)?.showAddRecordingScreen()
        }
    }
}