package mobi.hsz.idea.nodesecurity.components

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import mobi.hsz.idea.nodesecurity.models.Advisory

@State(name = "NodeSecuritySettings", storages = [Storage(id = "other", file = "\$APP_CONFIG$/nodeSecurity.xml")])
class NodeSecuritySettings : PersistentStateComponent<NodeSecuritySettings.State> {
    data class State(
            var advisories: Map<String, List<Advisory>> = emptyMap(),
            var version: String? = null
    )

    private var state: State = State()

    override fun getState(): State = state

    override fun loadState(state: State) {
        this.state = state
    }

    companion object {
        fun getInstance(): NodeSecuritySettings =
                ServiceManager.getService<NodeSecuritySettings>(NodeSecuritySettings::class.java)
    }
}
