package exam.app.layout

import android.os.Bundle
import android.support.v4.app.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.App
import exam.app.Entity.Friend
import exam.app.Entity.User
import exam.app.R
import exam.app.database.DBController
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import org.jetbrains.anko.onClick


class AMLogin : Fragment() {
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
           // (activity as ActivityMain).firebaseLogin(displayName, email, password, phonenumber)

            App.instance.listOfFriends = listOf<Friend>(Friend("Phillip", "ms@ms.dk", 22222222), Friend("Daniel", null, 22334455), Friend("Hazem", "hm@hm.dk", null))
            Log.d(TAG, App.instance.listOfFriends.get(0).email)
            (activity as ActivityMain).showOverview()
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment

    }
}


