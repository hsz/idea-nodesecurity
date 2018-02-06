package mobi.hsz.idea.nodesecurity.utils

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.containers.ContainerUtil
import mobi.hsz.idea.nodesecurity.components.NodeSecuritySettings
import mobi.hsz.idea.nodesecurity.models.Advisory
import java.lang.ref.WeakReference
import kotlin.coroutines.experimental.buildSequence

class VulnerabilitiesScanner {
    companion object {
        private val advisories: Map<String, List<Advisory>> = NodeSecuritySettings.getInstance().state.advisories
        private val keys = arrayOf("dependencies", "devDependencies", "optionalDependencies")

        private val getVulnerability = ::getDependencyVulnerability.memoize()

        fun scan(file: PsiFile): Sequence<Pair<Advisory, PsiElement>> =
                buildSequence {
                    if (file.firstChild is JsonObject) {
                        val json: JsonObject = file.firstChild as JsonObject

                        keys.forEach {
                            json.findProperty(it)?.value?.children?.map {
                                if (it is JsonElement) {
                                    val name = it.firstChild.text.trim()
                                    val version = it.lastChild.text.trim()
                                    val element = it.originalElement

                                    val vulnerability = getVulnerability(name, version)
                                    if (vulnerability !== null) {
                                        yield(Pair(vulnerability, element))
                                    }
                                }
                            }
                        }
                    }
                }

        private fun getDependencyVulnerability(name: String, version: String): Advisory? {
            println(name + ":" + version)

            advisories.getOrDefault(name, emptyList()).forEach {
                if (it.isVulnerable(version)) {
                    return it
                }
            }
            return null
        }

        fun isFileVulnerable(file: PsiFile?): Boolean =
                file != null && Utils.isSupportedFile(file.virtualFile) && scan(file).iterator().hasNext()
    }
}

fun String.trim(): String = StringUtil.replace(this, "\"", "")

fun <A, B, R> ((A, B) -> R).memoize(): (A, B) -> R? {
    val cache: MutableMap<Pair<A, B>, WeakReference<R>> = ContainerUtil.newHashMap()
    return { a: A, b: B ->
        cache.getOrPut(a to b, { WeakReference(this(a, b)) }).get()
    }
}
