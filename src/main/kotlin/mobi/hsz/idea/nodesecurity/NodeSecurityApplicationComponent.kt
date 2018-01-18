package mobi.hsz.idea.nodesecurity

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.intellij.openapi.components.ApplicationComponent
import mobi.hsz.idea.nodesecurity.models.Response

class NodeSecurityApplicationComponent : ApplicationComponent {
    override fun getComponentName(): String = "NodeSecurityApplicationComponent"

    override fun disposeComponent() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initComponent() {
        FuelManager.instance.basePath = "https://api.nodesecurity.io"
        "/advisories".httpGet().responseObject(Response.Deserializer()) { req, res, result ->
            //result is of type Result<User, Exception>
            val (response, err) = result

            println("")
        }
    }
}
