package exam.app.rest

import org.json.JSONObject

/**
 * Created by Mikkel on 25/05/2017.
 */
class APIController  constructor(serviceInjection: ServiceInterface) : ServiceInterface {
    private val service: ServiceInterface = serviceInjection

    override fun post(path: String, params: JSONObject, completionHandler: (response: JSONObject?) -> Unit) {
        service.post(path, params, completionHandler)
    }
}