package firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

/**
 * Created by Mikkel on 14/05/2017.
 */
class TokenProvider : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        var refreshedToken : String? = FirebaseInstanceId.getInstance().getToken()
        Log.d(TAG, "Refreshed token: " + refreshedToken)

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.

        //sendRegistrationToServer(refreshedToken)

    }
}