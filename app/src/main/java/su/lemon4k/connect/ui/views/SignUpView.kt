package su.lemon4k.connect.ui.views

interface SignUpView : LoadingView {
    fun showEmptyFieldError()

    fun showAlreadyRegisteredError()
}