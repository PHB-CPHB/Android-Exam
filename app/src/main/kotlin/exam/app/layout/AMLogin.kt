package exam.app.layout

import android.os.Bundle
import android.support.v4.app.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import exam.app.ActivityMain
import exam.app.App
import exam.app.Entity.User
import exam.app.R
import exam.app.database.DBController
import exam.app.rest.APIController
import exam.app.rest.ServiceVolley
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern



class AMLogin : Fragment() {
    val TAG = "AMLogin"

    val service = ServiceVolley()
    val apiController = APIController(service)
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {

        /*
        Inflate the layout for this fragment
            Takes the XML code and makes the view.
            ! All buttons should be with fragment
            EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_login, container, false)

        /**
         *  Login button
         *  Gets Username, email, Phonenumber and Password.
         *  Connects to firebase and vaildates the user.
         *  Shows the overview.
         */

        fragment.login_button.onClick {
            //Gets Username
            val displayName = fragment.username_field.text.toString()
            //Gets E-mail
            val email = fragment.email_field.text.toString()
            //Gets Phonenumber
            val phonenumber = fragment.phone_number_field.text.toString()
            //Gets Password
            val password = fragment.password_field.text.toString()


            /**
             * Change view
             * Make sure to call the activity like this when changing view.
             * (activity as ActivityMain).FunctionName
             */

            if(displayName.trim().equals("")){
                fragment.username_field.setError("Please enter a Username")
            } else if (!validateEmail(email)){
                fragment.email_field.setError("Please enter a valied email")
            } else if (!validatePhonenumber(phonenumber)){
                fragment.phone_number_field.setError("Please enter a Phonenumber")
            } else if (password.trim().equals("")) {
                fragment.password_field.setError("Please enter a Password")
            } else {
                var firebaseUser : FirebaseUser? = (activity as ActivityMain).firebaseLogin(displayName, email, password, phonenumber)
                if (firebaseUser != null) {
                    match(email, null, password)
                    App.instance.user = User(displayName, email, phonenumber, password)
                    App.instance.listOfFriends = DBController.instance.getFriends()
                    updateToken(email, App.instance.regToken!!, phonenumber)
                    (activity as ActivityMain).showOverview()
                }else {
                    createUser(email, password, phonenumber, displayName)
                    App.instance.user = User(displayName, email, phonenumber, password)
                }
            }
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment

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
                DBController.instance.insertUser(user)
            }
        }
    }


    fun sendMessage(from : String, to : String, message : String) {
        val path = "/"
        val params = JSONObject()
        params.put("to", to)
        params.put("from", from)
        params.put("message", message)
        //TODO: What has to happen with the returned data?
        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }

    fun validateEmail(email : String) : Boolean {
        val regex : String = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$"
        val pattern : Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher : Matcher = pattern.matcher(email)
        return matcher.matches()
    }

    fun validatePhonenumber(phonenumber : String) : Boolean {
        val regex : String = "^\\d{8}$"
        val pattern : Pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE)
        val matcher : Matcher = pattern.matcher(phonenumber)
        return matcher.matches()
    }
}


