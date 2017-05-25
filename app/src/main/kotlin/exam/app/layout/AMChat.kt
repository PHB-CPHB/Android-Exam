package exam.app.layout

import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import exam.app.App
import exam.app.R
import kotlinx.android.synthetic.main.fragment_am_chat.*
import kotlinx.android.synthetic.main.fragment_am_chat.view.*
import org.jetbrains.anko.onClick

class AMChat : Fragment() {
    val TAG : String = "AMChat"
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
        fragment.sendMSGChat.onClick {
            onSendClick(inputMSGChat.text.toString(), recieverChat.text.toString())
        }
        /**
         * This is that last thing that should happen in the fragment.
         * This where it actually returns the view
         */
        return fragment
    }
    var smsManager : SmsManager = SmsManager.getDefault()

    fun onSendClick(inputString : String, recieverString : String) {
        var input: String = inputString
        var reciever: String = recieverString
        if (ContextCompat.checkSelfPermission(App.instance, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            //getPermissionToReadSMS()
        } else {
            if (input == null && reciever == null || input == null && reciever == "" || input == "" && reciever == null) {
                return Toast.makeText(App.instance, "OOppps something went wrong", Toast.LENGTH_SHORT).show()
            } else {
                smsManager.sendTextMessage(reciever, null, input, null, null)
                Toast.makeText(App.instance, "Message sent!", Toast.LENGTH_SHORT).show()

            }
        }
    }
}