package app

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import database.DBController
import layout.AMChat
import layout.AMLogin
import layout.AMNewMessage
import layout.AMOverview
import org.jetbrains.anko.toast

class ActivityMain : FragmentActivity() {
    val amlogin = AMLogin()
    val amoverview = AMOverview()
    val amchat = AMChat()
    val amnewmessage = AMNewMessage()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = DBController.instance.writableDatabase

        if (savedInstanceState != null) return // already instantiated
        amlogin.arguments = intent.extras // arguments exist

        //Closes the connection
        db.close()

        //Here we add all of our fragmets and decide which one to show.
        supportFragmentManager
                .beginTransaction()
                // .add takes the fragment and makes it so make sure all fragments are here
                //!!!LÆS OP!!!
                .add(R.id.fragment_container, amlogin)
                .add(R.id.fragment_container, amoverview)
                .add(R.id.fragment_container, amchat)
                .add(R.id.fragment_container, amnewmessage)
                /**
                 * Show and hide fragments
                 * .hide hides all the fragments we dont want to show right now.
                 * amlogin should be the one that we are starting on.
                 */
                .hide(amoverview)
                .hide(amchat)
                .hide(amnewmessage)
                .commit()
    }
        // Function that show the amoverview fragment
            //Hvis tid kig på at bruge KeyStore.PasswordProtection
    fun showOverview(username : String, password : String){
            if (DBController.instance.getUser(username, password)) {
                supportFragmentManager
                        .beginTransaction()
                        .show(amoverview)
                        .hide(amlogin)
                        .commit()
            } else {
                toast("Wrong username or password please try agian")
            }

    }
        // Function that show the amlogin fragment
    fun showLogin(){
        supportFragmentManager
                .beginTransaction()
                .show(amlogin)
                .hide(amoverview)
                .commit()
    }

    // Function that show the amchat fragment
    fun showChat(){
        supportFragmentManager
                .beginTransaction()
                .show(amchat)
                .hide(amoverview)
                .commit()
    }

    // Function that show the ammewmessage fragment
    fun showNewMessage(){
        supportFragmentManager
                .beginTransaction()
                .show(amnewmessage)
                .hide(amoverview)
                .commit()
    }


}
