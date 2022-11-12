package su.lemon4k.connect.ui.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface LoadingView : MvpView {
    @AddToEndSingle
    fun showLoading()

    @AddToEndSingle
    fun hideLoading()
}