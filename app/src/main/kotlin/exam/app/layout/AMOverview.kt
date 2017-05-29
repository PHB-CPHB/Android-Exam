package exam.app.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.ActivityMain
import exam.app.App
import exam.app.Entity.Friend
import exam.app.R
import exam.app.database.DBController
import kotlinx.android.synthetic.main.fragment_am_overview.*
import kotlinx.android.synthetic.main.fragment_am_overview.view.*
import kotlinx.android.synthetic.main.friend_in_overview_list.view.*
import org.jetbrains.anko.onClick

class AMOverview : Fragment() {
    val TAG = "AMOverview"
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View?
    {
        App.instance.listOfFriends = DBController.instance.getFriends()
        Log.d(TAG, "CREATE")
        /**
         *  Inflate the layout for this fragment
         *  Takes the XML code and makes the view.
         *  ! All buttons should be with fragment
         *  EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_overview, container, false)

        /**
         *  New Message button
         *  Shows the new message fragment
         */
        fragment.new_message_button.onClick {
            (activity as ActivityMain).showNewMessage()
        }

        fragment.friend_list.layoutManager = LinearLayoutManager(this.context)
        fragment.friend_list.adapter = FriendsList(App.instance.listOfFriends, this)

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }

    fun onFriendListClick(v: View) {
        val friend = v.tag as Friend

        (activity as ActivityMain).showChat(friend)
    }

    override fun onResume() {
        Log.d(TAG, "RESUME")
        var friends = DBController.instance.getFriends()
        friends.forEach {
            Log.d(TAG, it.email)
        }
        super.onResume()
        //this.friend_list.adapter = FriendsList(App.instance.listOfFriends, this)
        if (App.instance.user == null) overview_label.text = "No user!!!"
        else {
            overview_label.text = App.instance.user?.displayName
        }

    }

}
