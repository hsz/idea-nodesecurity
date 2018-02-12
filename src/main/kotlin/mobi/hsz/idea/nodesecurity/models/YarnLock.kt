package mobi.hsz.idea.nodesecurity.models

data class YarnLock(
        var name: String = "",
        var version: String = "",
        var resolved: String = "",
        var dependencies: Map<String, String> = emptyMap(),
        var optionalDependencies: Map<String, String> = emptyMap()
)
