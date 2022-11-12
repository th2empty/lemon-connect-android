package su.lemon4k.connect.ui.views

import moxy.viewstate.strategy.alias.AddToEndSingle
import moxy.viewstate.strategy.alias.Skip

interface SignInView : LoadingView {
    @AddToEndSingle
    fun init()

    @AddToEndSingle
    fun showEmptyFieldError()

    @AddToEndSingle
    fun showAuthError(message: String, t: Throwable? = null)
}