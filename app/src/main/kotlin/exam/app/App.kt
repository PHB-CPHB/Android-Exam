package exam.app

import android.app.Application
import exam.app.Entity.Friend
import exam.app.Entity.User

class App : Application() {
    var user: User? = null
    var listOfFriends : List<Friend> = emptyList()

    companion object {
        lateinit var instance: App
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}