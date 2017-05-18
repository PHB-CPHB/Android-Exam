package exam.app.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.App
import exam.app.R
import kotlinx.android.synthetic.main.fragment_am_login.view.*
import kotlinx.android.synthetic.main.fragment_am_overview.view.*
import org.jetbrains.anko.listView
import org.jetbrains.anko.onClick

class AMOverview : Fragment() {
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?):
            View? {
        /**
         *  Inflate the layout for this fragment
         *  Takes the XML code and makes the view.
         *  ! All buttons should be with fragment
         *  EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_overview, container, false)

        //Set the username in overview
        //TODO Get the username from login
        fragment.username_field.setText(App.instance.firebase!!.displayName)

        /**
         *  New Message button
         *  Shows the new message fragment
         */
        fragment.new_message_button.onClick {
            (activity as ActivityMain).showNewMessage();
        }

        var users : MutableList<String> = mutableListOf("Mikkel", "Phillip", "Daniel", "Hazem")
        fragment.user_list.listView { users }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }
}
