package exam.app

import android.app.Activity
import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import exam.app.Entity.Friend
import exam.app.Entity.User
import exam.app.database.DBController
import exam.app.layout.AMChat
import exam.app.layout.AMLogin
import exam.app.layout.AMNewMessage
import exam.app.layout.AMOverview
import exam.app.rest.APIController
import exam.app.rest.ServiceVolley
import org.json.JSONObject

class ActivityMain : FragmentActivity() {
    /**
     * Make a TAG in every class.
     * It is used for logging and debugging.
     */
    val TAG = "ActivityMain"
    val amlogin = AMLogin()
    val amoverview = AMOverview()
    val amchat = AMChat()
    val amnewmessage = AMNewMessage()
    val service = ServiceVolley()
    val apiController = APIController(service)


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
                .detach(amoverview)
                .detach(amchat)
                .detach(amnewmessage)
                //.hide(amoverview)
                //.hide(amchat)
                //.hide(amnewmessage)
                .commit()
        var refreshedToken : String? = FirebaseInstanceId.getInstance().getToken()
        Log.d(ContentValues.TAG, "Refreshed token: " + refreshedToken)
        App.instance.regToken = refreshedToken
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressed()
        }
    }
        // Function that show the amoverview fragment
            //Hvis tid kig på at bruge KeyStore.PasswordProtection
    fun showOverview(){

                supportFragmentManager
                        .beginTransaction()
                        .attach(amoverview)
                        .detach(amlogin)
                        .commit()

    }
        // Function that show the amlogin fragment
    fun showLogin(){
        supportFragmentManager
                .beginTransaction()
                .attach(amlogin)
                .detach(amoverview)
                .commit()
    }

    // Function that show the amchat fragment
    fun showChat(friend : Friend){
        amchat.friend = friend
        supportFragmentManager
                .beginTransaction()
                .attach(amchat).addToBackStack("tag")
                .detach(amoverview)
                .commit()
    }

    // Function that show the ammewmessage fragment
    fun showNewMessage() {
        supportFragmentManager
                .beginTransaction()
                .attach(amnewmessage).addToBackStack("tag")
                .detach(amoverview)
                .commit()
    }

        fun showNewMessagetest(){
            supportFragmentManager
                    .beginTransaction()
                    .attach(amnewmessage)
                    .detach(amlogin)
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

    fun authenticate(email: String, password: String) : Boolean {
        val mAuth = FirebaseAuth.getInstance()
        var retVal : Boolean = false
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful){
                Log.d(TAG, "signInWithEmail:success")
                retVal = true
            } else {
                Log.e(TAG, "signInWithEmail:error")
            }
        }
        return retVal
    }

    /**
     * This is where we login in to firebase and to the app.
     * @param email : String
     * @param password : String
     * @param phonenumber : String
     */
    fun firebaseLogin(displayName : String, email : String, password : String, phonenumber : String) {
        val mAuth = FirebaseAuth.getInstance()
        var firebaseUser : FirebaseUser? = null
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    firebaseUser = mAuth.currentUser!!
                    Log.d(TAG, "User exists, matching...")
                    match(email, null, password)
                    App.instance.user = User(displayName, email, phonenumber, password)
                    App.instance.listOfFriends = DBController.instance.getFriends()
                    Log.d(TAG, App.instance.regToken!!)
                    updateToken(email, App.instance.regToken!!, phonenumber)
                    showOverview()
                } else {
                    //TODO: Kald Match og derefter Create på node serveren.
                    Log.d(TAG, "User not found in firebase, registering it now...")
                    createUser(email, password, phonenumber, displayName)
                    App.instance.user = User(displayName, email, phonenumber, password)
                    Log.e(TAG, "signInWithEmail:error")
                }
        }

    }
    fun updateToken(email : String, token : String, phone : String){
        val path = "/updateToken"
        val params = JSONObject()
        params.put("email", email)
        params.put("token", token)
        params.put("phone", phone)

        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }

    // Looks in our remote DB to see if a user exists after being authenticated.
    // If it does, we will insert it into our local DB
    fun match(email : String?, phone : String?, password: String){
        val path = "/match"
        val params = JSONObject()
        if (!email.isNullOrEmpty()){
            params.put("email", email)
        } else {
            params.put("phone", phone)
        }

        apiController.post(path, params) { response ->
            if (!response!!.has("error")) {

                var user : User = User(
                        email = response.getString("email"),
                        displayName = response.getString("displayName"),
                        phonenumber = response.getString("phone"),
                        password = password
                )
                DBController.instance.clearUserTable()
                DBController.instance.insertUser(user)
            }
        }
    }

    fun createUser(email : String,
                   password : String,
                   phoneNumber : String,
                   displayName : String) {

        val path = "/register"
        val params = JSONObject()
        params.put("email", email)
        params.put("password", password)
        params.put("phone", phoneNumber)
        params.put("displayName", displayName)
        params.put("token", App.instance.regToken)

        apiController.post(path, params) { response ->
            if (response!!.has("error")){
                Log.d(TAG, response.toString())
            } else {
                Log.d(TAG, "Error ${response.toString()}")
            }

        }
    }


}
