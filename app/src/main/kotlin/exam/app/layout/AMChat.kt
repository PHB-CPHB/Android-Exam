package exam.app.layout

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import exam.app.ActivityMain
import exam.app.App
import exam.app.Entity.Friend
import exam.app.Entity.Message
import exam.app.Entity.Status
import exam.app.R
import exam.app.database.DBController
import exam.app.rest.APIService
import kotlinx.android.synthetic.main.fragment_am_chat.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject

class AMChat : Fragment() {

    val TAG : String = "AMChat"
    var friend: Friend? = null
    var messages: List<Message> = emptyList()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ):
            View? {
        /**
         *  Inflate the layout for this fragment
         *  Takes the XML code and makes the view.
         *  ! All buttons should be with fragment
         *  EX. fragment.BUTTONNAME
         */
        val fragment = inflater.inflate(R.layout.fragment_am_chat, container, false)
        messages = DBController.instance.getMessagesByFriend(friend!!.phonenumber!!)
        Log.d(TAG, friend!!.phonenumber)
        messages.forEach {
            fragment.messages.append(it.message)
        }

        fragment.friend_name.text = friend!!.displayname

        fragment.sendOnline.onClick {
            val msg = fragment.inputMSGChat.text.toString()
            onSendOnlineClick(msg, friend!!.email)
        }

        fragment.sendSMS.onClick {
            val msg = fragment.inputMSGChat.text.toString()
            onSendSMSClick(msg, friend!!.phonenumber)
        }

        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }

    fun onSendOnlineClick(message : String, receiver : String) {
        val path = "/send"
        val params = JSONObject()
        params.put("from", receiver)
        params.put("to", friend!!.email)
        params.put("msg", message)
        params.put("phone", App.instance.user!!.phonenumber)

        APIService.apiController.post(path, params) { response ->
            if(response != null) {
                Log.d(TAG, response.toString())
                if (!response!!.has("error")) {
                    DBController.instance.insertMessage(Message(friend!!.email, friend!!.phonenumber, message, Status.SENT))
                    (activity as ActivityMain).refreshChat(friend!!)
                } else {
                    Log.d(TAG, response.getString("error"))
                    Toast.makeText(App.instance, "No user registered with that email address", Toast.LENGTH_SHORT)
                }
            } else {
                Log.d(TAG, "Response is NULL")
            }
        }
    }


    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendSMSClick(message : String, receiver : String) {
        val smsManager : SmsManager = SmsManager.getDefault()
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS()
        } else {
            smsManager.sendTextMessage(receiver, null, message, null, null)
            DBController.instance.insertMessage(Message("", receiver, message, Status.SENT))
            (activity as ActivityMain).refreshChat(friend!!)
        }
    }

    private val READ_SMS_PERMISSIONS_REQUEST = 1

    fun getPermissionToReadSMS() {
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(
                    android.Manifest.permission.READ_SMS)) {
                Toast.makeText(App.instance, "Please allow permission!", Toast.LENGTH_SHORT).show()
            }
            requestPermissions(arrayOf<String>(android.Manifest.permission.READ_SMS),
                    READ_SMS_PERMISSIONS_REQUEST)
        }
    }


}