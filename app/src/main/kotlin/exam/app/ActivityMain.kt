package exam.app

import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.content.ContextCompat
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import exam.app.Entity.Friend
import exam.app.Entity.Message
import exam.app.Entity.Status
import exam.app.Entity.User
import exam.app.database.DBController
import exam.app.layout.*
import exam.app.rest.APIService
import exam.app.rest.APIService.Companion.updateToken
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val db = DBController.instance.writableDatabase
        App.instance.activityInstance = this
        if (savedInstanceState != null) return // already instantiated
        amlogin.arguments = intent.extras // arguments exist

        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS()
        }
        db.close()

        //Here we add all of our fragmets and decide which one to show.
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, amlogin)
                .add(R.id.fragment_container, amoverview)
                .add(R.id.fragment_container, amchat)
                .add(R.id.fragment_container, amnewmessage)
                .add(R.id.fragment_container, amcreate)
                /**
                 * Show and hide fragments
                 * Detaches stops the fragment and attaches runs create and resume
                 * amlogin should be the one that we are starting on.
                 */
                .detach(amoverview)
                .detach(amchat)
                .detach(amnewmessage)
                .detach(amcreate)
                .detach(amlogin)
                .commit()

        /**
         * Set the Token so we can receive messages from firebase
         */
        var refreshedToken : String? = FirebaseInstanceId.getInstance().token
        val user = DBController.instance.getUser()
        Log.d(ContentValues.TAG, "Refreshed token: " + refreshedToken)
        App.instance.regToken = refreshedToken

        // Automatic login
        if(!user.email.isNullOrEmpty() && !user.password.isNullOrEmpty()) {
            firebaseLogin(user.email, user.password)
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .attach(amlogin)
                    .commit()
        }
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
                        .detach(amcreate)
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
                .attach(amchat)
                .addToBackStack("tag")
                .detach(amoverview)
                .commit()
    }

    fun newMsgToChat(friend : Friend){
        amchat.friend = friend
        supportFragmentManager
                .beginTransaction()
                .attach(amchat)
                .addToBackStack("tag")
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
                .detach(amoverview)
                .addToBackStack("tag")
                .commit()
    }

    fun refreshChat(friend : Friend){
        if (!amchat.isDetached) {
            amchat.friend = friend
            supportFragmentManager
                    .beginTransaction()
                    .detach(amchat)
                    .attach(amchat)
                    .commit()
        }
    }

    fun refreshOverview(){
        if (!amoverview.isDetached) {
            supportFragmentManager
                    .beginTransaction()
                    .detach(amoverview)
                    .attach(amoverview)
                    .commit()
        }
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
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) {
            task ->
                if(task.isSuccessful){
                    Log.d(TAG, "signInWithEmail:success")
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
                if (!response.has("error")) {
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
            if (response != null && response.has("error")){
                Log.d(APIService.TAG, response.toString())
            } else {
                Log.d(APIService.TAG, "Logging in the user")
                firebaseLogin(email, password)
            }

        }
    }

    fun updateInbox(message : String, phone: String){
        val friend : Friend = DBController.instance.getFriendByPhone(phone)

        //TODO: Missing match friend on server
        DBController.instance.insertMessage(Message(
                friendEmail = "",
                friendPhone = phone,
                message = message,
                status = Status.RECEIVED
        ))

        if (friend.displayname.isNullOrEmpty()){
            DBController.instance.insertFriend(Friend(phone, "", phone))
            refreshOverview()
        } else {
            refreshChat(friend)
        }
    }

    private val READ_SMS_PERMISSIONS_REQUEST = 1

    fun getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            shouldShowRequestPermissionRationale(android.Manifest.permission.READ_SMS)
            requestPermissions(arrayOf<String>(android.Manifest.permission.READ_SMS),
                    READ_SMS_PERMISSIONS_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {

        if (requestCode != READ_SMS_PERMISSIONS_REQUEST)
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}
