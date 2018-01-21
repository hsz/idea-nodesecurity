package mobi.hsz.idea.nodesecurity.utils

import com.intellij.json.psi.JsonElement
import com.intellij.json.psi.JsonObject
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import mobi.hsz.idea.nodesecurity.components.NodeSecuritySettings
import mobi.hsz.idea.nodesecurity.models.Advisory
import kotlin.coroutines.experimental.buildSequence

class VulnerabilitiesScanner {
    companion object {
        private val advisories: Map<String, List<Advisory>> = NodeSecuritySettings.getInstance().state.advisories
        private val keys = arrayOf("dependencies", "devDependencies", "optionalDependencies")

        fun scan(file: PsiFile): Sequence<Pair<Advisory, PsiElement>> =
                buildSequence {
                    val json: JsonObject = file.firstChild as JsonObject

                    keys.forEach {
                        json.findProperty(it)?.value?.children?.map {
                            if (it is JsonElement) {
                                val name = it.firstChild.text.trim()
                                val version = it.lastChild.text.trim()
                                val element = it.originalElement
                                advisories.getOrDefault(name, emptyList()).forEach {
                                    if (it.isVulnerable(version)) yield(Pair(it, element))
                                }
                            }
                        }
                    }
                }
    }
}

fun String.trim(): String = StringUtil.replace(this, "\"", "")
