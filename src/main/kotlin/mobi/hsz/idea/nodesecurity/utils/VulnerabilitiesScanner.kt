package mobi.hsz.idea.nodesecurity.utils

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.util.containers.ContainerUtil
import mobi.hsz.idea.nodesecurity.components.NodeSecuritySettings
import mobi.hsz.idea.nodesecurity.models.Advisory
import java.lang.ref.WeakReference
import kotlin.coroutines.experimental.buildSequence

class VulnerabilitiesScanner {
    companion object {
        private val advisories: Map<String, List<Advisory>> = NodeSecuritySettings.getInstance().state.advisories

        private val getVulnerability = ::getDependencyVulnerability.memoize()

        fun isFileVulnerable(file: PsiFile?): Boolean =
                file != null && Utils.isSupportedFile(file.virtualFile) && scanFile(file).iterator().hasNext()

        fun scanFile(file: PsiFile): Sequence<Pair<Advisory, PsiElement>> {
            (file.firstChild as? JsonObject).let {
                return mapDependencies(it, { element ->
                    val name = element.first()
                    val version = element.last()

                    WeakReference<Advisory?>(null)
                            .or { getVulnerability(name, version) }
                            .or {
                                (getPackageLockFile(file)?.firstChild as? JsonObject).let {
                                    checkDependencies(name, it?.findProperty("dependencies")?.lastChild as JsonObject)
                                }
                            }
                            .or {
                                // TODO: handle yarn.lock
                                getYarnLockFile(file)
                                null
                            }
                            .get()
                            .let { if (it === null) null else Pair(it, element) }
                })
            }
        }

        private fun checkDependencies(name: String, dependencies: JsonObject): Advisory? {
            val (version, requires) = (dependencies.findProperty(name)?.lastChild as? JsonObject).let {
                Pair(
                        it?.findProperty("version")?.last(),
                        it?.findProperty("requires")?.lastChild?.children
                )
            }

            return WeakReference<Advisory?>(null)
                    .or { getVulnerability(name, version!!) }
                    .or { requires?.mapFirst { getVulnerability(it.first(), it.last()) } }
                    .or { requires?.mapFirst { checkDependencies(it.first(), dependencies) } }
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

        private fun getPackageLockFile(file: PsiFile): PsiFile? =
                file.virtualFile.parent.findChild(Constants.PACKAGE_LOCK_JSON)?.let {
                    PsiManager.getInstance(file.project).findFile(it)
                }

        private fun getYarnLockFile(file: PsiFile): PsiFile? = null

        private fun getDependencyVulnerability(name: String, version: String): Advisory? {
            advisories.getOrDefault(name, emptyList()).forEach {
                if (it.isVulnerable(version)) {
                    return it
                }
            }
            return null
        }
    }
}

fun <T> WeakReference<T?>.or(other: () -> T?): WeakReference<T?> = when {
    this.get() !== null -> this
    else -> WeakReference(other())
}

fun <T, R> Array<T>.mapFirst(function: (element: T) -> R?): R? {
    this.forEach {
        val result = function(it)
        if (result != null) {
            return result
        }
    }
    return null
}

fun PsiElement.first(): String = this.firstChild.text.removeSurrounding("\"")
fun PsiElement.last(): String = this.lastChild.text.removeSurrounding("\"")

fun <A, B, R> ((A, B) -> R).memoize(): (A, B) -> R? {
    val cache: MutableMap<Pair<A, B>, WeakReference<R>> = ContainerUtil.newHashMap()
    return { a: A, b: B -> cache.getOrPut(a to b, { WeakReference(this(a, b)) }).get() }
}
