package app

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import layout.AMChat
import layout.AMLogin
import layout.AMNewMessage
import layout.AMOwerview

class ActivityMain : FragmentActivity() {
    val amlogin = AMLogin()
    val amchat = AMOwerview()
    val amchat = AMChat()
    val amnewmessage = AMNewMessage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) return // already instantiated
        amlogin.arguments = intent.extras // arguments exist

        //Here we add all of our fragmets and decide which one to show.
        supportFragmentManager
                .beginTransaction()
                // .add takes the fragment and makes it so make sure all fragments are here
                //!!!LÆS OP!!!
                .add(R.id.fragment_container, amlogin)
                .add(R.id.fragment_container, amchat)
                .add(R.id.fragment_container, amchat)
                .add(R.id.fragment_container, amnewmessage)
                /**
                 * Show and hide fragments
                 * .hide hides all the fragments we dont want to show right now.
                 * amlogin should be the one that we are starting on.
                 */
                .hide(amchat)
                .hide(amchat)
                .hide(amnewmessage)
                .commit()
    }
        // Function that show the amessage fragment
    fun showMessage(){
        supportFragmentManager
                .beginTransaction()
                .show(amchat)
                .hide(amlogin)
                .commit()
    }
        // Function that show the amlogin fragment
    fun showLogin(){
        supportFragmentManager
                .beginTransaction()
                .show(amlogin)
                .hide(amchat)
                .commit()
    }

    // Function that show the amlogin fragment
    fun showChat(){
        supportFragmentManager
                .beginTransaction()
                .show(amchat)
                .hide(amchat)
                .commit()
    }

    // Function that show the amlogin fragment
    fun showNewMessage(){
        supportFragmentManager
                .beginTransaction()
                .show(amnewmessage)
                .hide(amchat)
                .commit()
    }


}
