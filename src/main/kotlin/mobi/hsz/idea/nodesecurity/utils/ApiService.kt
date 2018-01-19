package mobi.hsz.idea.nodesecurity.utils

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.core.Request
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.intellij.util.containers.ContainerUtil
import mobi.hsz.idea.nodesecurity.models.Advisory
import mobi.hsz.idea.nodesecurity.models.Response
import nl.komponents.kovenant.Promise
import nl.komponents.kovenant.deferred
import nl.komponents.kovenant.functional.bind
import nl.komponents.kovenant.task
import nl.komponents.kovenant.then

class ApiService {
    companion object {
        private const val API_ENDPOINT = "https://api.nodesecurity.io"
        private fun advisoriesUrl(offset: Int) = "/advisories?offset=$offset"

        fun getAdvisories(advisories: MutableList<Advisory> = mutableListOf()): Promise<List<Advisory>, Exception> =
                ApiService().fetchAdvisories(advisories) then {
                    ArrayList<Advisory>(advisories)
                }
    }

    init {
        FuelManager.instance.basePath = API_ENDPOINT
    }

    private fun <T : Any> get(url: String, deserializer: ResponseDeserializable<T>): Promise<T, Exception> =
            url.httpGet().promise(deserializer)

    private fun fetchAdvisories(advisories: MutableList<Advisory>, offset: Int = 0): Promise<Response, Exception> =
            get(advisoriesUrl(offset), Response.Deserializer()) success {
                ContainerUtil.addAll(advisories, it.results)
            } bind {
                when {
                    it.count + it.offset < it.total -> fetchAdvisories(advisories, offset + 100)
                    else -> Promise.ofSuccess(it)
                }
            }
}

fun <T : Any> Request.promise(deserializer: ResponseDeserializable<T>): Promise<T, Exception> {
    val deferred = deferred<T, Exception>()
    task { responseObject(deserializer) } success { (_, _, result) ->
        when (result) {
            is Result.Success -> deferred.resolve(result.value)
            is Result.Failure -> deferred.reject(result.error)
        }
    } fail {
        deferred.reject(it)
    }
    return deferred.promise
}
