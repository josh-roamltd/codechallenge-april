package joshvdh.com.codechallengeapril.screen

import android.widget.Toast
import com.roamltd.kotlinkit.mvp.BaseView
import com.roamltd.kotlinkit.mvp.Presenter
import com.roamltd.kotlinkit.mvp.RoamFragment

abstract class CCFragment<V: BaseView, P: Presenter<V>>: RoamFragment<V, P>(), BaseView {
    protected fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}