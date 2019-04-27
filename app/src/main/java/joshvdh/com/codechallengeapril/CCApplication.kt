package joshvdh.com.codechallengeapril

import android.app.Application
import com.roamltd.kotlinkit.RoamToolkit
import com.roamltd.kotlinkit.randomInt
import io.realm.Realm
import io.realm.RealmConfiguration
import joshvdh.com.codechallengeapril.model.Recording

class CCApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        RoamToolkit.setup(this)

        Realm.init(this)
        Realm.setDefaultConfiguration(
            RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build()
        )

        createDummyData()
    }

    private fun createDummyData() {
        //Create some dummy objects to display in a view
        //Create a new recording with a random name from a list of dummys
        val names = arrayOf("", "", "", "", "")

        Realm.getDefaultInstance().executeTransaction { realm ->
            for (i in 0..randomInt(25)) {
                Recording.create("$i", names.random(), realm)
            }
        }
    }
}