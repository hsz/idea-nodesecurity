package mobi.hsz.idea.nodesecurity.models

class PackageFile(
        val name: String = "",
        val version: String = "",
        val dependencies: Map<String, String> = emptyMap(),
        val devDependencies: Map<String, String> = emptyMap(),
        val optionalDependencies: Map<String, String> = emptyMap()
)
