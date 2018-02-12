package mobi.hsz.idea.nodesecurity.scanner

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import mobi.hsz.idea.nodesecurity.components.NodeSecuritySettings
import mobi.hsz.idea.nodesecurity.models.Advisory
import mobi.hsz.idea.nodesecurity.utils.*
import kotlin.coroutines.experimental.buildSequence

class VulnerabilitiesScanner {
    companion object {
        private val advisories: Map<String, List<Advisory>> = NodeSecuritySettings.getInstance().state.advisories

        private val getVulnerability = Companion::getDependencyVulnerability.memoize()
        private val check = Companion::checkDependencies.memoize()

        fun isFileVulnerable(file: PsiFile?): Boolean =
                file != null && Utils.isSupportedFile(file.virtualFile) && scanFile(file).iterator().hasNext()

        fun scanFile(file: PsiFile): Sequence<Pair<Advisory, PsiElement>> {
            (file.firstChild as? JsonObject).let {
                return mapDependencies(it, { element ->
                    val name = element.first()
                    val version = element.last()

                    AdvisoryReference()
                            .or { getVulnerability(name, version) }
                            .or { LockProvider.get(file)?.let { check(name, it, listOf(name)) } }
                            .get()
                            .let { if (it === null) null else it to element }
                })
            }
        }

        private fun checkDependencies(name: String, dependencies: LockProvider, path: List<String>): Advisory? {
            val (version, requires) = dependencies.get(name)
            return AdvisoryReference()
                    .or { getVulnerability(name, version) }
                    .or { requires.mapFirst { getVulnerability(it.key, it.value) } }
                    .or {
                        requires.mapFirst {
                            when (path.contains(it.key)) {
                                true -> null
                                else -> check(it.key, dependencies, path + it.key)
                            }
                        }
                    }
                    .get()
        }

        private fun <T> mapDependencies(json: JsonObject?, function: (element: JsonElement) -> T?): Sequence<T> =
                buildSequence<T> {
                    Constants.DEPENDENCIES_KEYS.forEach {
                        json?.findProperty(it)?.value?.children?.map {
                            if (it is JsonElement) function(it).let { if (it !== null) yield(it) }
                        }
                    }
                }

        private fun getDependencyVulnerability(name: String, version: String?): Advisory? {
            advisories.getOrDefault(name, emptyList()).forEach {
                if (it.isVulnerable(version)) {
                    return it
                }
            }
            return null
        }
    }
}
