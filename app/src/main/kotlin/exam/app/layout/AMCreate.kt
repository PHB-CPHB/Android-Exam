package exam.app.layout


import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseUser
import exam.app.ActivityMain

import exam.app.R
import exam.app.Validation
import exam.app.rest.APIController
import exam.app.rest.ServiceVolley
import kotlinx.android.synthetic.main.fragment_am_create.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class AMCreate : Fragment() {
    val TAG = "AMCreate"

    override fun onCreateView(
            inflater: LayoutInflater?,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        /*
        Inflate the layout for this fragment
            Takes the XML code and makes the view.
            ! All buttons should be with fragment
            EX. fragment.BUTTONNAME
         */
        val fragment = inflater!!.inflate(R.layout.fragment_am_create, container, false)

        fragment.switch_login.onClick {
            (activity as ActivityMain).showLogin()
        }

        /**
         *  Create button
         *  Gets Username, email, Phonenumber and Password.
         *  Connects to firebase and create, validates and login the user.
         *  Shows the overview.
         */
        fragment.create_user.onClick {
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
            } else if (!Validation.validateEmail(email)){
                fragment.email_field.setError("Please enter a valied email")
            } else if (!Validation.validatePhonenumber(phonenumber)){
                fragment.phone_number_field.setError("Please enter a Phonenumber")
            } else if (password.trim().equals("")) {
                fragment.password_field.setError("Please enter a Password")
            } else {
                Log.d(TAG, "Creating user")
                (activity as ActivityMain).register(displayName, email, password, phonenumber)
            }
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment

    }
}


