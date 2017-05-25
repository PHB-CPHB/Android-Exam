package exam.app.layout

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.Entity.Friend
import exam.app.R
import kotlinx.android.synthetic.main.friend_in_overview_list.view.*
import org.jetbrains.anko.onClick

class FriendsList (val friends: List<Friend>, val amo: AMOverview) :
        RecyclerView.Adapter<FriendsList.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.friend_in_overview_list, parent , false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val friend = friends[position]
            holder.view.tag = friend
            holder.view.friend_name.text = friend.displayname
            holder.view.onClick { amo.onFriendListClick(holder.view) }
    }

    override fun getItemCount(): Int = friends.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}