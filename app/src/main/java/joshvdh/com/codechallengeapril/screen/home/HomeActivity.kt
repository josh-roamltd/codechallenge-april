package joshvdh.com.codechallengeapril.screen.home

import android.Manifest.permission.RECORD_AUDIO
import android.os.Bundle
import android.support.v7.app.AlertDialog
import com.roamltd.kotlinkit.permission.Permissions
import com.roamltd.kotlinkit.screens.BaseActivity
import com.roamltd.kotlinkit.screens.transaction
import joshvdh.com.codechallengeapril.R
import joshvdh.com.codechallengeapril.screen.newrecording.NewRecordingActivity
import joshvdh.com.codechallengeapril.screen.recordings.RecordingsFragment

interface HomeCallback {
    fun showAddRecordingScreen()
}

class HomeActivity : BaseActivity(), HomeCallback, Permissions.Provider {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_home)

        showHomeFragment()

        Permissions(this).requestPermissions(arrayOf(RECORD_AUDIO)) { success, accepted ->
            if (!success) {
                AlertDialog.Builder(this)
                    .setTitle("Permissions failed")
                    .setPositiveButton("OK") { dialog, which ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    private fun showHomeFragment() {
        supportFragmentManager.transaction {
            replace(R.id.homeFragment, RecordingsFragment())
        }
    }

    override fun showAddRecordingScreen() {
        presentActivity(NewRecordingActivity::class)
    }
}