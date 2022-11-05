package su.lemon4k.huskyid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import su.lemon4k.huskyid.model.HuskyUser
import su.lemon4k.huskyid.model.webservice.UserCallback
import su.lemon4k.huskyid.model.webservice.SignInInput

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
        this.finish()
    }

    private fun test() {
        val user = HuskyUser(null, null, null, null)
        try {
            user.signIn(SignInInput("lion", "lion#admin1"), object : UserCallback {
                override fun onResponse(response: Any) {
                    Toast.makeText(this@MainActivity, "You successfully signed in", Toast.LENGTH_LONG).show()
                }

                override fun onFailure(t: Throwable) {
                    Toast.makeText(this@MainActivity, t.message, Toast.LENGTH_LONG).show()
                }

            })
        } catch (ex: Exception) {
            ex.message?.let { Log.e(this::class.java.name.split('.').last(), it) }
            Toast.makeText(this, ex.message, Toast.LENGTH_LONG).show()
        }
    }
}