package exam.app.rest

import android.util.Log
import android.widget.Toast
import exam.app.ActivityMain
import exam.app.App
import exam.app.Entity.Friend
import exam.app.Entity.User
import exam.app.database.DBController
import org.json.JSONObject

class APIService {
    companion object {
        val service = ServiceVolley()
        val apiController = APIController(service)
        val TAG : String = "APIService"

        fun sendMessage(from : String, to : String, message : String) {
            val path = "/send"
            val params = JSONObject()
            params.put("to", to)
            params.put("from", from)
            params.put("msg", message)
            //TODO: What has to happen with the returned data?
            apiController.post(path, params) { response ->
                Log.d(TAG, response.toString())
            }
        }

        fun getMatchedFriend(email : String?, phone : String?) {
            val path = "/match"
            val params = JSONObject()
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
                } else {
                    Toast.makeText(App.instance, "No user registered with that email address", Toast.LENGTH_SHORT)
                }
            }
        }

        fun updateToken(email : String, token : String, phone : String){
            val path = "/updateToken"
            val params = JSONObject()
            params.put("email", email)
            params.put("token", token)
            params.put("phone", phone)

            APIService.apiController.post(path, params) { response ->
                Log.d(APIService.TAG, response.toString())
            }
        }

    }
}
