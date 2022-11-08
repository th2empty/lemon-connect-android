package su.lemon4k.huskyid.ui.views

interface SignInView : LoadingView {
    fun showEmptyFieldError()

    fun showAuthError(message: String, t: Throwable? = null)
}