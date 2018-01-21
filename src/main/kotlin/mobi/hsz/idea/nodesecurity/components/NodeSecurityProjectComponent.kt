package mobi.hsz.idea.nodesecurity.components

import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import mobi.hsz.idea.nodesecurity.NodeSecurityBundle
import mobi.hsz.idea.nodesecurity.utils.Notify
import mobi.hsz.idea.nodesecurity.utils.Utils
import mobi.hsz.idea.nodesecurity.utils.VulnerabilitiesScanner


class NodeSecurityProjectComponent(project: Project) : AbstractProjectComponent(project) {
    override fun getComponentName(): String = "NodeSecurityProjectComponent"

    override fun projectOpened() {
        DumbService.getInstance(myProject).runWhenSmart({
            val scope = GlobalSearchScope.projectScope(myProject)
            FilenameIndex.getFilesByName(myProject, "package.json", scope).forEach { file ->
                val path = file.virtualFile.path
                if (file.name == "package.json" && !path.contains("node_modules")) {
                    if (VulnerabilitiesScanner.scan(file).iterator().hasNext()) {
                        Notify.show(
                                myProject,
                                NodeSecurityBundle.message("notification.vulnerable.title"),
                                NodeSecurityBundle.message("notification.vulnerable.content", file.virtualFile.path),
                                NotificationType.ERROR,
                                NotificationListener { notification, _ ->
                                    notification.expire()
                                    Utils.openFile(myProject, file.virtualFile)
                                }
                        )
                    }
                }
            }
        })
    }
}
