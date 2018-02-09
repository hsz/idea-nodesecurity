package mobi.hsz.idea.nodesecurity.utils

class Constants {
    companion object {
        internal const val API_ENDPOINT = "https://api.nodesecurity.io"
        internal const val PACKAGE_JSON = "package.json"
        internal const val PACKAGE_LOCK_JSON = "package-lock.json"
        internal const val YARN_LOCK = "package-lock.json"
        internal const val NODE_MODULES = "node_modules"
        internal val DEPENDENCIES_KEYS = arrayOf(
                "dependencies", "devDependencies", "peerDependencies", "bundledDependencies", "optionalDependencies"
        )
    }
}
