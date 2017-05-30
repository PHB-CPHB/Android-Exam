package exam.app.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import exam.app.App

class TokenProvider : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        var refreshedToken : String? = FirebaseInstanceId.getInstance().getToken()
        Log.d(TAG, "Refreshed token: " + refreshedToken)
        App.instance.regToken = refreshedToken
    }
}