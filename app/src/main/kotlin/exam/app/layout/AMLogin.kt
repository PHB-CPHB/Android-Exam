package exam.app.layout

import android.os.Bundle
import android.support.v4.app.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.App
import exam.app.R
import exam.app.rest.APIController
import exam.app.rest.ServiceVolley
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject


class AMLogin : Fragment() {

    val service = ServiceVolley()
    val apiController = APIController(service)
    val TAG = "AMLogin"
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

        //
        fragment.login_button.onClick {
            //Gets Username
            val username = fragment.username_field.text.toString()
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
            //TODO: Make method to only create user
            if((activity as ActivityMain).authenticate(email, password)) {
                updateToken(email, App.instance.regToken!!, phonenumber)
                (activity as ActivityMain).showOverview(username, email, phonenumber)
            } else {
                createUser(email, password, phonenumber, username)
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

        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }

    fun updateToken(email : String, token : String, phone : String){
        val path = "/updatetoken"
        val params = JSONObject()
        params.put("email", email)
        params.put("token", token)
        params.put("phone", phone)

        apiController.post(path, params) { response ->
            Log.d(TAG, response.toString())
        }
    }
}


