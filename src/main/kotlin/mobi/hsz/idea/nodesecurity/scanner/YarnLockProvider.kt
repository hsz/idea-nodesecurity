package mobi.hsz.idea.nodesecurity.scanner

import com.esotericsoftware.yamlbeans.YamlReader
import com.intellij.psi.PsiFile
import mobi.hsz.idea.nodesecurity.models.YarnLock
import mobi.hsz.idea.nodesecurity.utils.first
import java.util.*

class YarnLockProvider(file: PsiFile) : LockProvider(file) {
    private val yaml = file.first().split("\n").mapNotNull {
        when {
            it.isEmpty() || it.startsWith("#") -> null
            it.startsWith("  ") -> it.replace(Regex("^(\\s*[\\w-.]+) "), "$1: ").substring(2)
            else -> it.replace(Regex("^\"?([^@\"]+).*"), "---\nname: $1")
        }
    }.joinToString("\n")

    private val dependencies: HashMap<String, Pair<String?, Map<String, String>>> = hashMapOf()
    private val reader = YamlReader(yaml)

    init {
        do {
            val item = reader.read(YarnLock::class.java)
            item?.let { dependencies[it.name] = Pair(it.version, it.dependencies + it.optionalDependencies) }
        } while (item != null)
    }

    override fun getDependency(name: String): Pair<String?, Map<String, String>>? = dependencies[name]
}
