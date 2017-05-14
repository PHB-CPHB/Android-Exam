package exam.app.layout

import android.os.Bundle
import android.support.v4.app.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.R
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import org.jetbrains.anko.onClick


class AMLogin : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {

        /* Inflate the layout for this fragment
            Takes the XML code and makes the view.
            ! All buttons should be with fragment
            EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_login, container, false)

        //
        fragment.login_button.onClick {
            //Gets Username
            val username = fragment.username_field.text.toString()
            //Gets Password
            val password = fragment.password_field.text.toString()
            /**
             * Change view
             * Make sure to call the activity like this when changing view.
             * (activity as ActivityMain).FunctionName
             */
            (activity as ActivityMain).showOverview(username, password);
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment

    }
}

