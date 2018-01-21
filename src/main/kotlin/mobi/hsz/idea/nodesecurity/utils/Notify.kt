package mobi.hsz.idea.nodesecurity.utils

import com.intellij.notification.*
import com.intellij.openapi.project.Project
import mobi.hsz.idea.nodesecurity.NodeSecurityBundle

class Notify {
    companion object {
        private val NOTIFICATION_GROUP = NotificationGroup(
                NodeSecurityBundle.message("notification.group"),
                NotificationDisplayType.STICKY_BALLOON,
                true
        )

        fun show(project: Project, title: String, content: String, type: NotificationType) {
            show(project, title, content, NOTIFICATION_GROUP, type, null)
        }


        fun show(project: Project, title: String, content: String, type: NotificationType,
                 listener: NotificationListener?) {
            show(project, title, content, NOTIFICATION_GROUP, type, listener)
        }


        fun show(project: Project, title: String, content: String, group: NotificationGroup, type: NotificationType,
                 listener: NotificationListener?) {
            val notification = group.createNotification(title, content, type, listener)
            Notifications.Bus.notify(notification, project)
        }
    }
}
