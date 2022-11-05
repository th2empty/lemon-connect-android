package su.lemon4k.huskyid

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class SignInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val build = BuildConfig.VERSION_CODE
        val version = BuildConfig.VERSION_NAME
        val buildType = BuildConfig.BUILD_TYPE
        val appId = BuildConfig.APPLICATION_ID
    }
}