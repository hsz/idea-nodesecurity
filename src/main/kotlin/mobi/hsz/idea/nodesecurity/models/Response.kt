package mobi.hsz.idea.nodesecurity.models

import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Response(
        val total: Int = 0,
        val count: Int = 0,
        val offset: Int = 0,
        val results: List<Advisory> = emptyList()
) {
    class Deserializer : ResponseDeserializable<Response> {
        override fun deserialize(content: String) = Gson().fromJson(content, Response::class.java)!!
    }
}
