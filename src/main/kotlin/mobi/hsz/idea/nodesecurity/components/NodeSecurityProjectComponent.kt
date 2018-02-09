package mobi.hsz.idea.nodesecurity.components

import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.AbstractProjectComponent
import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.psi.*
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.messages.MessageBusConnection
import mobi.hsz.idea.nodesecurity.NodeSecurityBundle
import mobi.hsz.idea.nodesecurity.messages.VulnerabilityNotification
import mobi.hsz.idea.nodesecurity.utils.Constants
import mobi.hsz.idea.nodesecurity.utils.Notify
import mobi.hsz.idea.nodesecurity.utils.Utils
import mobi.hsz.idea.nodesecurity.utils.VulnerabilitiesScanner


class NodeSecurityProjectComponent(project: Project) : AbstractProjectComponent(project), VulnerabilityNotification {
    private var messageBusConnection: MessageBusConnection? = null
    private var psiManager: PsiManager? = null
    private val publisher =
            myProject.messageBus.syncPublisher(VulnerabilityNotification.VULNERABILITY_NOTIFICATION_TOPIC)
    private val psiTreeChangeListener: PsiTreeChangeListener = object : PsiTreeChangeAdapter() {
        override fun childrenChanged(event: PsiTreeChangeEvent) {
            verifyFile(event.file)
        }
    }

    override fun projectOpened() {
        messageBusConnection = myProject.messageBus.connect()
        messageBusConnection!!.subscribe(VulnerabilityNotification.VULNERABILITY_NOTIFICATION_TOPIC, this)

        psiManager = PsiManager.getInstance(myProject)
        psiManager!!.addPsiTreeChangeListener(psiTreeChangeListener)

        DumbService.getInstance(myProject).runWhenSmart({
            val scope = GlobalSearchScope.projectScope(myProject)
            FilenameIndex.getFilesByName(myProject, Constants.PACKAGE_JSON, scope).forEach {
                verifyFile(it)
            }
        })
    }

    override fun projectClosed() {
        messageBusConnection?.disconnect()
        psiManager?.removePsiTreeChangeListener(psiTreeChangeListener)
    }

    private fun verifyFile(file: PsiFile?) {
        if (file != null && Utils.isSupportedFile(file)) {
            ApplicationManager.getApplication().runReadAction {
                if (VulnerabilitiesScanner.isFileVulnerable(file)) {
                    publisher.notify(file)
                }
            }
        }
    }

    override fun notify(file: PsiFile) {
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

    override fun getComponentName(): String = "NodeSecurityProjectComponent"
}
