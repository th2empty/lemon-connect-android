package su.lemon4k.connect.ui.views

interface SignInView : LoadingView {
    fun showEmptyFieldError()

    fun showAuthError(message: String, t: Throwable? = null)
}