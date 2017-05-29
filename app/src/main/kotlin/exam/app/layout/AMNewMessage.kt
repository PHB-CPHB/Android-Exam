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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        // Make sure it's our original READ_CONTACTS request
        if (requestCode == READ_SMS_PERMISSIONS_REQUEST) {
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(App.instance, "Read SMS permission granted", Toast.LENGTH_SHORT).show()

            } else {
                Toast.makeText(App.instance, "Read SMS permission denied", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendClick(input : String, reciever : String) {

        //var friendList : List<Friend> = App.instance.listOfFriends

        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS()
        } else {
            if(Validation.validateEmail(reciever)){

                //Send online message
                Log.d(TAG, reciever)
                APIService.getMatchedFriend(reciever, null)
                Log.d(TAG, "Friend found - sending message")
                APIService.sendMessage(App.instance.user!!.displayName, reciever, input)
                DBController.instance.insertMessage(Message(reciever, "", input, Status.SENT))
                showOverview(email = reciever)
            } else if (Validation.validatePhonenumber(reciever)) {
                Log.d(TAG, "Looks like a phone number")
                APIService.getMatchedFriend(null, reciever)
                smsManager.sendTextMessage(reciever, null, input, null, null)
                DBController.instance.insertFriend(Friend(reciever, null, reciever))
                DBController.instance.insertMessage(Message("", reciever, input, Status.SENT))
                Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()
                showOverview(phone = reciever)
            } else {
                Toast.makeText(App.instance, "We didn't recognize the receiver. Try with a valid email or phone number", Toast.LENGTH_SHORT)
            }

        }
    }

    fun showOverview(email : String = "", phone : String = ""){
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
                (activity as ActivityMain).showChat(friend)
            } else {
                Toast.makeText(App.instance, "No user registered with that email address", Toast.LENGTH_SHORT)
            }
        }
    }


}