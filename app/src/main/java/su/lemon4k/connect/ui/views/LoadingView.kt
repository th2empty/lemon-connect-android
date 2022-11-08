package su.lemon4k.connect.ui.views

import com.arellomobile.mvp.MvpView

interface LoadingView : MvpView {
    fun showLoading()

    fun hideLoading()
}