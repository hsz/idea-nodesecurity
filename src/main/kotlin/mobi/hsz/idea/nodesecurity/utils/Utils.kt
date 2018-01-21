package mobi.hsz.idea.nodesecurity.utils

import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class Utils {
    companion object {
        fun openFile(project: Project, file: VirtualFile) {
            FileEditorManager.getInstance(project).openFile(file, true)
        }
    }
}
