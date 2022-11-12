package su.lemon4k.connect.ui.views

import moxy.MvpView
import moxy.viewstate.strategy.alias.AddToEndSingle

interface SignUpView : MvpView {
    @AddToEndSingle
    fun showEmptyFieldError()
}