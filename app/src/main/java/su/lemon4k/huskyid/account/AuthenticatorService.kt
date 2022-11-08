package su.lemon4k.huskyid.account

import android.app.Service
import android.content.Intent
import android.os.IBinder

class AuthenticatorService : Service() {

    private lateinit var mAuthenticator: Authenticator

    override fun onCreate() {
        super.onCreate()
        mAuthenticator = Authenticator(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mAuthenticator.iBinder
    }
}