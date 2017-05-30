package exam.app.layout

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.Entity.Message
import exam.app.Entity.Status
import exam.app.R
import kotlinx.android.synthetic.main.message_recycle_view.view.*

class MessageViewAdapter (val messages: List<Message>, val amc: AMChat) :
        RecyclerView.Adapter<MessageViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : ViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.message_recycle_view, parent , false)
        val viewHolder = ViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val message = messages[position]
        holder.view.tag = message
       if (message.status == Status.RECEIVED){
           holder.view.message_layout.setBackgroundResource(R.drawable.shape_message_received)
           holder.view.message.text = message.message
        } else {
           holder.view.setBackgroundResource(R.drawable.shape_message_send)
           holder.view.message.text = message.message
        }

    }

    override fun getItemCount(): Int = messages.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)
}
