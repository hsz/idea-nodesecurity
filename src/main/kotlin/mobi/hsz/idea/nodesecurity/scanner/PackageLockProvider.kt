package mobi.hsz.idea.nodesecurity.scanner

import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiFile
import mobi.hsz.idea.nodesecurity.utils.first
import mobi.hsz.idea.nodesecurity.utils.last

class PackageLockProvider(file: PsiFile) : LockProvider(file) {
    private val dependencies = (file.firstChild as? JsonObject)?.findProperty("dependencies")?.lastChild as JsonObject

    override fun getDependency(name: String): Pair<String?, Map<String, String>>? =
            (dependencies.findProperty(name)?.lastChild as? JsonObject)?.let {
                Pair(
                        it.findProperty("version")?.last(),
                        it.findProperty("requires")?.lastChild?.children?.map {
                            it.first() to it.last()
                        }?.toMap() ?: emptyMap()
                )
            }
}
