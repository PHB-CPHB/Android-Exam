package exam.app.rest

import org.json.JSONObject

/**
 * Created by Mikkel on 25/05/2017.
 */
interface ServiceInterface {
    fun post(path : String, params : JSONObject, completionHandler : (response: JSONObject?) -> Unit)
}