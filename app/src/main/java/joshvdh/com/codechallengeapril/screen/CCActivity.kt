package joshvdh.com.codechallengeapril.screen

import com.roamltd.kotlinkit.mvp.BaseView
import com.roamltd.kotlinkit.mvp.Presenter
import com.roamltd.kotlinkit.mvp.RoamActivity

abstract class CCActivity<V: BaseView, P: Presenter<V>>: RoamActivity<V, P>(), BaseView {
}