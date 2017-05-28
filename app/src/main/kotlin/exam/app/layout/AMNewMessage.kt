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
import exam.app.Entity.Friend
import exam.app.database.DBController
import exam.app.rest.APIController
import exam.app.rest.ServiceVolley
import kotlinx.android.synthetic.main.fragment_am_new_message.view.*
import org.jetbrains.anko.onClick
import org.json.JSONObject


class AMNewMessage : Fragment() {
    val TAG = "AMNewMessage"
    val service = ServiceVolley()
    val apiController = APIController(service)
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

        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            getPermissionToReadSMS()
        } else {
            if(Validation.validateEmail(reciever)){
                //Send online message
                APIService.matchFriend(reciever, null)
                APIService.sendMessage(App.instance.user?.displayName, reciever, input)
                //TODO: Save message in DB
            } else if (Validation.validatePhonenumber(reciever)) {
                APIService.matchFriend(null, reciever)
                smsManager.sendTextMessage(reciever, null, input, null, null)
                Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(App.instance, "We didn't recognize the receiver. Try with a valid email or phone number", Toast.LENGTH_SHORT)
            }

        }
    }

    fun getMatchedFriend(phone : String?, email : String?) : Friend? {
        val path = "/match"
        val params = JSONObject()
        var friend : Friend? = null
        if (!email.isNullOrEmpty()){
            params.put("email", email)
        } else {
            params.put("phone", phone)
        }

        apiController.post(path, params) { response ->
            if (!response!!.has("error")) {
                 var friend = Friend(
                        displayname = response.getString("displayName"),
                        email = response.getString("email"),
                        phonenumber = response.getString("phone")

                )
                DBController.instance.insertFriend(friend)
            }
        }
        return friend
    }
}