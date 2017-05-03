package app

import android.os.Bundle
import android.support.v4.app.FragmentActivity

class ActivityMain : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return // already instantiated
        val amlogin = AMLogin()
        amlogin.arguments = intent.extras // arguments exist

        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, amlogin)
                .commit()

    }


}
