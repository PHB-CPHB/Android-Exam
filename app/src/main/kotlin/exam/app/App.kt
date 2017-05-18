package exam.app

import android.app.Application
import com.google.firebase.auth.FirebaseUser
import exam.app.Entity.User

class App : Application() {
    var firebase : FirebaseUser? = null

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}