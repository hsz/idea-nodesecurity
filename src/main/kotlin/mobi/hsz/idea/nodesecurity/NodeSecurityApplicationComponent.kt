package mobi.hsz.idea.nodesecurity

import com.intellij.openapi.components.ApplicationComponent
import mobi.hsz.idea.nodesecurity.components.NodeSecuritySettings
import mobi.hsz.idea.nodesecurity.utils.ApiService
import nl.komponents.kovenant.then


class NodeSecurityApplicationComponent : ApplicationComponent {
    override fun getComponentName(): String = "NodeSecurityApplicationComponent"

    override fun disposeComponent() {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var settings: NodeSecuritySettings = NodeSecuritySettings.getInstance()

    override fun initComponent() {
        ApiService.getAdvisories() then {
            settings.state.advisories = it
            settings.loadState(settings.state)
        }
    }
}
