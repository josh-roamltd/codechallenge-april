package joshvdh.com.codechallengeapril.model

import io.realm.Realm
import io.realm.RealmObject
import io.realm.kotlin.createObject
import io.realm.kotlin.where

open class Recording : RealmObject() {
    var recordingId = ""
        private set
    var recordingName = ""
        private set

    fun getId() = recordingId.hashCode().toLong()

    companion object {
        fun create(id: String, name: String, realm: Realm) = fromId(id, realm).apply {
            recordingName = name
        }

        fun fromId(id: String, realm: Realm) =
            realm.where<Recording>().equalTo("recordingId", id).findFirst()
                ?: realm.createObject<Recording>().apply {
                    recordingId = id
            }
    }
}