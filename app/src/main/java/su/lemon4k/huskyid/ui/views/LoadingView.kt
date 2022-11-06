package su.lemon4k.huskyid.ui.views

import com.arellomobile.mvp.MvpView

interface LoadingView : MvpView {
    fun showLoading()

    fun hideLoading()
}