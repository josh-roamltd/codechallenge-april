package joshvdh.com.codechallengeapril.mvp

interface BaseView{}

abstract class Presenter<V: BaseView> {
    protected var view: V? = null

    fun onBindView(view: V?) {
        this.view = view
    }

    fun onUnbindView() {
        this.view = null
    }

    fun onPause() {}
    fun onResume() {}
}