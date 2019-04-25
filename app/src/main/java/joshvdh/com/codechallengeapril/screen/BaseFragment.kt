package joshvdh.com.codechallengeapril.screen

import android.content.Context
import android.support.v4.app.Fragment
import joshvdh.com.codechallengeapril.mvp.BaseView
import joshvdh.com.codechallengeapril.mvp.Presenter

abstract class BaseFragment<V: BaseView, P: Presenter<V>>: Fragment(), BaseView {
    abstract val presenter: P

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        presenter.onBindView(this as V)
    }

    override fun onDetach() {
        super.onDetach()
        presenter.onUnbindView()
    }

    override fun onPause() {
        super.onPause()
        presenter.onResume()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }
}