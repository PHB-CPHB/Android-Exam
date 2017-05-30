package exam.app

import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.iid.FirebaseInstanceId
import exam.app.Entity.Friend
import exam.app.Entity.User
import exam.app.database.DBController
import exam.app.layout.*
import exam.app.rest.APIController
import exam.app.rest.APIService
import exam.app.rest.APIService.Companion.updateToken
import exam.app.rest.ServiceVolley
import org.jetbrains.anko.toast
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
    val amcreate = AMCreate()
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
                .add(R.id.fragment_container, amcreate)
                /**
                 * Show and hide fragments
                 * .hide hides all the fragments we dont want to show right now.
                 * amlogin should be the one that we are starting on.
                 */
                .detach(amoverview)
                .detach(amchat)
                .detach(amnewmessage)
                .detach(amcreate)
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
                        .attach(amoverview).addToBackStack("tag")
                        .detach(amlogin)
                        .detach(amnewmessage)
                        .commit()

    }
    /**
     * Function that shows Login
     */
    fun showLogin(){
        supportFragmentManager
                .beginTransaction()
                .attach(amlogin)
                .detach(amcreate)
                .commit()
    }
    /**
     * Function that shows create
     */
    fun showCreate() {
        supportFragmentManager
                .beginTransaction()
                .attach(amcreate)
                .detach(amlogin)
                .commit()
    }
    /**
     * Function that shows Chat
     */
    fun showChat(friend : Friend){
        Log.d(TAG, "Showing chat!")
        amchat.friend = friend
        supportFragmentManager
                .beginTransaction()
                .attach(amchat).addToBackStack("tag")
                .detach(amoverview)
                .detach(amnewmessage)
                .commit()
    }

    fun newMsgToChat(friend : Friend){
        amchat.friend = friend
        supportFragmentManager
                .beginTransaction()
                .attach(amchat).addToBackStack("tag")
                .detach(amnewmessage)
                .commit()
    }
    /**
     * Function that shows New Message
     */
    fun showNewMessage(){
        supportFragmentManager
                .beginTransaction()
                .attach(amnewmessage)
                .detach(amoverview).addToBackStack("tag")
                .commit()
    }

    fun refreshChat(friend : Friend){
        amchat.friend = friend
        supportFragmentManager.beginTransaction()
                .detach(amchat)
                .attach(amchat)
                .commit()
    }


    fun register(displayName: String, email: String, password: String, phoneNumber: String) {
        Log.d(TAG, "Registering at Firebase")
        val mAuth = FirebaseAuth.getInstance()
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
            if(task.isSuccessful){
                toast("This user is already registered")
                Log.d(TAG, "User already exist")
            } else {
                Log.d(TAG, "User created")
                createUser(email, password, phoneNumber, displayName)

            }
        }
    }

    /**
     * This is where we login in to firebase and to the app.
     * @param email : String
     * @param password : String
     */
    fun firebaseLogin(email : String, password : String) {
        val mAuth = FirebaseAuth.getInstance()
        var firebaseUser : FirebaseUser? = null
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
                    firebaseUser = mAuth.currentUser!!
                    Log.d(TAG, "User exists, matching...")
                    matchUser(email, password)
                } else {
                    Log.d(TAG, "User not found in firebase")
                    toast("Wrong E-Mail or Password")
                }
        }

    }
    // Looks in our remote DB to see if a user exists after being authenticated.
    // If it does, we will insert it into our local DB
    fun matchUser(email : String?, password: String) {
        val path = "/match"
        val params = JSONObject()

            params.put("email", email)

        APIService.apiController.post(path, params) {
            response ->
            Log.d(TAG, "waiting for response")
            if(response !=  null) {
                if (!response!!.has("error")) {
                    Log.d(TAG, "Have user")
                    var user: User = User(
                            email = response.getString("email"),
                            displayName = response.getString("displayName"),
                            phonenumber = response.getString("phone"),
                            password = password
                    )
                    DBController.instance.clearUserTable()
                    DBController.instance.insertUser(user)
                    App.instance.user = user
                    Log.d(APIService.TAG, App.instance.regToken!!)
                    App.instance.listOfFriends = DBController.instance.getFriends()
                    updateToken(user.email, App.instance.regToken!!, user.phonenumber)
                    Log.d(TAG, "Showing overview")
                    showOverview()
                }
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

        APIService.apiController.post(path, params) { response ->
            if (response!!.has("error")){
                Log.d(APIService.TAG, response.toString())
            } else {
                Log.d(APIService.TAG, "Logging in the user")
                firebaseLogin(email, password)
            }

        }
    }


}
