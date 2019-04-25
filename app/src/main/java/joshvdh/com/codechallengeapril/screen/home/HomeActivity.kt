package joshvdh.com.codechallengeapril.screen.home

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import joshvdh.com.codechallengeapril.R

interface HomeCallback {
    fun showAddRecordingScreen()
}

class HomeActivity: AppCompatActivity(), HomeCallback {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun showHomeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.homeFragment, RecordingsFragment())
    }


}