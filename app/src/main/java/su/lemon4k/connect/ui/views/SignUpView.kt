package su.lemon4k.connect.ui.views

interface SignUpView : LoadingView {
    fun init()

    fun showEmptyFieldError()

    fun showAlreadyRegisteredError()
}