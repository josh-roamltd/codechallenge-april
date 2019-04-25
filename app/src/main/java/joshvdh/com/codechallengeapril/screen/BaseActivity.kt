package joshvdh.com.codechallengeapril.screen

import android.support.v7.app.AppCompatActivity
import joshvdh.com.codechallengeapril.mvp.BaseView
import joshvdh.com.codechallengeapril.mvp.Presenter

abstract class BaseActivity<V: BaseView, P: Presenter<V>>: AppCompatActivity(), BaseView {
    abstract val presenter: P

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        presenter.onBindView(this as V)
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onUnbindView()
    }
}