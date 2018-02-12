package mobi.hsz.idea.nodesecurity.scanner

import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import mobi.hsz.idea.nodesecurity.utils.Constants
import mobi.hsz.idea.nodesecurity.utils.memoize

abstract class LockProvider(file: PsiFile) {
    private val hashCode = file.hashCode()

    enum class Type {
        PackageLock, YarnLock
    }

    companion object {
        val get = ::getProvider.memoize()

        private fun getProvider(file: PsiFile): LockProvider? = Type.values()
                .map { lazy { createProvider(it, file) } }
                .find { it.value != null }?.value

        private fun createProvider(type: Type, file: PsiFile): LockProvider? = when (type) {
            Type.PackageLock -> Constants.PACKAGE_LOCK_JSON
            Type.YarnLock -> Constants.YARN_LOCK
        }.let {
            file.virtualFile.parent.findChild(it)?.let {
                PsiManager.getInstance(file.project).findFile(it)
            }?.let {
                        when (type) {
                            Type.PackageLock -> PackageLockProvider(it)
                            Type.YarnLock -> YarnLockProvider(it)
                        }
                    }
        }
    }

    fun get(name: String): Pair<String?, Map<String, String>> =
            getDependency(name) ?: Pair<String?, Map<String, String>>(null, hashMapOf())

    protected abstract fun getDependency(name: String): Pair<String?, Map<String, String>>?

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return hashCode == (other as LockProvider).hashCode
    }

    override fun hashCode(): Int = hashCode
}
