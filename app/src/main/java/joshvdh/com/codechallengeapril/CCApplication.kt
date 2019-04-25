package joshvdh.com.codechallengeapril

import android.app.Application
import io.realm.Realm
import joshvdh.com.codechallengeapril.model.Recording
import joshvdh.com.codechallengeapril.utils.randomInt

class CCApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Realm.init(this)

        createDummyData()
    }

    private fun createDummyData() {
        //Create some dummy objects to display in a view
        //Create a new recording with a random name from a list of dummys
        val names = arrayOf("", "", "", "", "")

        Realm.getDefaultInstance().executeTransaction {realm ->
            for (i in 0..randomInt(25)) {
                Recording.create("$i", names.random(), realm)
            }
        }
    }
}