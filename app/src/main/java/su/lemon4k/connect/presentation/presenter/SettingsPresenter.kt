package su.lemon4k.connect.presentation.presenter

import moxy.InjectViewState
import moxy.MvpPresenter
import su.lemon4k.connect.ui.views.SettingsView

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }
}