package exam.app.layout

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import exam.app.R
import android.widget.Toast
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import exam.app.App
import kotlinx.android.synthetic.main.fragment_am_new_message.*
import android.telephony.SmsManager
import android.util.Log
import exam.app.ActivityMain
import exam.app.Entity.Status
import exam.app.database.DBController
import exam.app.Entity.Friend
import exam.app.Entity.Message
import exam.app.Validation
import exam.app.rest.APIController
import exam.app.rest.APIService
import exam.app.rest.ServiceVolley
import kotlinx.android.synthetic.main.fragment_am_new_message.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject


class AMNewMessage : Fragment() {
    val TAG = "AMNewMessage"
    val service = ServiceVolley()
    val apiController = APIController(service)
    val dbController = DBController();
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
        //val fragmentChat = inflater.inflate(R.layout.fragment_am_chat, container, false)

        fragment.sendMSg.onClick {
            onSendClick(inputMSG.text.toString(), reciever.text.toString())
            //if(getMatchedFriend())
            //(activity as ActivityMain).showChat() IN PROGRESS
        }
        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */


        return fragment
    }

    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendClick(input : String, receiver: String) {

        //var friendList : List<Friend> = App.instance.listOfFriends

        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            (activity as ActivityMain).getPermissionToReadSMS()
        } else {
            if(Validation.validateEmail(receiver)){

                //Send online message
                Log.d(TAG, receiver)
                Log.d(TAG, "Friend found - sending message")
                APIService.sendMessage(App.instance.user!!.displayName, receiver, input)
                getMatchedFriend(receiver, "", input)

            } else if (Validation.validatePhonenumber(receiver)) {
                Log.d(TAG, "Looks like a phone number")
                APIService.getMatchedFriendSMS(receiver)
                smsManager.sendTextMessage(receiver, null, input, null, null)
                DBController.instance.insertMessage(Message("", receiver, input, Status.SENT))
                Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()
                (activity as ActivityMain).newMsgToChat(DBController.instance.getFriendByPhone(receiver))
            } else {
                Toast.makeText(App.instance, "We didn't recognize the receiver. Try with a valid email or phone number", Toast.LENGTH_SHORT)
            }
        }
    }

    fun showChat(email : String = "", phone : String = ""){
        val path = "/match"
        val params = JSONObject()
        if (!email.isNullOrEmpty()){
            params.put("email", email)
        } else {
            params.put("phone", phone)
        }

        APIService.apiController.post(path, params) { response ->
            if (!response!!.has("error")) {
                var friend = Friend(
                        displayname = response.getString("displayName"),
                        email = response.getString("email"),
                        phonenumber = response.getString("phone")
                )
                (activity as ActivityMain).newMsgToChat(friend)
            } else {
                Toast.makeText(App.instance, "No user registered with that email address", Toast.LENGTH_SHORT)
            }
        }
    }

    fun getMatchedFriend(email : String, phone : String, message : String) {
        val path = "/match"
        val params = JSONObject()
        if (!email.isNullOrEmpty()){
            params.put("email", email)
        } else {
            params.put("phone", phone)
        }

        APIService.apiController.post(path, params) { response ->
            if (!response!!.has("error")) {
                var friend = Friend(
                        displayname = response.getString("displayName"),
                        email = response.getString("email"),
                        phonenumber = response.getString("phone")
                )
                DBController.instance.insertFriend(friend)
                DBController.instance.insertMessage(Message(email, friend.phonenumber!!, message, Status.SENT))
                showChat(email, phone)
            } else {
                Toast.makeText(App.instance, "No user registered with that email address", Toast.LENGTH_SHORT)
            }
        }
    }


}