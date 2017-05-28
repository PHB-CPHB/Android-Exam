package exam.app.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.R
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_BACK
import exam.app.ActivityMain


class AMNewMessage : Fragment() {
    val TAG = "AMNewMessage"
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
        val fragment = inflater.inflate(R.layout.fragment_am_new_message, container, false)
        
        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }



    fun onBackPressed() {
        Log.d("CDA", "onBackPressed Called")
        val setIntent = Intent(Intent.ACTION_MAIN)
        setIntent.addCategory(Intent.CATEGORY_HOME)
        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
    }
}