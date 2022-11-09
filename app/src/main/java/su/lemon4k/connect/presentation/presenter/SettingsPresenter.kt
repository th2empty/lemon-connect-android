package su.lemon4k.connect.presentation.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import su.lemon4k.connect.ui.views.SettingsView

@InjectViewState
class SettingsPresenter : MvpPresenter<SettingsView>() {

    companion object {
        private val TAG = this::class.java.name.split('.').last()
    }
}