package exam.app

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v7.app.AlertDialog
import android.telecom.Call
import android.util.Log
import exam.app.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesUtil
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import exam.app.database.DBController
import exam.app.layout.AMChat
import exam.app.layout.AMLogin
import exam.app.layout.AMNewMessage
import exam.app.layout.AMOwerview
import org.jetbrains.anko.toast

class ActivityMain : FragmentActivity() {
    val TAG = "ActivityMain"
    val amlogin = AMLogin()
    val amoverview = AMOwerview()
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

     fun isGooglePlayServicesAvailable(activity : Activity) : Boolean {
        var googleApiAvailability : GoogleApiAvailability = GoogleApiAvailability.getInstance()
        var status : Int = googleApiAvailability.isGooglePlayServicesAvailable(activity)
        if(status != ConnectionResult.SUCCESS) {
            if(googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show()
            }
            return false
        }
        return true
    }

    fun sendMessageToUser(user : String, message : String, email : String, password : String) {
        Log.d(TAG, "Send message to user called!!!")
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    var fireUser = mAuth.currentUser!!
                    dbCall(fireUser, user, message)
                } else {
                    Log.e(TAG, "signInWithEmail:error")
                }
        }

    }

    fun dbCall(fUser : FirebaseUser, user : String, message : String){
        val db = FirebaseDatabase.getInstance()
        var myRef = db.getReference("users")
        myRef.child(fUser.uid).setValue(message)
    }


}
