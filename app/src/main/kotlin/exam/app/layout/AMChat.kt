package exam.app.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.Entity.Friend
import exam.app.R

class AMChat : Fragment() {
    var friend: Friend? = null
    val TAG = "AMChat"
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
        val fragment = inflater.inflate(R.layout.fragment_am_chat, container, false)


        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }
}