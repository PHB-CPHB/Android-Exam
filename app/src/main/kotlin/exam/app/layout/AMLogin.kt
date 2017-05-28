package exam.app.layout

import android.os.Bundle
import android.support.v4.app.*
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
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject
import java.util.regex.Matcher
import java.util.regex.Pattern



class AMLogin : Fragment() {
    val TAG = "AMLogin"

    var firebaseUser : FirebaseUser? = null

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

        fragment.switch_create.onClick {
            (activity as ActivityMain).showCreate()
        }

        /**
         *  Login button
         *  Gets email and password.
         *  Connects to firebase and vaildates the user.
         *  Shows the overview.
         */
        fragment.login_button.onClick {
            //Gets E-mail
            val email = fragment.email_field.text.toString()
            //Gets Password
            val password = fragment.password_field.text.toString()


            /**
             * Change view
             * Make sure to call the activity like this when changing view.
             * (activity as ActivityMain).FunctionName
             */

            if (!Validation.validateEmail(email)){
                fragment.email_field.setError("Please enter a valied email")
            } else if (password.trim().equals("")) {
                fragment.password_field.setError("Please enter a Password")
            } else {
                Log.d(TAG, "Logging in...")
                (activity as ActivityMain).firebaseLogin(email, password)
            }
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment

    }

}


