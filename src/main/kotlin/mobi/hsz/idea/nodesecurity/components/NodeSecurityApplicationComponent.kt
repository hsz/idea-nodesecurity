package mobi.hsz.idea.nodesecurity.components

import com.intellij.openapi.components.ApplicationComponent
import mobi.hsz.idea.nodesecurity.utils.ApiService
import nl.komponents.kovenant.then

class NodeSecurityApplicationComponent : ApplicationComponent {
    private var settings: NodeSecuritySettings = NodeSecuritySettings.getInstance()

    override fun getComponentName(): String = "NodeSecurityApplicationComponent"

    override fun initComponent() {
        ApiService.getAdvisories() then {
            settings.state.advisories = it
            settings.loadState(settings.state)
        }
    }
}
