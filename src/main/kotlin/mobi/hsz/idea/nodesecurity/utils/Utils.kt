package mobi.hsz.idea.nodesecurity.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

class Utils {
    companion object {
        fun openFile(project: Project, file: VirtualFile) {
            FileEditorManager.getInstance(project).openFile(file, true)
        }

        fun isInNodeModules(file: VirtualFile) : Boolean = file.path.toLowerCase().contains(Constants.NODE_MODULES)

        fun isSupportedFile(file: VirtualFile?): Boolean =
                file != null && Constants.SUPPORTED_FILES.contains(file.name) && !isInNodeModules(file)

        fun isSupportedFile(file: PsiFile?): Boolean =
                file != null && isSupportedFile(file.virtualFile)
    }
}
