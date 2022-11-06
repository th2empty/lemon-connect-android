package su.lemon4k.huskyid.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import su.lemon4k.huskyid.ui.views.MainView

@InjectViewState
class MainPresenter : MvpPresenter<MainView>() {

    fun isUserLoggedIn(): Boolean {
        return false
    }
}