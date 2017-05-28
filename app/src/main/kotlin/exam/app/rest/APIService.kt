package exam.app.rest

import android.util.Log
import exam.app.App
import exam.app.Entity.User
import exam.app.database.DBController
import org.json.JSONObject

class APIService {
    companion object {
        val service = ServiceVolley()
        val apiController = APIController(service)
        val TAG : String = "APIService"

        fun sendMessage(from : String, to : String, message : String) {
            val path = "/"
            val params = JSONObject()
            params.put("to", to)
            params.put("from", from)
            params.put("message", message)
            //TODO: What has to happen with the returned data?
            apiController.post(path, params) { response ->
                Log.d(TAG, response.toString())
            }
        }

        }
    }

