package mobi.hsz.idea.nodesecurity.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.util.containers.ContainerUtil
import mobi.hsz.idea.nodesecurity.utils.Constants.Companion.NODE_MODULES
import mobi.hsz.idea.nodesecurity.utils.Constants.Companion.PACKAGE_JSON
import java.lang.ref.WeakReference

class Utils {
    companion object {
        fun openFile(project: Project, file: VirtualFile) {
            FileEditorManager.getInstance(project).openFile(file, true)
        }

        fun isInNodeModules(file: VirtualFile): Boolean = file.path.toLowerCase().contains(NODE_MODULES)

        fun isSupportedFile(file: VirtualFile?): Boolean = file?.name == PACKAGE_JSON && !isInNodeModules(file)

        fun isSupportedFile(file: PsiFile?): Boolean = isSupportedFile(file?.virtualFile)
    }
}

fun <K, V, R> Map<K, V>.mapFirst(function: (element: Map.Entry<K, V>) -> R?): R? {
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

fun <A, R> ((A) -> R).memoize(): (A) -> R? {
    val cache: MutableMap<A, WeakReference<R>> = ContainerUtil.newHashMap()
    return { a: A -> cache.getOrPut(a, { WeakReference(this(a)) }).get() }
}

fun <A, B, R> ((A, B) -> R).memoize(): (A, B) -> R? {
    val cache: MutableMap<Pair<A, B>, WeakReference<R>> = ContainerUtil.newHashMap()
    return { a: A, b: B -> cache.getOrPut(a to b, { WeakReference(this(a, b)) }).get() }
}

fun <A, B, C, R> ((A, B, C) -> R).memoize(): (A, B, C) -> R? {
    val cache: MutableMap<Triple<A, B, C>, WeakReference<R>> = ContainerUtil.newHashMap()
    return { a: A, b: B, c: C ->
        cache.getOrPut(Triple(a, b, c), {
            WeakReference(this(a, b, c))
        }).get()
    }
}
