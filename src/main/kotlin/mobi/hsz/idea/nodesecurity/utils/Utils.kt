package mobi.hsz.idea.nodesecurity.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import mobi.hsz.idea.nodesecurity.utils.Constants.Companion.NODE_MODULES
import mobi.hsz.idea.nodesecurity.utils.Constants.Companion.PACKAGE_JSON

class Utils {
    companion object {
        fun openFile(project: Project, file: VirtualFile) {
            FileEditorManager.getInstance(project).openFile(file, true)
        }

        fun isInNodeModules(file: VirtualFile) : Boolean = file.path.toLowerCase().contains(NODE_MODULES)

        fun isSupportedFile(file: VirtualFile?): Boolean = file?.name == PACKAGE_JSON && !isInNodeModules(file)

        fun isSupportedFile(file: PsiFile?): Boolean = isSupportedFile(file?.virtualFile)
    }
}
